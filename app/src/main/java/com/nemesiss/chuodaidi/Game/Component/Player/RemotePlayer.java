package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class RemotePlayer implements Player {
    

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
