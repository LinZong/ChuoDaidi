package com.nemesiss.chuodaidi.Game.Component.Card.Comparator;

import com.nemesiss.chuodaidi.Game.Component.Card.CardHelper;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public class CardComparatorMultiple {
    private static CardComparator singleComparator;

    static {
        singleComparator = new CardComparator();
    }

    public static boolean Compare(List<Card> left, List<Card> right) {
        int length = left.size();
        int o1PointSize1 = CardHelper.GetPointSize(left.get(0).getPoint());
        int o2PointSize1 = CardHelper.GetPointSize(right.get(0).getPoint());
        switch (length) {
            case 1:{
                return singleComparator.compare(left.get(0), right.get(0)) > 0;
            }
            case 2: {
                if (o1PointSize1 == o2PointSize1) {
                    int o11Size = left.get(0).getPattern().size;
                    int o12Size = left.get(1).getPattern().size;
                    int maxo1;
                    if (o11Size > o12Size) {
                        maxo1 = o11Size;
                    } else
                        maxo1 = o12Size;
                    int o21Size = right.get(0).getPattern().size;
                    int o22Size = right.get(1).getPattern().size;
                    int maxo2;
                    if (o21Size > o22Size) {
                        maxo2 = o21Size;
                    } else
                        maxo2 = o22Size;
                    if (maxo1 > maxo2) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return o1PointSize1 > o2PointSize1;
            }
            case 3: {
                return o1PointSize1 > o2PointSize1;
            }
            case 5: {
                int leftNum = 0;
                int rightNum = 0;
                //判断5张牌属于哪种类型
                if (CardComparator.isFlushStraight(left))
                    leftNum = 5;
                else if (CardComparator.isFourBindOne(left))
                    leftNum = 4;
                else if (CardComparator.isThreeBindTwo(left))
                    leftNum = 3;
                else if (CardComparator.isFlush(left))
                    leftNum = 2;
                else if (CardComparator.isStraight(left))
                    leftNum = 1;

                if (CardComparator.isFlushStraight(right))
                    rightNum = 5;
                else if (CardComparator.isFourBindOne(right))
                    rightNum = 4;
                else if (CardComparator.isThreeBindTwo(right))
                    rightNum = 3;
                else if (CardComparator.isFlush(right))
                    rightNum = 2;
                else if (CardComparator.isStraight(right))
                    rightNum = 1;

                if (leftNum > rightNum)
                    return true;
                else if (leftNum == rightNum) {
                    List<Card> sortLeftCards = CardComparator.sortCards(left);
                    List<Card> sortRightCards = CardComparator.sortCards(right);
                    Card leftMaxCard = sortLeftCards.get(4);
                    Card rightMaxCard = sortRightCards.get(4);
                    Card leftSecondBigCard=sortLeftCards.get(3);
                    Card rightSecondBigCard=sortRightCards.get(3);
                    int leftMiddleCard = CardHelper.GetPointSize(sortLeftCards.get(2).getPoint());
                    int rightMiddleCard = CardHelper.GetPointSize(sortRightCards.get(2).getPoint());

                    if(leftNum==5||leftNum==1)
                    {
                       if(leftMaxCard.getPoint().equals("2") && leftSecondBigCard.getPoint().equals("A"))
                           leftMaxCard=sortLeftCards.get(2);
                       if(leftMaxCard.getPoint().equals("2"))
                           leftMaxCard=leftSecondBigCard;
                        if(rightMaxCard.getPoint().equals("2") && rightSecondBigCard.getPoint().equals("A"))
                            rightMaxCard=sortRightCards.get(2);
                        if(rightMaxCard.getPoint().equals("2"))
                            rightMaxCard=rightSecondBigCard;

                        CardComparator cardComparator = new CardComparator();
                        int result = cardComparator.compare(leftMaxCard, rightMaxCard);
                        return (result > 0);
                    }
                    if ( leftNum == 2 ) {
                        CardComparator cardComparator = new CardComparator();
                        int result = cardComparator.compare(leftMaxCard, rightMaxCard);
                        return (result > 0);
                    }
                    if (leftNum == 3 || leftNum == 4)
                        return leftMiddleCard > rightMiddleCard;
                } else
                    return false;

                break;
            }
        }
        return true;
    }
}
