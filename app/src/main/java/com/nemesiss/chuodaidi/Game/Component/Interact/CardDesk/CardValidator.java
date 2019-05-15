package com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;

import android.content.Context;
import android.widget.Toast;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.BaseMiddleware;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.CardDeskMiddlewarePool;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardValidator implements BaseMiddleware
{

    @Override
    public void Handle(CardDesk deskSelf, Context context, CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger)
    {
        List<Card> seleted = deskSelf.getSelectedCards();

        if(seleted.isEmpty()) {
            Toast.makeText(context,"必须至少选择一张牌!", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean validResult = seleted.size() <= 5;

            // TODO Execute some detailed validate logic here.
            if(validResult)
                nextTrigger.next();//允許執行下一個中间件
            else {
                Toast.makeText(context,"不允许一次出超过5张牌!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
