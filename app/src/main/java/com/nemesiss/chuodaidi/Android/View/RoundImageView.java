package com.nemesiss.chuodaidi.Android.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import com.nemesiss.chuodaidi.R;

public class RoundImageView extends android.support.v7.widget.AppCompatImageView {

    float width,height;
    float radius;

    private static Path path;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

        TypedArray ta = context.obtainStyledAttributes(R.styleable.RoundImageView);
        radius = ta.getFloat(R.styleable.RoundImageView_corner_radius,12f);
        ta.recycle();

    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private Path getPath()
    {
        if(path== null)
        {
            path = new Path();
            path.moveTo(radius, 0);
            path.lineTo(width - radius, 0);
            path.quadTo(width, 0, width, radius);
            path.lineTo(width, height - radius);
            path.quadTo(width, height, width - radius, height);
            path.lineTo(radius, height);
            path.quadTo(0, height, 0, height - radius);
            path.lineTo(0, radius);
            path.quadTo(0, 0, radius, 0);
        }
        return path;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > 12 && height > 12) {
            Path path = getPath();
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}