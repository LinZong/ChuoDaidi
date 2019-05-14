package com.nemesiss.chuodaidi.Android.View.ViewProcess;

import android.content.Context;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class RoundImageTransform extends BitmapTransformation {
    private float Radius = -1,StrokeWidth = 0;

    public RoundImageTransform(Context context) {
        super(context);
    }

    public RoundImageTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    public RoundImageTransform(Context context,float radius,float strokeWidth) {
        super(context);
        Radius = radius;
        StrokeWidth = strokeWidth;
    }

    public RoundImageTransform(BitmapPool bitmapPool,float radius,float strokeWidth) {
        super(bitmapPool);
        Radius = radius;
        StrokeWidth = strokeWidth;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return RoundRectCropper(pool,toTransform);
    }

    private Bitmap RoundRectCropper(BitmapPool pool,Bitmap source)
    {
        if(source == null) return null;
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        if(Radius!=-1) {
            Paint edgePaint = new Paint();
            edgePaint.setStyle(Paint.Style.FILL);
            edgePaint.setColor(Color.GRAY);
            edgePaint.setAntiAlias(true);
            canvas.drawRoundRect(new RectF(0,0,width,height), Radius, Radius,edgePaint);
        }

        canvas.drawRoundRect(new RectF(StrokeWidth,StrokeWidth,width-StrokeWidth,height-StrokeWidth), Radius, Radius,paint);
        canvas.save();
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
