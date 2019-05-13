package com.nemesiss.chuodaidi.Android.Fragment;

import android.support.v4.app.Fragment;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;

public class BaseWelcomeFragment extends Fragment
{
    protected ChuoDaidiActivity WelcomeActivity;
    public BaseWelcomeFragment()
    {
        WelcomeActivity = (ChuoDaidiActivity) getActivity();

    }
}
