package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Helper.CardComparator;
import com.nemesiss.chuodaidi.Game.Component.Helper.CardHelper;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Controller.HostRoundController;
import com.nemesiss.chuodaidi.Game.Component.Player.LocalPlayer;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotPlayer;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestCardDeskActivity extends ChuoDaidiActivity {

    private BaseRoundController roundController;

    private List<Player> FakeRobots;
    private LocalPlayer self;

    @BindView(R.id.InGameCardDesk)
    CardDesk InGameCardDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card_desk);
        ButterKnife.bind(this);

        roundController = new HostRoundController(TestCardDeskActivity.this,InGameCardDesk);
        FakeRobots = new ArrayList<>();
        FakeRobots.add(new RobotPlayer(roundController,1,InGameCardDesk));
        FakeRobots.add(new RobotPlayer(roundController,2,InGameCardDesk));
        FakeRobots.add(new RobotPlayer(roundController,3,InGameCardDesk));

        self = new LocalPlayer(roundController,0,InGameCardDesk);

        List<Card> shuffled = CardHelper.GetShuffledCard();

        List<Card> c1 = shuffled.subList(0,13);
        List<Card> c2 = shuffled.subList(13,26);
        List<Card> c3 = shuffled.subList(26,39);
        List<Card> c4 = shuffled.subList(39,52);
        Collections.sort(c1, new CardComparator());
        Collections.sort(c2, new CardComparator());
        Collections.sort(c3, new CardComparator());
        Collections.sort(c4, new CardComparator());


        FakeRobots.get(0).InitSetHandCards(c1);
        FakeRobots.get(1).InitSetHandCards(c2);
        FakeRobots.get(2).InitSetHandCards(c3);
        self.InitSetHandCards(c4);



    }

    @Override
    protected void onResume() {
        super.onResume();
        roundController.NewCompetition(FakeRobots,self);
    }
}
