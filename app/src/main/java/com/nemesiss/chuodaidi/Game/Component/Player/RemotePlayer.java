package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class RemotePlayer implements Player {

    private List<Card> handCards;
    private BaseRoundController roundController;
    private int PlayerNumber;
    private CardDesk GameCardDesk;

    public RemotePlayer(BaseRoundController rc, int MyNumber, CardDesk cardDesk)
    {
        roundController = rc;
        SetPlayerNumber(MyNumber);
        GameCardDesk = cardDesk;
    }

    @Override
    public int GetPlayerNumber() {
        return 0;
    }

    @Override
    public void SetPlayerNumber(int num) {

    }

    @Override
    public List<Card> GetHandCards() {
        return null;
    }

    @Override
    public void InitSetHandCards(@NonNull List<Card> InitHandCards) {

    }


    @Override
    public void ShowCard(List<Integer> CardIndex) {

    }

    @Override
    public void NotifyTakeTurn() {

    }

    @Override
    public void HandleTakeTurn() {

    }
}
