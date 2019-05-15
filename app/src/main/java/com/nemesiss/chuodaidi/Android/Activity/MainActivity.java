package com.nemesiss.chuodaidi.Android.Activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nemesiss.chuodaidi.Android.View.RoundImageView;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.RoundImageTransform;
import com.nemesiss.chuodaidi.R;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ChuoDaidiActivity {

    @BindView(R.id.TestClipImageCorner)
    ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Glide.with(MainActivity.this)
//                .load(R.drawable.poke_card_2_diamond)
//                .transform(new RoundImageTransform(MainActivity.this,10))
//                .dontAnimate()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(iv);



    }
}
