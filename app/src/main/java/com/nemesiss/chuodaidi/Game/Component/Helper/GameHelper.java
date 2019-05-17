package com.nemesiss.chuodaidi.Game.Component.Helper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.Android.Activity.InGameActivity;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Controller.RoundControllerMessage;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class GameHelper
{
    public static final String I_WILL_WIN_FLAG = "I_WILL_WIN_FLAG";
    public static final String SHOW_CARDS_FLAG = "SHOW_CARDS_FLAG";
    public static final String WHO_FLAG = "WHO_FLAG";

    public static Message BuildSelectCardMessage(int Who, List<Card> Selected,boolean IWillWin)
    {
        Message msg = new Message();
        msg.what = RoundControllerMessage.FINISH_SHOW_CARD;
        Bundle bd = new Bundle();
        bd.putInt(WHO_FLAG,Who);
        bd.putBoolean(I_WILL_WIN_FLAG,IWillWin);
        bd.putSerializable(SHOW_CARDS_FLAG, Selected.toArray(new Card[0]));
        msg.setData(bd);
        return msg;
    }

    public static Intent BuildRobotsPlayIntent(ChuoDaidiActivity activity)
    {
        Intent intent = new Intent(activity, InGameActivity.class);
        intent.putExtra(BaseRoundController.GAME_TYPE,BaseRoundController.ROBOTS_GAME);
        intent.putExtra(BaseRoundController.CONTROLLER_TYPE,BaseRoundController.HOST_CONTROLLER);
        return intent;
    }


}
