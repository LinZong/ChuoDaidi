package com.nemesiss.chuodaidi.Android.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.R;

public class WelcomeFragment extends BaseGameFragment
{

    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.welcome_func_frag,container,false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        unbinder = ButterKnife.bind(this,view);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.Welcome_NewGame})
    public void NewGame(View v)
    {
        AppUtil.LoadFragmentToActivity(AttachedActivity,R.id.WelcomeFuncFragmentContainer,new PlayerTypeFragment());
    }

    @OnClick({R.id.Welcome_Exit})
    public void ExitGame(View v)
    {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
        AttachedActivity.finish();
    }

}
