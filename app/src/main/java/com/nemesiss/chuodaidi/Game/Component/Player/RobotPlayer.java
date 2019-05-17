package com.nemesiss.chuodaidi.Game.Component.Player;

import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Controller.BaseRoundController;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.RobotAI.ShowCardRules;
import com.nemesiss.chuodaidi.Game.Model.Card;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;

import java.util.ArrayList;
import java.util.List;

public class RobotPlayer implements Player {

    private BasePlayerInformation RobotInformation;

    private List<Card> handCards;
    private BaseRoundController roundController;
    private int PlayerNumber;
    private CardDesk GameCardDesk;

    public RobotPlayer(BaseRoundController rc, int MyNumber, CardDesk cardDesk, BasePlayerInformation robotInformation)
    {
        roundController = rc;
        SetPlayerNumber(MyNumber);
        GameCardDesk = cardDesk;
        RobotInformation = robotInformation;
    }


    @Override
    public int GetPlayerNumber() {
        return PlayerNumber;
    }

    @Override
    public void SetPlayerNumber(int num) {
        PlayerNumber = num;
    }

    @Override
    public List<Card> GetHandCards() {
        return handCards;
    }

    @Override
    public void InitSetHandCards(@NonNull List<Card> InitHandCards) {
        handCards = InitHandCards;
    }

    @Override
    public void ShowCard(List<Integer> CardIndex) {

        List<Card> selected = new ArrayList<>();
        for (Integer index : CardIndex)
        {
            selected.add(handCards.get(index));
        }
        for (Card card : selected)
        {
            handCards.remove(card);
        }

        GameCardDesk.ShowCards(GetPlayerNumber(),selected);
        Message msg = GameHelper.BuildSelectCardMessage(PlayerNumber,selected,handCards.size() == 0);
        roundController.GetMessageHandler().sendMessage(msg);
    }

    @Override
    public void NotifyTakeTurn() {
        Log.d("RobotPlayer","轮到我  " + PlayerNumber);
        HandleTakeTurn();
    }

    @Override
    public void HandleTakeTurn() {

        if(roundController.IsFirstTurn())
        {
            List<Card> shown = ShowCardRules.DetermineShowCard(handCards,null,true);
            List<Integer> shownIndex = GetShowCardIndex(shown);
            ShowCard(shownIndex);
            return;
        }
        else {
            int last = (roundController.GetNextTurn() + 4 - 1) % 4;
            //if(!IamFirst) {
                List<Card> MyLastHostShownCard = GameCardDesk.GetAllHadShownCards()[last];
                while (MyLastHostShownCard.isEmpty() && (last + 4 - 1) % 4 != GetPlayerNumber()) {
                    last = (last + 4 - 1) % 4;
                    MyLastHostShownCard = GameCardDesk.GetAllHadShownCards()[last];

                }
                List<Card> shown = ShowCardRules.DetermineShowCard(handCards,MyLastHostShownCard.isEmpty() ? null : MyLastHostShownCard,false);
                List<Integer> shownIndex = GetShowCardIndex(shown);
                ShowCard(shownIndex);
//            }
//            else {
//                List<Card> shown = ShowCardRules.DetermineShowCard(handCards,null,false);
//                List<Integer> shownIndex = GetShowCardIndex(shown);
//                ShowCard(shownIndex);
//            }
//            return;
        }
    }

    @Override
    public BasePlayerInformation getPlayerInformation() {
        return RobotInformation;
    }

    private List<Integer> GetShowCardIndex(List<Card> hc)
    {
        List<Integer> result = new ArrayList<>();
        for (Card card : hc)
        {
            result.add(handCards.indexOf(card));
        }
        return result;
    }
}
