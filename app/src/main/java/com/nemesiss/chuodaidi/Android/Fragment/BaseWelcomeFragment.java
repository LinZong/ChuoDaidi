package com.nemesiss.chuodaidi.Android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import com.github.ikidou.fragmentBackHandler.FragmentBackHandler;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.Android.Activity.WelcomeActivity;

public class BaseWelcomeFragment extends Fragment implements FragmentBackHandler
{
    protected com.nemesiss.chuodaidi.Android.Activity.WelcomeActivity welcomeActivity;

    public static final int WELCOME_FRAG_TAG = 1;
    public static final int PLAY_TYPE_FRAG_TAG = 2;
    public static final int REMOTE_PLAY_ROLE_TAG = 3;
    public static final int REMOTE_PLAY_INNER_ROOM_FLAG = 4;
    public static final int REMOTE_PLAY_ROOM_LIST_FLAG = 5;

    protected View view;
    private FragmentManager fm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        welcomeActivity = (WelcomeActivity) context;
    }

    @Override
    public boolean onBackPressed() {
        int fragCount = fm.getBackStackEntryCount();
        if(fragCount == 1)
        {
            fm.popBackStack();
            welcomeActivity.finish();
            return true;
        }
        return false;
    }
}
