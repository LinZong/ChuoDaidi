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
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Android.View.GameDialog;
import com.nemesiss.chuodaidi.Android.View.GameDialogNew;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.R;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PlayerTypeFragment extends BaseGameFragment
{
    private Unbinder unbinder;
    private RobotSelectionAdapter rightRobot;
    private RobotSelectionAdapter leftRobot;
    private RobotSelectionAdapter topRobot;

    private BasePlayerInformation selectedFirst;
    private BasePlayerInformation selectedSecond;
    private BasePlayerInformation selectedThird;

    private BlackSpinner rightRobotSpinner;
    private BlackSpinner leftRobotSpinner;
    private BlackSpinner topRobotSpinner;

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
        View view = LoadRobotsPlayerSelection();

        GameDialogNew dialog = new GameDialogNew.Builder()
                .with(this)
                .setTitle("选择与之一战的机器人")
                .setPositiveButton("确定", (v) -> {

                    // inject robots info

                    Intent intent = GameHelper.BuildRobotsPlayIntent(AttachedActivity);
                    AttachedActivity.runOnUiThread(() -> {
                        startActivity(intent);
                    });
                })
                .setNegativeButton("取消", null)
                .Build(view);

        dialog.Show();
    }


    private View LoadRobotsPlayerSelection()
    {
        rightRobot = new RobotSelectionAdapter(Arrays.asList(RobotCharactersExport.AllRobotCharacters));
        leftRobot = new RobotSelectionAdapter(Arrays.asList(RobotCharactersExport.AllRobotCharacters));
        topRobot = new RobotSelectionAdapter(Arrays.asList(RobotCharactersExport.AllRobotCharacters));

        View innerSelection = View.inflate(getContext(), R.layout.select_three_robots, null);

        rightRobotSpinner = innerSelection.findViewById(R.id.Select_RightRobot);
        leftRobotSpinner = innerSelection.findViewById(R.id.Select_Left_Robot);
        topRobotSpinner = innerSelection.findViewById(R.id.Select_TopRobot);

        rightRobotSpinner.setAdapter(rightRobot);
        leftRobotSpinner.setAdapter(leftRobot);
        topRobotSpinner.setAdapter(topRobot);

        return innerSelection;
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
