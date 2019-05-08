package com.nemesiss.chuodaidi.Activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.nemesiss.chuodaidi.R;
import com.nemesiss.chuodaidi.Utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ChuoDaidiActivity {

    private LinearLayout SelfPokeContainer;
    private LinearLayout SelfShowPokeContainer;
    private List<ImageView> imageViewList;
    private boolean[] CardStatus;

    // Card Container data:

    private int CardOriginalWidth = 250;
    private int CardOriginalHeight = 400;
    private float CardWHRatio = (float)CardOriginalWidth / CardOriginalHeight;
    private float r1 = 43.5f/250;
    private float r2 = 53.7f/400;

    private int HorizontalPokeContainerHeight;
    private int HorizontalPokeContainerWidth;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardStatus = new boolean[13];
        imageViewList = new ArrayList<>();
        SelfPokeContainer = findViewById(R.id.SelfPokeCollection);
        SelfShowPokeContainer = findViewById(R.id.SelfShowPokeCollection);

        SelfPokeContainer.post(() -> {
            HorizontalPokeContainerHeight = SelfPokeContainer.getHeight();
            HorizontalPokeContainerWidth = SelfPokeContainer.getWidth();
            LoadCards();
        });

        SelfPokeContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    boolean[] IsProcessed = new boolean[13];

                    int evX = Math.round(event.getX());
                    int evY = Math.round(event.getY());


                    int childCount = SelfPokeContainer.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = SelfPokeContainer.getChildAt(i);
                        int left = child.getLeft();
                        int right = child.getRight();
                        int top = child.getTop();
                        int bottom = child.getBottom();
                        if(left <= evX && evX <= right && top <= evY &&  evY <= bottom)
                        {
                            // touch到了
                            Log.d("MainActivity","触摸到"+i+"牌");
                            if(!IsProcessed[i]){
                                HandleCardSelected(i);
                                IsProcessed[i] = true;
                            }
                        }
                    }
                }
                return true ;
            }
        });
    }

    private void LoadCards()
    {
        Uri CardImgUri = AppUtil.ParseResourceIdToUri(R.drawable.poke_card_1);
        for (int i = 0; i < 13; i++) {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setBackground(getDrawable(R.drawable.card_img_bored));
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(MainActivity.this).load(CardImgUri).into(iv);
            int cardHeight = MeasureCardHeight();
            int cardWidth = MeasureCardWidth(cardHeight);
            int marginStart = MeasureMarginStart(cardWidth);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardWidth,cardHeight);
            if(i > 0)
            {
                lp.setMarginStart(-cardWidth/2);
            }
            imageViewList.add(iv);
            SelfPokeContainer.addView(iv,lp);
        }
    }

    private int MeasureCardHeight()
    {
        return (int)(HorizontalPokeContainerHeight / (1+r1));
    }
    private int MeasureCardWidth(int height)
    {
        return (int)(height * CardWHRatio);
    }
    private int MeasureMarginBottom(int height)
    {
        return (int)(-height*r1);
    }
    private int MeasureMarginStart(int width)
    {
        return (int)(-(1-r1)*width);
    }
    private int MeasureMarginTop(int height)
    {
        return (int)(-(1-r2/2)*height);
    }

    private void HandleCardSelected(int position)
    {
        ImageView iv = imageViewList.get(position);
        boolean status = CardStatus[position];
        int height = iv.getHeight();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)iv.getLayoutParams();
        lp.bottomMargin = !status ? MeasureMarginBottom(height) : 0;
        iv.setLayoutParams(lp);
        CardStatus[position] = !status;
    }
}
