package com.nemesiss.chuodaidi.Game.Component.Interact.CardDeskMiddleware;

import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CardDeskMiddlewarePool
{
    public HashMap<Integer,List<BaseMiddleware>> MiddlewareQueue;
    public HashMap<Integer,Integer> MiddlewareIterator;
    private CardDesk mCardDesk;


    public CardDeskMiddlewarePool()
    {
        Field[] middleWareFields = MiddlewareType.class.getFields();
        MiddlewareQueue = new HashMap<>();
        MiddlewareIterator = new HashMap<>();
        for (int i = 0; i < middleWareFields.length; i++)
        {
            boolean IsStatic = Modifier.isStatic(middleWareFields[i].getModifiers());
            if(IsStatic) {
                try
                {
                    int fieldNumber = middleWareFields[i].getInt(null);
                    MiddlewareQueue.put(fieldNumber,new ArrayList<>());
                    MiddlewareIterator.put(fieldNumber,0);
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean AddMiddleware(int MiddlewareType,BaseMiddleware middleware)
    {
        List<BaseMiddleware> bm = MiddlewareQueue.get(MiddlewareType);
        if(bm!=null) {
            bm.add(middleware);
        }
        return false;
    }

    public void ExecuteMiddlewares(int MiddlewareType)
    {
        List<BaseMiddleware> bm = MiddlewareQueue.get(MiddlewareType);
        if(bm!=null && !bm.isEmpty()) {
            MiddlewareIterator.put(MiddlewareType,0);
            bm.get(0).Handle(mCardDesk, mCardDesk.getContext(), new MiddlewarePipeInterceptor()
            {
                @Override
                public void next()
                {
                    int current = MiddlewareIterator.get(MiddlewareType);
                    List<BaseMiddleware> list = MiddlewareQueue.get(MiddlewareType);
                    if(current < list.size()) {
                        current ++;
                        MiddlewareIterator.put(MiddlewareType,current);
                        list.get(current).Handle(mCardDesk,mCardDesk.getContext(), this);
                    }
                    else {
                        // reset, do nothing.
                        MiddlewareIterator.put(MiddlewareType,0);
                    }
                }
            });
        }
    }

    public interface MiddlewarePipeInterceptor {
        void next();
    }
}
