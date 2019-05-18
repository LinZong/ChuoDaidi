package com.nemesiss.chuodaidi.Android.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import com.nemesiss.chuodaidi.Android.Adapter.RobotSelectionAdapter;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Android.View.GameDialog;
import com.nemesiss.chuodaidi.Android.View.GameDialogNew;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.R;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PlayerTypeFragment extends BaseGameFragment
{
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.play_type_func_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
       return view;
    }

    @Override
    public void onDestroy()
    {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick({R.id.Welcome_PlayWithRobot})
    public void PlayWithRobots()
    {
        AppUtil.LoadFragmentToActivity(AttachedActivity,R.id.WelcomeFuncFragmentContainer,new RobotCharacterSelectFragment());
        AttachedActivity.ExpandSelectionFragmentArea();
    }


    @OnClick({R.id.Welcome_PlayWithRemotePlayer})
    public void PlayWithRemotePlayer()
    {
        Snackbar.make(view, "This mode is currently being developed!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case GameDialog.REQUEST_CUSTOM_DIALOG_PERMISSION:
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (Settings.canDrawOverlays(getContext()) && PendingDialogBuilder != null)
                    {
                        PendingDialogBuilder.Build();
                    } else
                    {
                        Toast.makeText(getContext(), "You deny custom dialog permission.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}
