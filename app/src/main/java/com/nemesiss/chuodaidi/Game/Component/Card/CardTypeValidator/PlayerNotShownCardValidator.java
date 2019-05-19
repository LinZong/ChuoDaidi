package com.nemesiss.chuodaidi.Game.Component.Card.CardTypeValidator;

import android.content.Context;
import android.widget.Toast;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.BaseMiddleware;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware.CardDeskMiddlewarePool;

public class PlayerNotShownCardValidator implements BaseMiddleware
{
    @Override
    public void Handle(CardDesk deskSelf, Context context, CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger)
    {
        int current = deskSelf.GetRoundController().GetOriginalNextTurn();
        int first = deskSelf.GetRoundController().GetFirstTurn();
        if(current == first) {
            ShowHint("首轮必须出牌!");
        }
        else {
            nextTrigger.next();
        }
    }
    private void ShowHint(String content)
    {
        Toast.makeText(ChuoDaidiApplication.getContext(),content, Toast.LENGTH_SHORT).show();
    }
}
