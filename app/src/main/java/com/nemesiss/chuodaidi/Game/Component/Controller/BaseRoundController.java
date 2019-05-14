package com.nemesiss.chuodaidi.Game.Component.Controller;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public interface BaseRoundController {

    String CONTROLLER_TYPE = "CONTROLLER_TYPE";
    int HOST_CONTROLLER = 1;
    int CLIENT_CONTROLLER = 2;

    String GAME_TYPE = "GAME_TYPE";
    int ROBOTS_GAME = 3;
    int REMOTE_PLAYER_GAME = 4;

    void NextTurn();
    void TakeTurn();
    void NewCompetition(List<Player> TogetherPlayer,@NonNull Player Self);
    void HandleShowCard(int Who, List<Card> ShownCard);
    void HandleGameSettle(int Who);
    int GetCurrentTurnPlayerNumber();
    Player[] GetAllPlayer();
    Handler GetMessageHandler();
    void ShouldExit();
}
