package com.nemesiss.chuodaidi.Game.Component.Controller;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class ClientRoundController implements BaseRoundController {

    private static Handler MessageHandler = null;

    public ClientRoundController()
    {

    }

    @Override
    public void NextTurn() {

    }

    @Override
    public void TakeTurn() {

    }

    @Override
    public void NewCompetition(List<Player> TogetherPlayer, @NonNull Player Self) {

    }

    @Override
    public void HandleShowCard(int Who, List<Card> ShownCard) {

    }

    @Override
    public void HandleGameSettle(int Who) {

    }

    @Override
    public int GetCurrentTurnPlayerNumber()
    {
        return 0;
    }

    @Override
    public Player[] GetAllPlayer()
    {
        return new Player[0];
    }

    @Override
    public Handler GetMessageHandler() {
        return MessageHandler;
    }

    @Override
    public void ShouldExit()
    {

    }

    @Override
    public int JudgeFirstTurnPlayer() {
        return 0;
    }
}
