package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.Android.Adapter.RobotSelectionAdapter;
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ChuoDaidiActivity {



    private RobotSelectionAdapter rightRobot;
    private RobotSelectionAdapter leftRobot;
    private RobotSelectionAdapter topRobot;

    private BasePlayerInformation selectedFirst;
    private BasePlayerInformation selectedSecond;
    private BasePlayerInformation selectedThird;


    private BlackSpinner rightRobotSpinner;
    private BlackSpinner leftRobotSpinner;
    private BlackSpinner topRobotSpinner;

    private List<BasePlayerInformation> players;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        players = Arrays.asList(RobotCharactersExport.AllRobotCharacters);
        InitFakeItems();
    }

    private void InitFakeItems() {
        rightRobot = new RobotSelectionAdapter(players);
        leftRobot = new RobotSelectionAdapter(players);
        topRobot = new RobotSelectionAdapter(players);

        rightRobotSpinner = findViewById(R.id.Select_RightRobot);
        leftRobotSpinner = findViewById(R.id.Select_LeftRobot);
        topRobotSpinner = findViewById(R.id.Select_TopRobot);

        rightRobotSpinner.setAdapter(rightRobot);
        leftRobotSpinner.setAdapter(leftRobot);
        topRobotSpinner.setAdapter(topRobot);
    }
}
