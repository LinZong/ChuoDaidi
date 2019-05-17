package com.nemesiss.chuodaidi.Android.Activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nemesiss.chuodaidi.Android.Adapter.FakeSpinnerAdapter;
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Android.View.RoundImageView;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.RoundImageTransform;
import com.nemesiss.chuodaidi.R;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ChuoDaidiActivity {


    @BindView(R.id.FakeBackSpinner)
    BlackSpinner spinner;

    private FakeSpinnerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitFakeItems();
    }

    private void InitFakeItems()
    {
        List<String> xx = new ArrayList<>();
        xx.add("123");
        xx.add("456");
        xx.add("789");
        xx.add("333");
        xx.add("666");
        xx.add("777");
        adapter = new FakeSpinnerAdapter(xx);
        spinner.setAdapter(adapter);
        adapter.setOnChildItemClickedListener(new FakeSpinnerAdapter.OnChildItemClickedListener() {
            @Override
            public void handle(int position) {
                Log.d("MainActivity","我被点击了! " + xx.get(position));
            }
        });
    }
}
