package com.nemesiss.chuodaidi.Game.Component.Interact.CardDeskMiddleware;

import android.content.Context;
import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;

public interface BaseMiddleware
{
    void Handle(CardDesk deskSelf, Context context,CardDeskMiddlewarePool.MiddlewarePipeInterceptor nextTrigger);
}
