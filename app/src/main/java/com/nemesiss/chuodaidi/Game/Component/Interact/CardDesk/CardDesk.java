package com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.Android.Utils.EventProxy;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.RoundImageTransform;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.BaseMiddleware;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.CardDeskMiddlewarePool;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.MiddlewareType;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CardDesk extends ConstraintLayout
{


    // View控制相关变量
    private float CardWHRatio; // 扑克牌  宽/长  的比值
    private float r1; // 花色部分宽/牌宽
    private float r2; // 花色部分高/牌高

    // 手牌区容器长宽控制
    private int SelfPokeContainerHeight; // 手牌区域容器高度
    private int SelfPokeContainerWidth; // 手牌区域容器宽度

    // 其他玩家不可见手牌长宽控制
    private int TwoSideContainerHeight; // 两边纵向手牌区域高度
    private int TwoSideContainerWidth; // 两边纵向手牌区域宽度

    private int TopContainerHeight; //顶部手牌区域高度
    private int TopContainerWidth;  // -------  宽度

    // 出牌区容器长宽控制
    private int HorizontalShowContainerWidth;
    private int HorizontalShowContainerHeight;

    private int VerticalShowContainerWidth;
    private int VerticalShowContainerHeight;

    // 牌面显示控制变量
    private List<Boolean> SelfCardStatus; // 当前玩家手牌选中状态，选中为true，不选为false
    private List<Boolean> SelfCardMoveLock; // 防止触摸事件重复检测的辅助属性


    private List<ImageView> SelfCardImageList;  // 存玩家手中牌面的ImageView

    private List<Card>[] AllHadShownCard;  // 存本轮次中所有已经出的牌


    // 四个方位玩家的代号（存取数组时用）
    public static final int SELF = 0, RIGHT = 1, TOP = 2, LEFT = 3;

    // 存手牌的容器
    private LinearLayout[] PokeCollections;// 0 1 2 3 Self, Right, Top, Left
    // 存出牌的容器
    private LinearLayout[] ShowPokeCollections;
    private TextView[] NotShowTextViews;

    private Context mContext;

    //  是否正在测量子容器的宽高
    boolean IsMeasuringChildView = true;

    // 等待CardDesk完成测量子容器大小的时候所需要的数据结构
    private EventProxy<String> AllMeasureChildViewTask;
    private Queue<Runnable> PendingOnMeasureChildView = new LinkedList<>();


    // 玩家手牌控制面板
    private LinearLayout SelfPokeControlPanel;
    // 出牌
    private Button ShowCard;
    // 不出
    private Button PassCard;

    private Player Self;

    private CardDeskMiddlewarePool middlewarePool = new CardDeskMiddlewarePool(this);

    public Button getShowCardButton()
    {
        return ShowCard;
    }

    public Button getPassCardButton()
    {
        return PassCard;
    }

    public synchronized List<Card> getSelectedCards()
    {
        List<Card> result = new ArrayList<>();
        for (int i = 0; i < SelfCardStatus.size(); i++)
        {
            if(SelfCardStatus.get(i))
            {
                result.add(Self.GetHandCards().get(i));
            }
        }
        return result;
    }

    public CardDesk(Context context)
    {
        super(context);
        mContext = context;
        PrepareChildViewMeasureEventProxy();
        PrepareInnerMiddleware();

    }

    public CardDesk(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CardDesk);
        CardWHRatio = ta.getFloat(R.styleable.CardDesk_card_wh_ratio, 1f);
        r1 = ta.getFloat(R.styleable.CardDesk_card_r1, 1f);
        r2 = ta.getFloat(R.styleable.CardDesk_card_r2, 1f);
        ta.recycle();
        PrepareChildViewMeasureEventProxy();
        PrepareInnerMiddleware();
    }

    public CardDesk(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CardDesk);
        CardWHRatio = ta.getFloat(R.styleable.CardDesk_card_wh_ratio, 1f);
        r1 = ta.getFloat(R.styleable.CardDesk_card_r1, 1f);
        r2 = ta.getFloat(R.styleable.CardDesk_card_r2, 1f);
        ta.recycle();
        PrepareChildViewMeasureEventProxy();
        PrepareInnerMiddleware();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        if (IsMeasuringChildView) Init();
    }

    private void PrepareInnerMiddleware()
    {

        middlewarePool.AddMiddleware(MiddlewareType.BEFORE_SHOW_CARD,new CardValidator());

        middlewarePool.SetEndupMiddleware(MiddlewareType.BEFORE_SHOW_CARD, new BaseMiddleware() {
            @Override
            public void Handle(CardDesk deskSelf, Context context, CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger) {
                ShowCards(Self);
                nextTrigger.next();
            }
        });
    }

    public boolean AddMiddleware(int MiddlewareType,BaseMiddleware middleware) {
        return middlewarePool.AddMiddleware(MiddlewareType, middleware);
    }

    private void PrepareChildViewMeasureEventProxy()
    {
        AllMeasureChildViewTask = new EventProxy<>();

        AllMeasureChildViewTask.all(new EventProxy.EventResult<String>()
        {
            @Override
            public void handle(ConcurrentHashMap<String, Object> evs, ConcurrentHashMap<String, EventProxy.EventStatus> evStatus)
            {
                IsMeasuringChildView = false;
                while (!PendingOnMeasureChildView.isEmpty())
                {

                    // 重新执行因为子View没Measure完而挂起的任务。
                    PendingOnMeasureChildView.peek().run();
                    PendingOnMeasureChildView.remove();
                }
            }
        }, "self", "right", "top", "show_self");
    }


    public synchronized void DoAfterCardDeskLoaded(Runnable action)
    {
        if (IsMeasuringChildView)
        {
            PendingOnMeasureChildView.add(action);
        } else action.run();
    }

    public void ShowPokeControlPanel()
    {
        if (IsMeasuringChildView)
        {
            PendingOnMeasureChildView.add(() -> {
                SelfPokeControlPanel.setVisibility(VISIBLE);
            });
        } else
        {
            SelfPokeControlPanel.setVisibility(VISIBLE);
        }
    }

    public void HidePokeControlPanel()
    {
        if (IsMeasuringChildView)
        {
            PendingOnMeasureChildView.add(() -> {
                SelfPokeControlPanel.setVisibility(GONE);
            });
        } else
        {
            SelfPokeControlPanel.setVisibility(GONE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Init()
    {
        // 把LinearLayout存到数组中，索引用

        SelfPokeControlPanel = findViewById(R.id.SelfPokeControlPanel);
        ShowCard = findViewById(R.id.PickCard);
        PassCard = findViewById(R.id.PassCard);

        ShowCard.setOnClickListener((v) ->
                middlewarePool.ExecuteMiddlewares(MiddlewareType.BEFORE_SHOW_CARD));

        PassCard.setOnClickListener((v) ->
                ShowNoCards(Self));

        PokeCollections = new LinearLayout[4];
        ShowPokeCollections = new LinearLayout[4];
        NotShowTextViews = new TextView[4];

        PokeCollections[SELF] = findViewById(R.id.SelfPokeCollection);
        ShowPokeCollections[SELF] = findViewById(R.id.SelfShowPokeCollection);
        NotShowTextViews[SELF] = findViewById(R.id.SelfNotShow);

        PokeCollections[RIGHT] = findViewById(R.id.RightPlayerPokeCollection);
        ShowPokeCollections[RIGHT] = findViewById(R.id.RightPlayerShowPokeCollection);
        NotShowTextViews[RIGHT] = findViewById(R.id.RightNotShow);


        PokeCollections[LEFT] = findViewById(R.id.LeftPlayerPokeCollection);
        ShowPokeCollections[LEFT] = findViewById(R.id.LeftPlayerShowPokeCollection);
        NotShowTextViews[LEFT] = findViewById(R.id.LeftNotShow);

        PokeCollections[TOP] = findViewById(R.id.TopPlayerPokeCollection);
        ShowPokeCollections[TOP] = findViewById(R.id.TopPlayerShowPokeCollection);
        NotShowTextViews[TOP] = findViewById(R.id.TopNotShow);


        //post拿到计算完成后的宽高

        PokeCollections[SELF].post(() -> {
            SelfPokeContainerWidth = PokeCollections[SELF].getWidth();
            SelfPokeContainerHeight = PokeCollections[SELF].getHeight();
            AllMeasureChildViewTask.tryemit("self", EventProxy.EventStatus.Finish, "null");
        });

        PokeCollections[RIGHT].post(() -> {
            TwoSideContainerHeight = PokeCollections[RIGHT].getHeight();
            TwoSideContainerWidth = PokeCollections[RIGHT].getWidth();
            AllMeasureChildViewTask.tryemit("right", EventProxy.EventStatus.Finish, "null");
        });

        PokeCollections[TOP].post(() -> {
            TopContainerHeight = PokeCollections[TOP].getHeight();
            TopContainerWidth = PokeCollections[TOP].getWidth();
            AllMeasureChildViewTask.tryemit("top", EventProxy.EventStatus.Finish, "null");
        });

        ShowPokeCollections[SELF].post(() -> {
            HorizontalShowContainerHeight = ShowPokeCollections[SELF].getHeight();
            HorizontalShowContainerWidth = ShowPokeCollections[SELF].getWidth();

            VerticalShowContainerHeight = HorizontalShowContainerHeight;
            VerticalShowContainerWidth = HorizontalShowContainerWidth;
            AllMeasureChildViewTask.tryemit("show_self", EventProxy.EventStatus.Finish, "null");
        });


        // 设置自己牌组触摸事件
        PokeCollections[SELF].setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    {
                        Collections.fill(SelfCardMoveLock, false);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                    {
                        int evX = Math.round(event.getX());
                        int evY = Math.round(event.getY());
                        int childCount = PokeCollections[SELF].getChildCount();
                        for (int i = 0; i < childCount; i++)
                        {
                            if (DetectCardSelected(i, evX, evY) && !SelfCardMoveLock.get(i))
                            {
                                HandleCardSelected(i);
                                SelfCardMoveLock.set(i, true);
                            }
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }


    // 计算横向牌的大小
    private int MeasureHorizontalCardHeight(int ContainerHeight)
    {
        return (int) (ContainerHeight / (1 + r1));
    }

    private int MeasureHorizontalCardWidth(int CardHeight)
    {
        return (int) (CardHeight * CardWHRatio);
    }

    // 计算横向牌应该重叠多少
    private int MeasureHorizontalMarginBottom(int CardHeight)
    {
        return (int) (CardHeight * r1);
    }

    private int MeasureHorizontalMarginStart(int CardWidth)
    {
        //return (int)(-(1-r1)*width);
        // 感觉只要给牌的一半就足够
        return (int) (-(0.6f) * CardWidth);
    }


    // 计算纵向牌大小
    private int MeasureVerticalCardHeight(int VerticalContainerWidth)
    {
        return (int) (VerticalContainerWidth * (1f / CardWHRatio));
    }

    private int MeasureVerticalCardWidth(int VerticalContainerWidth)
    {
        return (int) (VerticalContainerWidth);
    }

    // 计算
    private int MeasureVerticalCardMarginTop(int CardHeight, int ContainerHeight)
    {
        //标准 return
        //return -(int) ((CardHeight - ((ContainerHeight - CardHeight) / 13)) + 0.5f);
        return (int) (-(0.85f) * CardHeight);
    }

    private boolean DetectCardSelected(int position, int evX, int evY)
    {
        int end = SelfCardImageList.size() - 1;
        ImageView child = SelfCardImageList.get(position);
        int left = child.getLeft();
        int right = child.getRight();
        int top = child.getTop();
        int bottom = child.getBottom();
        if (position == end)
        {
            return (left <= evX && evX <= right && top <= evY && evY <= bottom);
        } else
        {
            int width = child.getWidth();
            int marginStart = width / 2;
            right = right - marginStart;
            return (left <= evX && evX <= right && top <= evY && evY <= bottom);
        }
    }

    private void HandleCardSelected(int position)
    {
        ImageView iv = SelfCardImageList.get(position);
        boolean status = SelfCardStatus.get(position);
        int height = iv.getHeight();

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
        lp.bottomMargin = !status ? MeasureHorizontalMarginBottom(height) : 0;

        iv.setLayoutParams(lp);
        SelfCardStatus.set(position, !status);
    }


    private void LoadSelfCardsAsImageToContainer(List<Card> cards)
    {

        try
        {
            List<Integer> cardsResource = AppUtil.ConvertCardToDrawable(cards);

            for (int i = 0; i < cardsResource.size(); i++)
            {
                ImageView iv = CreatePokeImageView();

                Uri imgUri = AppUtil.ParseResourceIdToUri(cardsResource.get(i));

                Glide.with(mContext)
                        .load(imgUri)
                        .transform(new RoundImageTransform(mContext, 20, 4))
                        .dontAnimate().into(iv);
                int cardHeight = MeasureHorizontalCardHeight(SelfPokeContainerHeight);
                int cardWidth = MeasureHorizontalCardWidth(cardHeight);
                int marginStart = MeasureHorizontalMarginStart(cardWidth);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardWidth, cardHeight);
                if (i > 0)
                {
                    lp.setMarginStart((marginStart));
                }
                SelfCardImageList.add(iv);
                PokeCollections[SELF].addView(iv, lp);
            }

        } catch (NoSuchFieldException e)
        {
            Log.d("CardDesk", "找不到牌信息指定的牌图片 NoSuchFieldException");

            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            Log.d("CardDesk", "找不到牌信息指定的牌图片 IllegalAccessException");
            e.printStackTrace();
        }
    }

    private void LoadOtherPlayerCardsAsImageToContainer(int count, int position)
    {
        //
        Uri bgUri = AppUtil.ParseResourceIdToUri(R.drawable.poke_back);

        for (int i = 0; i < count; i++)
        {
            ImageView iv = CreatePokeImageView();
            Glide.with(mContext)
                    .load(bgUri)
                    .transform(new RoundImageTransform(mContext, 20, 4))
                    .dontAnimate()
                    .into(iv);
            int width, height;
            LinearLayout.LayoutParams lp = null;
            switch (position)
            {
                case LEFT:
                case RIGHT:
                {
                    width = MeasureVerticalCardWidth(TwoSideContainerWidth);
                    height = MeasureVerticalCardHeight(TwoSideContainerWidth);
                    lp = new LinearLayout.LayoutParams(width, height);
                    if (i > 0)
                    {
                        lp.topMargin = MeasureVerticalCardMarginTop(height, TwoSideContainerHeight);
                    }
                    break;
                }
                case TOP:
                {
                    height = (TopContainerHeight);
                    width = MeasureHorizontalCardWidth(height) + 1;
                    lp = new LinearLayout.LayoutParams(width, height);
                    if (i > 0)
                    {
                        lp.leftMargin = (int) (MeasureHorizontalMarginStart(width) * 1.3f);
                    }
                    break;
                }
            }
            PokeCollections[position].addView(iv, lp);
        }
    }

    private ImageView CreatePokeImageView()
    {
        ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return iv;
    }

    // 自己出牌用
    private void PutCardToShowContainer(List<ImageView> selectedCardView)
    {
        for (int i = 0; i < selectedCardView.size(); i++)
        {
            ImageView iv = selectedCardView.get(i);
            // 把这张牌从手牌中移除
            PokeCollections[SELF].removeView(iv);
            SelfCardImageList.remove(iv);
            // 重新测量宽高之后放置到出牌区域

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
            // 为它重新测量宽高
            int height = (HorizontalShowContainerHeight);
            int width = MeasureHorizontalCardWidth(height);
            lp.width = width;
            lp.height = height;
            if (i > 0)
            {
                lp.leftMargin = MeasureHorizontalMarginStart(width);
            } else
            {
                lp.leftMargin = 0;
            }
            ShowPokeCollections[SELF].addView(iv, lp);
            Log.d("CardDesk", String.valueOf(ShowPokeCollections[SELF].getWidth()));
        }
        // 修复第一张牌的偏移
        if (!SelfCardImageList.isEmpty())
        {
            ImageView iv = SelfCardImageList.get(0);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) iv.getLayoutParams();
            lp.leftMargin = 0;
            iv.setLayoutParams(lp);
        }
    }

    // 别的玩家出牌用
    private void PutCardToShowContainer(int position, List<Card> cards)
    {
        try
        {
            List<Integer> drawRes = AppUtil.ConvertCardToDrawable(cards);

            for (int i = 0; i < cards.size(); i++)
            {
                ImageView iv = CreatePokeImageView();
                Glide.with(mContext)
                        .load(drawRes.get(i))
                        .transform(new RoundImageTransform(mContext, 20, 4))
                        .dontAnimate()
                        .into(iv);

                int height = (VerticalShowContainerHeight);
                int width = MeasureHorizontalCardWidth(height);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
                if (i > 0)
                {
                    lp.leftMargin = (int) (MeasureHorizontalMarginStart(width) * 1.3f);
                } else
                {
                    lp.leftMargin = 0;
                }
                ShowPokeCollections[position].addView(iv, lp);
            }
            // 都是牌的背面，只要随便Remove掉足够数量的牌即可

            int childCnt = PokeCollections[position].getChildCount();
            for (int i = childCnt - 1; i >= childCnt - cards.size(); i--)
            {
                PokeCollections[position].removeViewAt(i);
            }

        } catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void ShowNoCards(Player self)
    {
        self.ShowCard(new ArrayList<>());
        NotShowTextViews[SELF].setVisibility(VISIBLE);
    }

    public void ShowCards(Player self)
    {
        // 此函数用于我方出牌, 牌面选择完成之后内部调用有参的SelectCard显示出牌信息

        // Get all selected card
        List<Card> PlayerAllCard = self.GetHandCards();
        List<ImageView> Selectediv = new ArrayList<>();
        List<Integer> SelectedCardIndex = new ArrayList<>();
        List<Card> SelectedCard = new ArrayList<>();
        // 先检测当前有哪些牌是被选中的
        for (int i = 0; i < SelfCardStatus.size(); i++)
        {
            if (SelfCardStatus.get(i))
            {
                ImageView iv = (ImageView) PokeCollections[SELF].getChildAt(i);
                Selectediv.add(iv);
                SelectedCardIndex.add(i);
                SelectedCard.add(PlayerAllCard.get(i));
            }
        }


        Collections.fill(SelfCardStatus, false);
        for (int i = 0; i < SelectedCardIndex.size(); i++)
        {
            SelfCardStatus.remove(0);
        }

        if (!Selectediv.isEmpty())
        {
            PutCardToShowContainer(Selectediv);
            AllHadShownCard[SELF].addAll(SelectedCard);
            self.ShowCard(SelectedCardIndex);
        } else
        {
            // TODO 显示不出
            ShowNoCards(Self);
        }
    }

    public void ShowCards(int position, List<Card> cards)
    {
        if (!cards.isEmpty())
        {
            PutCardToShowContainer(position, cards);
            AllHadShownCard[position].addAll(cards);
        } else
        {
            NotShowTextViews[position].setVisibility(VISIBLE);
        }
    }

    public List<Card>[] GetAllHadShownCards()
    {
        return AllHadShownCard;
    }

    public void NewTurn()
    {
        // 新的一轮，把场上出的全部牌给清除掉
        for (int i = 0; i < 4; i++)
        {
            AllHadShownCard[i].clear();
            ShowPokeCollections[i].removeAllViews();
            NotShowTextViews[i].setVisibility(GONE);
        }
    }

    public void NewCompetition(Player self)
    {
        Self = self;
        SelfCardImageList = new ArrayList<>();
        SelfCardStatus = new ArrayList<>(13);
        SelfCardMoveLock = new ArrayList<>(13);

        for (int i = 0; i < 13; i++)
        {
            SelfCardStatus.add(false);
            SelfCardMoveLock.add(false);
        }

        AllHadShownCard = new List[4];
        for (int i = 0; i < 4; i++)
        {
            AllHadShownCard[i] = new ArrayList<>();
        }

        if (IsMeasuringChildView)
        {
            PendingOnMeasureChildView.add(() -> {
                // 牌桌不持有任何玩家牌信息，只持有牌的图片
                LoadSelfCardsAsImageToContainer(self.GetHandCards());
                LoadOtherPlayerCardsAsImageToContainer(13, RIGHT);
                LoadOtherPlayerCardsAsImageToContainer(13, TOP);
                LoadOtherPlayerCardsAsImageToContainer(13, LEFT);
            });
        } else
        {
            // 牌桌不持有任何玩家牌信息，只持有牌的图片
            LoadSelfCardsAsImageToContainer(self.GetHandCards());
            LoadOtherPlayerCardsAsImageToContainer(13, RIGHT);
            LoadOtherPlayerCardsAsImageToContainer(13, TOP);
            LoadOtherPlayerCardsAsImageToContainer(13, LEFT);
        }
    }

}
