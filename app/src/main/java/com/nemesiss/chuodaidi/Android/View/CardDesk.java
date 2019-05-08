package com.nemesiss.chuodaidi.Android.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.util.Collections;
import java.util.List;

public class CardDesk extends ConstraintLayout {

    // View控制相关变量
    private float CardWHRatio;
    private float r1;
    private float r2;
    private int SelfPokeContainerHeight;
    private int SelfPokeContainerWidth;

    // 牌面显示控制变量
    private List<Boolean> SelfCardStatus;
    private List<Boolean> SelfCardMoveLock;
    private List<ImageView> SelfCardImageList;
    private List<Card> SelfCardList;
    private List<Card>[] AllHadShownCard;



    private static final int SELF = 0,RIGHT = 1,TOP = 2,LEFT = 3;
    private LinearLayout[] PokeCollections;// 0 1 2 3 Self, Right, Top, Left
    private LinearLayout[] ShowPokeCollections;
    public CardDesk(Context context) {
        super(context);
        Init();

    }

    public CardDesk(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init();
    }

    public CardDesk(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init();
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
    private int MeasureCardHeight()
    {
        return (int)(SelfPokeContainerHeight / (1+r1));
    }
    private int MeasureCardWidth(int height)
    {
        return (int)(height * CardWHRatio);
    }
    private int MeasureMarginBottom(int height)
    {
        return (int)(height*r1);
    }
    private int MeasureMarginStart(int width)
    {
        //return (int)(-(1-r1)*width);
        // 感觉只要给牌的一半就足够
        return (int)(-(0.5f)*width);
    }
    private int MeasureMarginTop(int height)
    {
        return (int)(-(1-r2/2)*height);
    }

    private boolean DetectCardSelected(int position, int evX, int evY)
    {
        int end = SelfCardImageList.size() - 1;
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
        lp.bottomMargin = !status ? MeasureMarginBottom(height) : 0;

        iv.setLayoutParams(lp);
        SelfCardStatus.set(position,!status);
    }

    public void SelectCard()
    {
        // 此函数用于我方出牌
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
}
