package com.nemesiss.chuodaidi.Game.Component.Card;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardValidationRules {

    public static Card Diamond3Sample = new Card("3",Card.Pattern.Diamond);

    public static boolean AShouldNotAppearInCenter(List<Card> cards)
    {
        // 同花顺，杂顺需要做对A的判定
        if(IsContainSomeCardPoint(cards,"A"))
        {
            return cards.get(0).getPoint().equals("A") || cards.get(cards.size() - 1).getPoint().equals("A");
        }
        else return true;
    }

    public static boolean IsContainSomeCard(List<Card> cards,Card sample)
    {
        for (Card c : cards)
        {
            if(c.equals(sample))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean IsContainSomeCardPoint(List<Card> cards,String Point)
    {
        for (Card c : cards)
        {
            if(c.getPoint().equals(Point))
            {
                return true;
            }
        }
        return false;
    }
}
