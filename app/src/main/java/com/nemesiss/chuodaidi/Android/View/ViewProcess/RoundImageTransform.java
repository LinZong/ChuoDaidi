package com.nemesiss.chuodaidi.Android.View.ViewProcess;

import android.content.Context;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class RoundImageTransform extends BitmapTransformation {
    public RoundImageTransform(Context context) {
        super(context);
    }

    public RoundImageTransform(BitmapPool bitmapPool) {
        super(bitmapPool);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return RoundRectCropper(pool,toTransform);
    }

    private static Bitmap RoundRectCropper(BitmapPool pool,Bitmap source)
    {
        if(source == null) return null;
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap result = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        canvas.drawRoundRect(new RectF(0,0,width,height), 20f, 20f,paint);
        canvas.save();
        return result;
    }


    @Override
    public String getId() {
        return getClass().getName();
    }
}
