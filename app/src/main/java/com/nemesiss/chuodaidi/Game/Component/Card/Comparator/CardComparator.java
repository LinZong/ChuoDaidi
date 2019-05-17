package com.nemesiss.chuodaidi.Game.Component.Card.Comparator;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.nemesiss.chuodaidi.Game.Component.Card.CardHelper.GetPointSize;
import static com.nemesiss.chuodaidi.Game.Component.Card.CardHelper.GetPointSizeInFive;

public class CardComparator implements Comparator<Card>
{
    @Override
    public int compare(Card o1, Card o2)
    {
        int o1PointSize = GetPointSize(o1.getPoint());
        int o2PointSize = GetPointSize(o2.getPoint());

        if (o1PointSize == o2PointSize)
        {
            // 比较Pattern
            int o1Size = o1.getPattern().size;
            int o2Size = o2.getPattern().size;
            if (o1Size == o2Size)
            {
                return 0;
            }
            if (o1Size > o2Size)
            {
                return 1;
            } else
            {
                return -1;
            }
        } else
        {
            if (o1PointSize > o2PointSize)
            {
                return 1;
            } else
            {
                return -1;
            }
        }
    }

    public static boolean isFlushStraight(List<Card> cards) //同花顺
    {
        int pattern = cards.get(0).getPattern().size;
        for (int i = 1; i < 5; i++)
        {
// 有花色异常则返回false
            if (pattern != cards.get(i).getPattern().size)
                return false;
        }
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSizeInFive(sortedCards.get(0).getPoint());
        int point2 = GetPointSizeInFive(sortedCards.get(1).getPoint());
        int point3 = GetPointSizeInFive(sortedCards.get(2).getPoint());
        int point4 = GetPointSizeInFive(sortedCards.get(3).getPoint());
        int point5 = GetPointSizeInFive(sortedCards.get(4).getPoint());
        if (point1 + 1 == point2 && point1 + 2 == point3 && point1 + 3 == point4 && point1 + 4 == point5)
        {
            return true;
        } else if (point5 == 1 && point1 == 10 && point2 == 11 && point3 == 12 && point4 == 13)
            return true;
        else
            return false;
    }

    public static boolean isStraight(List<Card> cards) //杂顺
    {
        if (isFlushStraight(cards))
            return false;
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSizeInFive(sortedCards.get(0).getPoint());
        int point2 = GetPointSizeInFive(sortedCards.get(1).getPoint());
        int point3 = GetPointSizeInFive(sortedCards.get(2).getPoint());
        int point4 = GetPointSizeInFive(sortedCards.get(3).getPoint());
        int point5 = GetPointSizeInFive(sortedCards.get(4).getPoint());
        if (point1 + 1 == point2 && point1 + 2 == point3 && point1 + 3 == point4 && point1 + 4 == point5)
        {
            return true;
        } else if (point5 == 1 && point1 == 10 && point2 == 11 && point3 == 12 && point4 == 13)
            return true;
        else
            return false;
    }

    public static boolean isFlush(List<Card> cards) //同花五
    {
        if (isFlushStraight(cards))
            return false;
        int pattern1 = cards.get(0).getPattern().size;
        int pattern2 = cards.get(1).getPattern().size;
        int pattern3 = cards.get(2).getPattern().size;
        int pattern4 = cards.get(3).getPattern().size;
        int pattern5 = cards.get(4).getPattern().size;
        if (pattern1 == pattern2 && pattern1 == pattern3 && pattern1 == pattern4 && pattern1 == pattern5)
            return true;
        else
            return false;
    }

    public static boolean isFourBindOne(List<Card> cards)//四带一
    {
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSize(sortedCards.get(0).getPoint());
        int point2 = GetPointSize(sortedCards.get(1).getPoint());
        int point3 = GetPointSize(sortedCards.get(2).getPoint());
        int point4 = GetPointSize(sortedCards.get(3).getPoint());
        int point5 = GetPointSize(sortedCards.get(4).getPoint());
        if ((point1 == point2 && point1 == point3 && point1 == point4) || (point2 == point3 && point2 == point4 && point2 == point5))
            return true;
        else
            return false;
    }

    public static boolean isThreeBindTwo(List<Card> cards)//三带二
    {
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSize(sortedCards.get(0).getPoint());
        int point2 = GetPointSize(sortedCards.get(1).getPoint());
        int point3 = GetPointSize(sortedCards.get(2).getPoint());
        int point4 = GetPointSize(sortedCards.get(3).getPoint());
        int point5 = GetPointSize(sortedCards.get(4).getPoint());
        if ((point1 == point2 && point1 == point3 && point4 == point5) || (point1 == point2 && point3 == point4 && point3 == point5))
            return true;
        else
            return false;
    }

    public static boolean isTriple(List<Card> cards)
    {
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSize(sortedCards.get(0).getPoint());
        int point2 = GetPointSize(sortedCards.get(1).getPoint());
        int point3 = GetPointSize(sortedCards.get(2).getPoint());

        return (point1 == point3 && point2 == point3);
    }

    public static boolean isDouble(List<Card> cards)
    {
        List<Card> sortedCards = sortCards(cards);
        int point1 = GetPointSize(sortedCards.get(0).getPoint());
        int point2 = GetPointSize(sortedCards.get(1).getPoint());

        return (point1 == point2);
    }

    public static List<Card> sortCards(List<Card> cards)
    {
        Collections.sort(cards, new CardComparator());
        return cards;
    }
}
