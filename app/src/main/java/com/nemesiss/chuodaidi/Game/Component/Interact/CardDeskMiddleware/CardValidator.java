package com.nemesiss.chuodaidi.Game.Component.Interact.CardDeskMiddleware;

import android.content.Context;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardValidator implements BaseMiddleware
{
    public CardValidator()
    {

    }
    @Override
    public void Handle(CardDesk deskSelf, Context context, CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger)
    {
        List<Card> seleted = deskSelf.getSelectedCards();
        // TODO Execute some validate logic here.
        boolean validResult = true;
        if(validResult)
            nextTrigger.next();//允許執行下一個中间件
        else {
            deskSelf.getShowCardButton().setEnabled(false);
        }
    }
}
