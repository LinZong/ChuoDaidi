package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.Android.Fragment.WelcomeFragment;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.AnimateWelcomeTitle;
import com.nemesiss.chuodaidi.R;

public class WelcomeActivity extends ChuoDaidiActivity
{

    @BindView(R.id.WelcomeFuncFragmentContainer)
    FrameLayout container;

    public BaseGameFragment[] fragments;
    public FragmentManager fm;
    private int CurrentFragmentTag = -1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);


        fragments = new BaseGameFragment[6];
        fm = getSupportFragmentManager();

        AppUtil.LoadFragmentToActivity(WelcomeActivity.this,R.id.WelcomeFuncFragmentContainer,new WelcomeFragment());
    }

    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }

    public void ExpandSelectionFragmentArea()
    {
        int begin = AppUtil.Dp2Px(200);
        int end = AppUtil.Dp2Px(270);
        AnimateWelcomeTitle.Animate(400,container,begin,end);
    }

    public void ShrinkSelectionFragmentArea()
    {
        int begin = AppUtil.Dp2Px(270);
        int end = AppUtil.Dp2Px(200);
        AnimateWelcomeTitle.Animate(400,container,begin,end);
    }
}
