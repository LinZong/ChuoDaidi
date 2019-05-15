package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Helper.CardHelper;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Controller.HostRoundController;
import com.nemesiss.chuodaidi.Game.Component.Player.LocalPlayer;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotPlayer;
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card_desk);
        ButterKnife.bind(this);
        int controllerType = getIntent().getIntExtra(BaseRoundController.CONTROLLER_TYPE, -1);
        int gameType = getIntent().getIntExtra(BaseRoundController.GAME_TYPE, -1);
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

        List[] cards = CardHelper.GetShuffledCardGroups();
        // 启动轮次控制器

        roundController = new HostRoundController(InGameActivity.this, InGameCardDesk);

        FakeRobots = new ArrayList<>();
        FakeRobots.add(new RobotPlayer(roundController, 1, InGameCardDesk));
        FakeRobots.add(new RobotPlayer(roundController, 2, InGameCardDesk));
        FakeRobots.add(new RobotPlayer(roundController, 3, InGameCardDesk));

        self = new LocalPlayer(roundController, 0, InGameCardDesk);

        FakeRobots.get(0).InitSetHandCards(cards[CardDesk.RIGHT]);
        FakeRobots.get(1).InitSetHandCards(cards[CardDesk.TOP]);
        FakeRobots.get(2).InitSetHandCards(cards[CardDesk.LEFT]);
        self.InitSetHandCards(cards[CardDesk.SELF]);

        InGameCardDesk.DoAfterCardDeskLoaded(() -> {
            runOnUiThread(() -> {
                roundController.NewCompetition(FakeRobots,self);
            });
        });

    }

    private void PrepareRemotePlayersCompetition()
    {

    }
}
