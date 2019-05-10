package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public interface Player {
    List<Card> GetHandCards();
    void InitSetHandCards(@NonNull List<Card> InitHandCards);
    void ShowCard(List<Integer> CardIndex);
    void NotifyTakeTurn();
    void HandleTakeTurn();
}
