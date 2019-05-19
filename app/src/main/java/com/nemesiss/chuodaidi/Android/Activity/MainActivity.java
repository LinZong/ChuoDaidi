package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nemesiss.chuodaidi.Android.Adapter.RobotSelectionAdapter;
import com.nemesiss.chuodaidi.Android.View.BlackSpinner;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.RobotPlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;
import com.nemesiss.chuodaidi.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends ChuoDaidiActivity {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.QueryAll})
    public void QueryAll(View v)
    {
        List<RobotPlayerInformation> robots =  Characters.GetAllRobots();
        for (RobotPlayerInformation robot : robots) {
            Log.d("MainActivity",robot.getNickName());
        }
    }

    @OnClick({R.id.QueryPlayer})
    public void QueryPlayer(View vi)
    {
        UserInformation player = Characters.GetPlayer();
        Log.d("MainActivity",player.getNickName());
    }

}
