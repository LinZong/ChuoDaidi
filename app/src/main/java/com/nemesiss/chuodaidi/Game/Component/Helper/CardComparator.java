package com.nemesiss.chuodaidi.Game.Component.Helper;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2) {
        int o1PointSize = CardHelper.GetPointSize(o1.getPoint());
        int  o2PointSize = CardHelper.GetPointSize(o2.getPoint());

        if(o1PointSize == o2PointSize) {
            // 比较Pattern
            int o1Size = o1.getPattern().size;
            int o2Size = o2.getPattern().size;
            if(o1Size == o2Size) {
                return 0;
            }
            if(o1Size > o2Size) {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            if(o1PointSize > o2PointSize) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
}
