package com.nemesiss.chuodaidi.Game.Component.Player;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Controller.RoundControllerMessage;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalPlayer implements Player {

    private List<Card> handCards;
    private BaseRoundController roundController;
    private int PlayerNumber;
    public LocalPlayer(BaseRoundController rc,int MyNumber)
    {
        roundController = rc;
        SetPlayerNumber(MyNumber);
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
        // remove that cards from my hands.
        List<Card> AllSelectedCard = new ArrayList<>();
        for (Integer i  : CardIndex) {
            AllSelectedCard.add(handCards.get(i));
        }
        for (Card card : AllSelectedCard) {
            handCards.remove(card);
        }

        Message msg = new Message();

        Bundle bd = new Bundle();
        bd.putInt("Who",GetPlayerNumber());
        bd.putSerializable("ShowCards", AllSelectedCard.toArray(new Card[0]));

        roundController.GetMessageHandler().sendMessage(msg);
    }

    @Override
    public void NotifyTakeTurn() {
        HandleTakeTurn();
    }

    @Override
    public void HandleTakeTurn() {
        // 应答TakeTurn消息，通知自身轮次控制器开始倒计时
        roundController.MessageHandler.sendEmptyMessage(RoundControllerMessage.BEGIN_SHOW_CARD);
        // 45s not show card regards as pass.
        roundController.MessageHandler.sendEmptyMessageDelayed(RoundControllerMessage.SHOW_CARD_OVERTIME,45 * 1000);
    }
}
