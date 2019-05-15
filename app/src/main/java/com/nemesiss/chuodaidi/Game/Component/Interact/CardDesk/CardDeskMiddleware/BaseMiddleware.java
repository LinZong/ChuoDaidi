package com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware;

import android.content.Context;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;

public interface BaseMiddleware
{
    void Handle(CardDesk deskSelf, Context context,CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger);
}
