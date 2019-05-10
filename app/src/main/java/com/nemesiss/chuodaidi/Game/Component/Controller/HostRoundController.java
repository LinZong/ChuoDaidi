package com.nemesiss.chuodaidi.Game.Component.Controller;

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
import java.util.List;

public class HostRoundController implements BaseRoundController {


    private ChuoDaidiActivity HostGameActivity;

    // 掌控的玩家
    private Player[] AllPlayer;
    private int WinnerPlayer = -1;
    private int NextTurn = -1;

    // 牌桌
    private CardDesk GameCardDesk;

    // 牌桌上的控制按钮

    public static Handler MessageHandler;


    static class RoundMessageHandler extends Handler
    {
        private WeakReference<BaseRoundController> rc;
        private WeakReference<CardDesk> innerCardDesk;
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
//                case RoundControllerMessage.SHOWN_ALL_CARD: {
//                    Bundle bd = msg.getData();
//                    int Who = bd.getInt("Who");
//                    rc.get().HandleGameSettle(Who);
//                    break;
//                }
                case RoundControllerMessage.BEGIN_SHOW_CARD:{
                    // 如果是SELF，通知牌桌显示出牌按钮
                    innerCardDesk.get().ShowPokeControlPanel();
                    break;
                }
                case RoundControllerMessage.FINISH_SHOW_CARD:{
                    // 如果是SELF，通知牌桌隐藏出牌按钮
                    innerCardDesk.get().HidePokeControlPanel();
                    MessageHandler.removeMessages(RoundControllerMessage.SHOW_CARD_OVERTIME);

                    break;
                }
                case RoundControllerMessage.SHOW_CARD_OVERTIME:{
                    innerCardDesk.get().HidePokeControlPanel();

                    break;
                }
            }
        }

        RoundMessageHandler(BaseRoundController roundController,CardDesk cd)
        {
            rc = new WeakReference<>(roundController);
            innerCardDesk = new WeakReference<>(cd);
        }
    }

    public HostRoundController(ChuoDaidiActivity act,CardDesk cd)
    {
        HostGameActivity = act;
        MessageHandler = new RoundMessageHandler(this,cd);
        GameCardDesk = cd;
    }

    @Override
    public void TakeTurn() {

        AllPlayer[NextTurn].NotifyTakeTurn();
    }

    @Override
    public void NewCompetition(List<Player> TogetherPlayer,@NonNull Player Self) {
        // 保存当局的所有Player
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
        // 通知CardDesk开启新局
        GameCardDesk.NewCompetition(Self);
        // 开始轮转
        TakeTurn();
    }


    @Override
    public void HandleShowCard(int Who, List<Card> ShownCard) {

    }


    @Override
    public void HandleGameSettle(int Who) {

    }

    @Override
    public Handler GetMessageHandler() {
        return MessageHandler;
    }
}
