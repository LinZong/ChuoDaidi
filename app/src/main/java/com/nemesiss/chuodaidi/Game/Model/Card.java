package com.nemesiss.chuodaidi.Game.Model;

import org.jetbrains.annotations.Nullable;

public class Card {
    private String Point;
    private Pattern pattern;
    public Card(String point,Pattern patt)
    {
        Point = point;
        pattern = patt;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getPoint() {
        return Point;
    }

    public enum Pattern {
        Spade,//♠️
        Heart, // ♥️
        Diamond, // 方块
        Club // ♣️
        // 方块  梅花  红心  黑桃  (从小到大)
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj == null) return false;
        if(obj instanceof Card) {
            Card right = (Card) obj;
            return this.getPoint().equals(right.getPoint()) && this.getPattern().equals(right.getPattern());
        }
        return true;
    }
}