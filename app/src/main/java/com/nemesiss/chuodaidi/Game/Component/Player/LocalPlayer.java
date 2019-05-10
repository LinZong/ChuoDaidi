package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class LocalPlayer implements Player {

    private List<Card> handCards;

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

    }

    @Override
    public void NotifyTakeTurn() {

    }

    @Override
    public void HandleTakeTurn() {

    }
}
