package com.nemesiss.chuodaidi.Game.Component.Controller;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public interface BaseRoundController {
    void TakeTurn();
    void NewCompetition(List<Player> TogetherPlayer,@NonNull Player Self);
    void HandleShowCard(int Who, List<Card> ShownCard);
    void HandleGameSettle(int Who);

    Handler MessageHandler = null;

    Handler GetMessageHandler();
}
