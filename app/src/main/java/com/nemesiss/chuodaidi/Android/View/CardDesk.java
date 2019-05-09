package com.nemesiss.chuodaidi.Android.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CardDesk extends ConstraintLayout {

    // View控制相关变量
    private float CardWHRatio;
    private float r1;
    private float r2;

    // 手牌区容器长宽控制
    private int SelfPokeContainerHeight;
    private int SelfPokeContainerWidth;

    // 其他玩家不可见手牌长宽控制
    private int TwoSideContainerHeight;
    private int TwoSideContainerWidth;

    private int TopContainerHeight;
    private int TopContainerWidth;

    // 出牌区容器长宽控制
    private int HorizontalShowContainerWidth;
    private int HorizontalShowContainerHeight;

    private int VerticalShowContainerWidth;
    private int VerticalShowContainerHeight;

    // 牌面显示控制变量
    private List<Boolean> SelfCardStatus;
    private List<Boolean> SelfCardMoveLock;


    private HashMap<Card,ImageView> SelfCardsMap;
    private List<Card> SelfCardList;
    private List<ImageView> SelfCardImageList;

    private List<Card>[] AllHadShownCard;


    private static final int SELF = 0,RIGHT = 1,TOP = 2,LEFT = 3;
    private LinearLayout[] PokeCollections;// 0 1 2 3 Self, Right, Top, Left
    private LinearLayout[] ShowPokeCollections;

    private Context mContext;
    public CardDesk(Context context) {
        super(context);
        mContext = context;
        Init();
    }

    public CardDesk(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        Init();
    }

    public CardDesk(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        Init();
    }

    private void PreprareCards(List<Card> MyCards)
    {

    }

    @SuppressLint("ClickableViewAccessibility")
    public void Init()
    {
        // 把LinearLayout存到数组中，索引用
        PokeCollections = new LinearLayout[4];
        ShowPokeCollections = new LinearLayout[4];

        PokeCollections[SELF] = findViewById(R.id.SelfPokeCollection);
        ShowPokeCollections[SELF] = findViewById(R.id.SelfShowPokeCollection);

        PokeCollections[RIGHT] = findViewById(R.id.RightPlayerPokeCollection);
        ShowPokeCollections[RIGHT] = findViewById(R.id.RightPlayerShowPokeCollection);

        PokeCollections[LEFT] = findViewById(R.id.LeftPlayerPokeCollection);
        ShowPokeCollections[LEFT] = findViewById(R.id.LeftPlayerShowPokeCollection);

        PokeCollections[TOP] = findViewById(R.id.TopPlayerPokeCollection);
        ShowPokeCollections[TOP] = findViewById(R.id.TopPlayerShowPokeCollection);

        //post拿到计算完成后的宽高

        // 设置自己牌组触摸事件
        PokeCollections[SELF].setOnTouchListener(new View.OnTouchListener() {
        // ACTION_DOWN 0
        // ACTION_UP 1
        // ACTION_MOVE 2
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:{
                    Collections.fill(SelfCardMoveLock,false);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE:{
                    int evX = Math.round(event.getX());
                    int evY = Math.round(event.getY());
                    int childCount = PokeCollections[SELF].getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        if(DetectCardSelected(i,evX,evY) && !SelfCardMoveLock.get(i)) {
                            HandleCardSelected(i);
                            SelfCardMoveLock.set(i,true);
                        }
                    }
                    break;
                }
            }
            return true;
        }
    });
    }


    // 计算牌的位置
    private int MeasureHorizontalCardHeight(int ContainerHeight)
    {
        return (int)(ContainerHeight / (1+r1));
    }
    private int MeasureHorizontalCardWidth(int CardHeight)
    {
        return (int)(CardHeight * CardWHRatio);
    }
    private int MeasureHorizontalMarginBottom(int CardHeight)
    {
        return (int)(CardHeight*r1);
    }
    private int MeasureHorizontalMarginStart(int CardWidth)
    {
        //return (int)(-(1-r1)*width);
        // 感觉只要给牌的一半就足够
        return (int)(-(0.5f)*CardWidth);
    }

    private int MeasureVerticalCardHeight(int VerticalContainerWidth) {
        return (int)(VerticalContainerWidth*(1f/r2));
    }
    private int MeasureVerticalCardWidth(int VerticalContainerWidth) {
        return (int)(VerticalContainerWidth);
    }
    private int MeasureVerticalCardMarginTop(int height) {
        //标准 return  -(1-r2/2)*TwoSideContainerHeight
        return (int)((-0.63f)*height);
    }
//    private int MeasureMarginTop(int height)
//    {
//        return (int)(-(1-r2/2)*height);
//    }

    private boolean DetectCardSelected(int position, int evX, int evY)
    {
        int end = SelfCardList.size() - 1;
        ImageView child = SelfCardImageList.get(position);
        int left = child.getLeft();
        int right = child.getRight();
        int top = child.getTop();
        int bottom = child.getBottom();
        if(position == end) {
            return (left <= evX && evX <= right && top <= evY &&  evY <= bottom);
        }
        else {
            int width = child.getWidth();
            int marginStart = width/2;
            right = right - marginStart;
            return (left <= evX && evX <= right && top <= evY &&  evY <= bottom);
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
        SelfCardStatus.set(position,!status);
    }


    private void LoadSelfCardsAsImageToContainer(List<Card> cards)
    {

        try {
            List<Integer> cardsResource = AppUtil.ConvertCardToDrawable(cards);

            for (int i = 0; i < cardsResource.size(); i++) {
                ImageView iv = CreatePokeImageView();

                Uri imgUri = AppUtil.ParseResourceIdToUri(cardsResource.get(i));

                Glide.with(mContext).load(imgUri).into(iv);
                int cardHeight = MeasureHorizontalCardHeight(SelfPokeContainerHeight);
                int cardWidth = MeasureHorizontalCardWidth(cardHeight);
                int marginStart = MeasureHorizontalMarginStart(cardWidth);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardWidth,cardHeight);
                if(i > 0)
                {
                    lp.setMarginStart(marginStart);
                }
                SelfCardImageList.add(iv);
                PokeCollections[SELF].addView(iv,lp);
            }

        } catch (NoSuchFieldException e) {
            Log.d("CardDesk","找不到牌信息指定的牌图片 NoSuchFieldException");

            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Log.d("CardDesk","找不到牌信息指定的牌图片 IllegalAccessException");
            e.printStackTrace();
        }
    }

    private void LoadOtherPlayerCardsAsImageToContainer(int count,int position)
    {
        //
        Uri bgUri = AppUtil.ParseResourceIdToUri(R.drawable.poke_back);
        switch (position) {
            case LEFT:
            case RIGHT:
            {
                for (int i = 0; i < count; i++) {
                    ImageView iv = CreatePokeImageView();
                    Glide.with(mContext).load(bgUri).into(iv);
                    int width = MeasureVerticalCardWidth(TwoSideContainerWidth);
                    int height = MeasureVerticalCardHeight(TwoSideContainerWidth);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
                    if(i > 0) {
                        lp.topMargin = MeasureVerticalCardMarginTop(height);
                    }
                    PokeCollections[position].addView(iv,lp);
                }
                break;
            }
            case TOP:{
                for (int i = 0; i < count; i++) {

                    ImageView iv = CreatePokeImageView();
                    Glide.with(mContext).load(bgUri).into(iv);

                    int height = MeasureHorizontalCardHeight(TopContainerHeight);
                    int width = MeasureHorizontalCardWidth(height);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
                    if(i > 0) {
                        lp.leftMargin = MeasureHorizontalMarginStart(width);
                    }

                    PokeCollections[TOP].addView(iv,lp);
                }
                break;
            }
        }
    }

    private ImageView CreatePokeImageView()
    {
        ImageView iv = new ImageView(mContext);
        iv.setBackground(mContext.getDrawable(R.drawable.card_img_bored));
        iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return iv;
    }

    private void PutCardToShowContainer(int position,List<Card> cards)
    {
        try {
            List<Integer> drawRes = AppUtil.ConvertCardToDrawable(cards);

            switch (position) {
                case TOP:
                case SELF:
                {
                    for (int i = 0; i < cards.size(); i++) {
                        ImageView iv = CreatePokeImageView();
                        Glide.with(mContext).load(drawRes.get(i)).into(iv);
                        int height = MeasureHorizontalCardHeight(HorizontalShowContainerHeight);
                        int width = MeasureHorizontalCardWidth(height);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,height);
                        if(i > 1) {
                            lp.leftMargin = MeasureHorizontalMarginStart(width);
                        }
                        ShowPokeCollections[position].addView(iv);
                    }

                    break;
                }

                case LEFT:
                case RIGHT:
                {
                    break;
                }
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void SelectCard(Player self)
    {
        // 此函数用于我方出牌, 牌面选择完成之后内部调用有参的SelectCard显示出牌信息

    }
    public void SelectCard(int position,List<Card> cards)
    {
        // 此函数用于显示其他玩家出牌
        // 把这些牌Set到对应的ShowPokeCollections[position]上
        // Remove掉PokeCollections相应数量的

    }

    public List<Card>[] GetAllHadShownCards()
    {
        return AllHadShownCard;
    }

    public void NewTurn()
    {
        // 新的一轮，把场上出的全部牌给清除掉
        for (int i = 0; i < 4; i++) {
            AllHadShownCard[i].clear();
            ShowPokeCollections[i].removeAllViews();
        }
    }

    public void NewCompetition(Player self)
    {
        SelfCardImageList = new ArrayList<>();
        SelfCardStatus = new ArrayList<>(13);
        SelfCardMoveLock = new ArrayList<>(13);

        AllHadShownCard = new List[4];
        for (int i = 0; i < 4; i++) {
            AllHadShownCard[i] = new ArrayList<>();
        }

        SelfCards = self.GetHandCards();
        LoadSelfCardsAsImageToContainer(SelfCards);

        LoadOtherPlayerCardsAsImageToContainer(13,RIGHT);
        LoadOtherPlayerCardsAsImageToContainer(13,TOP);
        LoadOtherPlayerCardsAsImageToContainer(13,LEFT);
    }
}
