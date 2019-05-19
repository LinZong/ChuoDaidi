package com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDeskMiddleware;

import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk.CardDesk;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CardDeskMiddlewarePool
{
    public HashMap<Integer,List<BaseMiddleware>> MiddlewareQueue;
    public HashMap<Integer,BaseMiddleware> EndupMiddleware;
    public HashMap<Integer,Integer> MiddlewareIterator;
    private CardDesk mCardDesk;


    public CardDeskMiddlewarePool(CardDesk cd)
    {
        mCardDesk = cd;

        Field[] middleWareFields = MiddlewareType.class.getFields();
        MiddlewareQueue = new HashMap<>();
        EndupMiddleware = new HashMap<>();
        MiddlewareIterator = new HashMap<>();

        for (int i = 0; i < middleWareFields.length; i++)
        {
            int mod = middleWareFields[i].getModifiers();
            boolean IsTarget = Modifier.isStatic(mod) && Modifier.isFinal(mod) && middleWareFields[i].getType().getSimpleName().equals("int");
            if(IsTarget) {
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
            return true;
        }
        return false;
    }
    public boolean SetEndupMiddleware(int MiddlewareType,BaseMiddleware middleware) {
        if(EndupMiddleware!=null) {
            EndupMiddleware.put(MiddlewareType,middleware);
            return true;
        }
        return false;
    }

    public void ExecuteMiddlewares(int MiddlewareType)
    {
        List<BaseMiddleware> bm = MiddlewareQueue.get(MiddlewareType);
        if(bm!=null && !bm.isEmpty()) {
            MiddlewareIterator.put(MiddlewareType,1);
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
                        BaseMiddleware endUp =  EndupMiddleware.get(MiddlewareType);
                        if(endUp!=null) {
                            endUp.Handle(mCardDesk, mCardDesk.getContext(), new MiddlewarePipeInterceptor() {
                                @Override
                                public void next() {
                                    MiddlewareIterator.put(MiddlewareType,0);
                                    // reset iter to the first middleware.
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public interface MiddlewarePipeInterceptor {
        void next();
    }
}
