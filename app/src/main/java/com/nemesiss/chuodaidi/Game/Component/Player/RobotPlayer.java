package com.nemesiss.chuodaidi.Game.Component.Player;

import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.ArrayList;
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
        return handCards;
    }

    @Override
    public void InitSetHandCards(@NonNull List<Card> InitHandCards) {
        handCards = InitHandCards;
    }

    @Override
    public void ShowCard(List<Integer> CardIndex) {

        List<Card> selected = new ArrayList<>();
        for (Integer index : CardIndex)
        {
            selected.add(handCards.get(index));
        }
        for (Card card : selected)
        {
            handCards.remove(card);
        }

        Message msg = GameHelper.BuildSelectCardMessage(PlayerNumber,selected,handCards.size() == 0);
        roundController.GetMessageHandler().sendMessage(msg);
    }

    @Override
    public void NotifyTakeTurn() {
        Log.d("RobotPlayer","轮到我  " + PlayerNumber);
        HandleTakeTurn();
    }

    @Override
    public void HandleTakeTurn() {
        List<Card> show = handCards.subList(0,1);
        GameCardDesk.ShowCards(PlayerNumber,show);
        List<Integer> showIndex = new ArrayList<>();
        showIndex.add(0);
        ShowCard(showIndex);
    }
}
