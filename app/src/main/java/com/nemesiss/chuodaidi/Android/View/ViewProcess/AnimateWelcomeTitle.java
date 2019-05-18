package com.nemesiss.chuodaidi.Android.View.ViewProcess;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public class AnimateWelcomeTitle {

    public static void Animate(int delay, View container,int begin,int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(begin,end);
        animator.setStartDelay(delay);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams lp = container.getLayoutParams();
                lp.height = val;
                container.setLayoutParams(lp);
            }
        });
        animator.start();
    }
}
