package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import android.util.Log;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class RobotPlayer implements Player {

    private List<Card> handCards;
    private BaseRoundController roundController;
    private int PlayerNumber;
    private CardDesk GameCardDesk;

    public RobotPlayer(BaseRoundController rc, int MyNumber, CardDesk cardDesk)
    {
        roundController = rc;
        SetPlayerNumber(MyNumber);
        GameCardDesk = cardDesk;
    }

    @Override
    public int GetPlayerNumber() {
        return PlayerNumber;
    }

    @Override
    public void SetPlayerNumber(int num) {
        PlayerNumber = num;
    }

    @Override
    public List<Card> GetHandCards() {
        return null;
    }

    @Override
    public void InitSetHandCards(@NonNull List<Card> InitHandCards) {
        handCards = InitHandCards;
    }

    @Override
    public void ShowCard(List<Integer> CardIndex) {

    }

    @Override
    public void NotifyTakeTurn() {
        Log.d("RobotPlayer","轮到我  " + PlayerNumber);
    }

    @Override
    public void HandleTakeTurn() {

    }
}
