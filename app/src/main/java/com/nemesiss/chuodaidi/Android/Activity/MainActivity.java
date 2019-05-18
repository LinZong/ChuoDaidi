package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
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


    @BindView(R.id.FakeBackSpinner)
    BlackSpinner spinner;

    private RobotSelectionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        InitFakeItems();
    }

    private void InitFakeItems()
    {

        List<BasePlayerInformation> xx = Arrays.asList(RobotCharactersExport.AllRobotCharacters);
        adapter = new RobotSelectionAdapter(xx);
        spinner.setAdapter(adapter);

    }
}
