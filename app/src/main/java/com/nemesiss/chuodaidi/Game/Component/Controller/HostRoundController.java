package com.nemesiss.chuodaidi.Game.Component.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;
import com.nemesiss.chuodaidi.Android.View.CountDownTextView;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Player.Player;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HostRoundController implements BaseRoundController {



    private ChuoDaidiActivity HostGameActivity;

    // 掌控的玩家
    private Player[] AllPlayer;
    private int WinnerPlayer = -1;
    private int NextTurn = -1;
    private int FirstTurn = -1;
    private boolean FirstEnterGame = true;
    // 牌桌
    private CardDesk GameCardDesk;
    private CountDownTextView CountDown;

    // 牌桌上的控制按钮

    public static Handler MessageHandler;


    public HostRoundController(ChuoDaidiActivity act,CardDesk cd)
    {
        HostGameActivity = act;
        MessageHandler = new Handler(this::HandleControllerMessage);
        GameCardDesk = cd;
        CountDown = HostGameActivity.findViewById(R.id.countDownTextView);
        CountDown.InitCountDown(1000,45,()-> {
            MessageHandler.sendEmptyMessage(RoundControllerMessage.SHOW_CARD_OVERTIME);
            CountDown.Cancel();
        }, act);
    }

    private void ShowPokeControlPanelForMySelf()
    {
        int CurrentTurn = GetCurrentTurnPlayerNumber();
        if(CurrentTurn == GetAllPlayer()[CardDesk.SELF].GetPlayerNumber())
        {
            GameCardDesk.ShowPokeControlPanel();
        }
    }

    private void HidePokeControlPanelForMySelf()
    {
        int CurrentTurn = GetCurrentTurnPlayerNumber();
        if(CurrentTurn == GetAllPlayer()[CardDesk.SELF].GetPlayerNumber())
        {
            GameCardDesk.HidePokeControlPanel();
        }
    }


    private boolean HandleControllerMessage(Message msg)
    {
        switch (msg.what) {
            case RoundControllerMessage.BEGIN_SHOW_CARD:{

                ShowPokeControlPanelForMySelf();

                // 启动定时器
                CountDown.Start();
                break;
            }
            case RoundControllerMessage.FINISH_SHOW_CARD:{
                // 如果是SELF，通知牌桌隐藏出牌按钮
                HidePokeControlPanelForMySelf();


                CountDown.Cancel();
                Bundle data = msg.getData();

                boolean WillWin = data.getBoolean(GameHelper.I_WILL_WIN_FLAG);
                int Who = data.getInt(GameHelper.WHO_FLAG);
                Card[] shownCard = (Card[]) data.getSerializable(GameHelper.SHOW_CARDS_FLAG);


                int Current = GetCurrentTurnPlayerNumber();

                // 所有延时的请求都丢弃
                if(Who == Current)
                {
                    HandleShowCard(Who, Arrays.asList(shownCard));
                    if(WillWin)
                    {
                        Toast.makeText(ChuoDaidiApplication.getContext(),"有人赢了, 游戏结束 " + Who,Toast.LENGTH_SHORT).show();
                        WinnerPlayer = Who;
                    }
                    else {

                        MessageHandler.removeMessages(RoundControllerMessage.SHOW_CARD_OVERTIME);
                        NextTurn();
                        TakeTurn();
                    }
                }

                break;
            }
            case RoundControllerMessage.SHOW_CARD_OVERTIME:{


                HidePokeControlPanelForMySelf();
                // 不出
                GameCardDesk.SelectCard(NextTurn,new ArrayList<>());

                NextTurn();
                TakeTurn();
                break;
            }
        }
        return true;
    }

    @Override
    public void NextTurn() {
        // 计算下一个应该是谁
        NextTurn = (NextTurn + 1) % 4;
    }

    public int GetCurrentTurnPlayerNumber()
    {
        return AllPlayer[NextTurn].GetPlayerNumber();
    }

    @Override
    public Player[] GetAllPlayer()
    {
        return AllPlayer;
    }

    @Override
    public void TakeTurn() {

        if(NextTurn == FirstTurn)
        {
            if(FirstEnterGame)
            {
                FirstEnterGame = false;
                GameCardDesk.NewTurn();
                AllPlayer[NextTurn].NotifyTakeTurn();
            }
            // 延迟几秒
            else {
                MessageHandler.postDelayed(() -> {
                    GameCardDesk.NewTurn();
                    AllPlayer[NextTurn].NotifyTakeTurn();
                }, 3000);
            }
        }
        else {
            AllPlayer[NextTurn].NotifyTakeTurn();
        }
    }

    @Override
    public void NewCompetition(List<Player> TogetherPlayer,@NonNull Player Self) {

        FirstEnterGame  = true;

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
            FirstTurn = NextTurn = sr.nextInt(4);
        }

        // 通知CardDesk开启新局
        GameCardDesk.NewCompetition(Self);
        // 开始轮转
        TakeTurn();
    }


    @Override
    public void HandleShowCard(int Who, List<Card> ShownCard) {
        // TODO 把出牌信息广播出去

    }


    @Override
    public void HandleGameSettle(int Who) {

    }

    @Override
    public Handler GetMessageHandler() {
        return MessageHandler;
    }

    public void ShouldExit()
    {
        CountDown.Cancel();
        MessageHandler.removeMessages(RoundControllerMessage.SHOW_CARD_OVERTIME);
        MessageHandler.removeMessages(RoundControllerMessage.SHOW_CARD);
        MessageHandler.removeMessages(RoundControllerMessage.BEGIN_SHOW_CARD);
        MessageHandler.removeMessages(RoundControllerMessage.FINISH_SHOW_CARD);
        MessageHandler.removeMessages(RoundControllerMessage.SHOWN_ALL_CARD);
    }
}
