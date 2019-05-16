package com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;

import android.content.Context;
import android.widget.Toast;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Game.Component.Card.CardComparatorMultiple;
import com.nemesiss.chuodaidi.Game.Component.Card.CardHelper;
import com.nemesiss.chuodaidi.Game.Component.Card.CardValidationRules;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.BaseMiddleware;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.CardDeskMiddlewarePool;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardValidator implements BaseMiddleware
{

    @Override
    public void Handle(CardDesk deskSelf, Context context, CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger)
    {
        // 只有玩家出牌的时候，才会做牌类型检测。
        // 机器人出牌或者是远程玩家出牌的时候是不会进行这个检测的

        List<Card> seleted = deskSelf.getSelectedCards();

        // 检测牌是否为空
        if(seleted.isEmpty()) {
            ShowHint("必须选择至少一张牌以完成出牌!");
        }
        // 检测牌数不能超过限制
        else if(seleted.size() > 5) {
            ShowHint("不能出超过5张的牌!");
        }
        else if(-1 == CardHelper.JudgeCardGroupType(seleted))
        {
            ShowHint("玩家出牌不符合牌型!");
        }
        else {

             // 判断是否为首轮出牌，如果是的话，要求必须带方块三

            int current = deskSelf.GetRoundController().GetOriginalNextTurn();
            int first = deskSelf.GetRoundController().GetFirstTurn();
            if(current == first) {
                // 玩家是首轮出牌
                boolean ContainDiamond3 = CardValidationRules.IsContainSomeCard(seleted,CardValidationRules.Diamond3Sample);
                if(!ContainDiamond3) {
                    ShowHint("玩家首轮出牌必须包括方块3!");
                    return;
                }
                nextTrigger.next();
            }
            else {

                // 提取出我的上家的出牌
                int lastHost = (current - 1 + 4) % 4;
                List<Card> lastHostShownCard = deskSelf.GetAllHadShownCards()[lastHost];
                while (lastHostShownCard.isEmpty() && (lastHost + 4 - 1) % 4 != CardDesk.SELF) {
                    lastHost = (lastHost + 4 - 1) % 4;
                    lastHostShownCard = deskSelf.GetAllHadShownCards()[lastHost];
                }
                // 场上存在上家出的牌
                if(!lastHostShownCard.isEmpty()) {

                    if(seleted.size() != lastHostShownCard.size()) {
                        // 要求自己的出牌数量必须与上家一直
                        ShowHint("出牌数量必须与上家一致!");
                    }
                    else
                    {
                        // 判断牌型

                        int lastHostType = CardHelper.JudgeCardGroupType(lastHostShownCard);
                        int myType = CardHelper.JudgeCardGroupType(seleted);
                        if(lastHostType < 4 && myType != lastHostType) {
                            ShowHint("牌型必须与上家一致!");
                            return;
                        }
                        else {
                            boolean largeThanLastHost = CardComparatorMultiple.Compare(seleted,lastHostShownCard);
                            if(!largeThanLastHost) {
                                ShowHint("必须出大过上家的牌!");
                            }
                            else nextTrigger.next();
                        }
                    }
                }
                else nextTrigger.next();
            }
        }
    }

    private void ShowHint(String content)
    {
        Toast.makeText(ChuoDaidiApplication.getContext(),content, Toast.LENGTH_SHORT).show();
    }
}
