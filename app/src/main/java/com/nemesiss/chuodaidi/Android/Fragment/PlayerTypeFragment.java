package com.nemesiss.chuodaidi.Android.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.nemesiss.chuodaidi.R;

public class PlayerTypeFragment extends BaseWelcomeFragment
{
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.play_type_func_frag,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Welcome_PlayWithRobot})
    public void PlayWithRobots()
    {

    }

    @OnClick({R.id.Welcome_PlayWithRemotePlayer})
    public void PlayWithRemotePlayer()
    {
        Snackbar.make(view,"This mode is currently being developed!",Snackbar.LENGTH_SHORT).show();

    }
}
