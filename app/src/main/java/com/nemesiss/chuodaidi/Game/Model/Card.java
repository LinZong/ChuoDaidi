package com.nemesiss.chuodaidi.Game.Model;

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
}


