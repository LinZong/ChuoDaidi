package com.nemesiss.chuodaidi.Game.Component.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.Android.View.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.lang.ref.WeakReference;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class HostRoundController implements BaseRoundController {

    private ChuoDaidiActivity HostGameActivity;

    private Player[] AllPlayer;
    private int WinnerPlayer = -1;
    private int NextTurn = -1;

    private CardDesk GameCardDesk;

    public static Handler MessageHandler;


    static class RoundMessageHandler extends Handler
    {
        private WeakReference<BaseRoundController> rc;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RoundControllerMessage.SHOW_CARD : {
                    Bundle bd = msg.getData();
                    int Who = bd.getInt("Who");
                    List<Card> shownCard = (List<Card>) bd.getSerializable("ShownCard");
                    rc.get().HandleShowCard(Who,shownCard);
                    break;
                }
                case RoundControllerMessage.SHOWN_ALL_CARD: {
                    Bundle bd = msg.getData();
                    int Who = bd.getInt("Who");
                    rc.get().HandleGameSettle(Who);
                    break;
                }
            }
        }

        RoundMessageHandler(BaseRoundController roundController)
        {
            rc = new WeakReference<>(roundController);
        }
    }

    public HostRoundController(ChuoDaidiActivity act,CardDesk cd)
    {
        HostGameActivity = act;
        MessageHandler = new RoundMessageHandler(this);
        GameCardDesk = cd;
    }

    @Override
    public void TakeTurn() {
        AllPlayer[NextTurn].NotifyTakeTurn();
    }

    @Override
    public void NewCompetition(List<Player> TogetherPlayer,@NonNull Player Self) {
        AllPlayer = new Player[4];
        // TODO 检测TogetherPlayer的数目必须正好为3， 检测Self不能为空
        AllPlayer[CardDesk.SELF] = Self;
        AllPlayer[CardDesk.RIGHT] = TogetherPlayer.get(0);
        AllPlayer[CardDesk.TOP] = TogetherPlayer.get(1);
        AllPlayer[CardDesk.LEFT] = TogetherPlayer.get(2);
        // 决定谁先开局
        if(WinnerPlayer != -1) {
            NextTurn = WinnerPlayer;
        }
        else {
            // 选择NextTurn
            SecureRandom sr = new SecureRandom();
            NextTurn = sr.nextInt(4);
        }

        TakeTurn();
    }


    @Override
    public void HandleShowCard(int Who, List<Card> ShownCard) {

    }


    @Override
    public void HandleGameSettle(int Who) {

    }
}
