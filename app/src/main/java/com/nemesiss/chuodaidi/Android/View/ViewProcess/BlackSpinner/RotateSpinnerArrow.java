package com.nemesiss.chuodaidi.Android.View.ViewProcess.BlackSpinner;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;

public class RotateSpinnerArrow
{
    public static ObjectAnimator Rotate(Drawable drawable,boolean FinalStateArrowUp,int duration)
    {
        // ensure the init state of drawable is an arrow with down position.

        int begin = FinalStateArrowUp ? 0 : 10000;
        int end = FinalStateArrowUp ? 10000 : 0;
        ObjectAnimator arrowAnimator = ObjectAnimator.ofInt(drawable,"level",begin,end);
        arrowAnimator.setDuration(duration);
        return arrowAnimator;

    }
    public static ObjectAnimator Rotate(Drawable drawable, boolean FinalStateArrowUp, int duration,
                              ObjectAnimator.AnimatorUpdateListener AnimationUpdateListener)
    {
        int begin = FinalStateArrowUp ? 0 : 10000;
        int end = FinalStateArrowUp ? 10000 : 0;
        ObjectAnimator arrowAnimator = ObjectAnimator.ofInt(drawable,"level",begin,end);
        arrowAnimator.setDuration(duration);
        arrowAnimator.addUpdateListener(AnimationUpdateListener);
        arrowAnimator.start();

        return arrowAnimator;
    }
}
