package com.nemesiss.chuodaidi.Android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.nemesiss.chuodaidi.Android.Adapter.BlackSpinnerAdapter;
import com.nemesiss.chuodaidi.Android.Adapter.RobotSelectionAdapter;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.RobotPlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;
import com.nemesiss.chuodaidi.R;

import java.util.Arrays;
import java.util.List;

public class RobotCharacterSelectFragment extends BaseGameFragment {

    private Unbinder unbinder;
    private List<BasePlayerInformation> robotPlayers;


    private RobotSelectionAdapter rightRobot;
    private RobotSelectionAdapter leftRobot;
    private RobotSelectionAdapter topRobot;

    private BasePlayerInformation selectedRight;
    private BasePlayerInformation selectedLeft;
    private BasePlayerInformation selectedTop;

    private BlackSpinner rightRobotSpinner;
    private BlackSpinner leftRobotSpinner;
    private BlackSpinner topRobotSpinner;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.select_three_robots_frag,container,false);
        unbinder = ButterKnife.bind(this,view);
        robotPlayers = (List) Characters.GetAllRobots();

        InitRobotsPlayerSelection();
        LoadRobotsPlayerSelection();
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void InitRobotsPlayerSelection()
    {
        rightRobot = new RobotSelectionAdapter(robotPlayers);
        leftRobot = new RobotSelectionAdapter(robotPlayers);
        topRobot = new RobotSelectionAdapter(robotPlayers);
    }

    private void LoadRobotsPlayerSelection()
    {

        rightRobotSpinner = view.findViewById(R.id.Select_RightRobot);
        leftRobotSpinner = view.findViewById(R.id.Select_LeftRobot);
        topRobotSpinner = view.findViewById(R.id.Select_TopRobot);

        selectedRight = robotPlayers.get(0);
        selectedLeft = robotPlayers.get(0);
        selectedTop = robotPlayers.get(0);

        rightRobot.setOnChildItemClickedListener(position -> selectedRight = robotPlayers.get(position));
        leftRobot.setOnChildItemClickedListener(position -> selectedLeft = robotPlayers.get(position));
        topRobot.setOnChildItemClickedListener(position -> selectedTop = robotPlayers.get(position));

        rightRobotSpinner.setAdapter(rightRobot);
        leftRobotSpinner.setAdapter(leftRobot);
        topRobotSpinner.setAdapter(topRobot);

    }

    @OnClick({R.id.ConfirmEnterGame})
    public void EnterGame(View v)
    {
        UserInformation player = Characters.GetPlayer();
        // 严格按照右，上，左的顺序传递数据
        Intent it = GameHelper.BuildRobotsPlayIntent(AttachedActivity,player,selectedRight,selectedTop,selectedLeft);

        AttachedActivity.startActivity(it);
    }

    @OnClick({R.id.BackToTypeSelection})
    public void BackToTypeSelection(View v)
    {
        if (getFragmentManager() != null)
        {
            getFragmentManager().popBackStack();
        }
    }

}
