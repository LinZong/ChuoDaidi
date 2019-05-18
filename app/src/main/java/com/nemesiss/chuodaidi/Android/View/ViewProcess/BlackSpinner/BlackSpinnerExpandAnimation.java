package com.nemesiss.chuodaidi.Android.View.ViewProcess.BlackSpinner;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

public class BlackSpinnerExpandAnimation
{
    public static ValueAnimator Expand(RecyclerView self,int currentHeight,int targetHeight,int duration)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(currentHeight,targetHeight);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) self.getLayoutParams();
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                self.setLayoutParams(lp);
            }
        });
        return valueAnimator;
    }


    public static void Go(ObjectAnimator objectAnimator,ValueAnimator valueAnimator)
    {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimator,valueAnimator);
    }
}
