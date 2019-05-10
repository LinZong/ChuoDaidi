package com.nemesiss.chuodaidi.Android.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Player.LocalPlayer;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.util.ArrayList;
import java.util.List;

public class TestCardDeskActivity extends ChuoDaidiActivity {

    @BindView(R.id.InGameCardDesk)
    CardDesk cardDesk;

    private List<Card> cardList = new ArrayList<>();
    private Player self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_card_desk);
        ButterKnife.bind(this);


        cardList.add(new Card("2", Card.Pattern.Club));
        cardList.add(new Card("3", Card.Pattern.Diamond));
        cardList.add(new Card("J", Card.Pattern.Spade));
        cardList.add(new Card("Q", Card.Pattern.Heart));
        cardList.add(new Card("2", Card.Pattern.Club));
        cardList.add(new Card("2", Card.Pattern.Club));
        cardList.add(new Card("3", Card.Pattern.Diamond));
        cardList.add(new Card("J", Card.Pattern.Spade));
        cardList.add(new Card("Q", Card.Pattern.Heart));
        cardList.add(new Card("2", Card.Pattern.Club));
        cardList.add(new Card("J", Card.Pattern.Spade));
        cardList.add(new Card("Q", Card.Pattern.Heart));
        cardList.add(new Card("2", Card.Pattern.Club));
        self = new LocalPlayer();
        self.InitSetHandCards(cardList);

        cardDesk.NewCompetition(self);

    }

    @OnClick({R.id.PickupPoke})
    public void PickPoke(View v)
    {
//        cardDesk.SelectCard(1,cardList);
//        cardDesk.SelectCard(2,cardList);
//        cardDesk.SelectCard(3,cardList);
    }

    @OnClick({R.id.EnterNewTurn})
    public void NT(View v)
    {
        cardDesk.NewTurn();
    }
}
