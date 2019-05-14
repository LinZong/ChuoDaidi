package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.Android.Fragment.WelcomeFragment;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.R;

public class WelcomeActivity extends ChuoDaidiActivity
{

    public BaseGameFragment[] fragments;
    public FragmentManager fm;
    private int CurrentFragmentTag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
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
}
