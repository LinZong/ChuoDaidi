package com.nemesiss.chuodaidi.Game.Component.Player;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public interface Player {
    List<Card> GetHandCards();
    void InitSetHandCards();
    void ShowCard(List<Integer> CardIndex);
    void NotifyTakeTurn();
    void HandleTakeTurn();
}
