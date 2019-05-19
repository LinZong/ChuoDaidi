package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.Android.View.GameDialogNew;
import com.nemesiss.chuodaidi.Game.Component.Card.CardHelper;
import com.nemesiss.chuodaidi.Game.Component.Card.Score.ScoreCalculator;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Controller.HostRoundController;
import com.nemesiss.chuodaidi.Game.Component.Player.LocalPlayer;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotPlayer;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.RobotPlayerInformation;
import com.nemesiss.chuodaidi.R;

import java.util.ArrayList;
import java.util.List;

public class InGameActivity extends ChuoDaidiActivity
{

    private BaseRoundController roundController;

    private List<Player> FakeRobots;
    private LocalPlayer self;

    @BindView(R.id.InGameCardDesk)
    CardDesk InGameCardDesk;

    private BasePlayerInformation[] InGamePlayers;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card_desk);
        ButterKnife.bind(this);
        int controllerType = getIntent().getIntExtra(BaseRoundController.CONTROLLER_TYPE, -1);
        int gameType = getIntent().getIntExtra(BaseRoundController.GAME_TYPE, -1);

        InGamePlayers = (BasePlayerInformation[]) getIntent().getSerializableExtra(BaseRoundController.PLAYER_INFO);


        switch (controllerType)
        {
            case BaseRoundController.HOST_CONTROLLER:
            {

                switch (gameType)
                {
                    case BaseRoundController.ROBOTS_GAME:
                    {
                        PrepareRobotsCompetition();
                        break;
                    }
                    case BaseRoundController.REMOTE_PLAYER_GAME:
                    {
                        PrepareRemotePlayersCompetition();
                        break;
                    }
                }
                break;

            }
            case BaseRoundController.CLIENT_CONTROLLER:
            {
                //TODO 还没有写客户机的逻辑

                break;
            }
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        roundController.ShouldExit();
        Log.d("InGameActivity","轮次控制器退出!");
    }

    private void PrepareRobotsCompetition()
    {


        // 启动轮次控制器
        roundController = new HostRoundController(InGameActivity.this, InGameCardDesk);
        InGameCardDesk.SetRoundController(roundController);


        FakeRobots = new ArrayList<>();
        FakeRobots.add(new RobotPlayer(roundController, CardDesk.RIGHT, InGameCardDesk,InGamePlayers[CardDesk.RIGHT]));
        FakeRobots.add(new RobotPlayer(roundController, CardDesk.TOP, InGameCardDesk,InGamePlayers[CardDesk.TOP]));
        FakeRobots.add(new RobotPlayer(roundController, CardDesk.LEFT, InGameCardDesk,InGamePlayers[CardDesk.LEFT]));

        self = new LocalPlayer(roundController, 0, InGameCardDesk,InGamePlayers[CardDesk.SELF]);

        InGameCardDesk.DoAfterCardDeskLoaded(() -> {
            runOnUiThread(() -> {
                roundController.NewCompetition(FakeRobots,self);
            });
        });
    }

    private void PrepareRemotePlayersCompetition()
    {

    }


    @Override
    public void onBackPressed()
    {
        new GameDialogNew.Builder()
                .with(this)
                .setTitle("中途离开游戏")
                .setText("注意!中途退出游戏将会受到严厉的惩罚!确定要中途退出吗?")
                .setPositiveButton("确定",(v) -> {
                    ScoreCalculator.Penalty();
                    finish();
                })
                .setNegativeButton("取消",null)
                .Build()
                .Show();
    }
}
