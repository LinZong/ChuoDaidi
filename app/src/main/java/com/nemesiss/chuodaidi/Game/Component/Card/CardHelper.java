package com.nemesiss.chuodaidi.Game.Component.Card;

import com.nemesiss.chuodaidi.Game.Component.Card.CardTypeValidator.CardValidationRules;
import com.nemesiss.chuodaidi.Game.Component.Card.CardTypeValidator.ShowCardType;
import com.nemesiss.chuodaidi.Game.Component.Card.Comparator.CardComparator;
import com.nemesiss.chuodaidi.Game.Component.Card.Comparator.CardComparatorInFive;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.lang.reflect.Array;
import java.util.*;

public class CardHelper
{


    public static List<Card> GetShuffledCard()
    {
        List<Card> shuffle = new ArrayList<>();
        String[] Points = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "J", "Q", "K"};
        Card.Pattern[] patterns = {Card.Pattern.Club, Card.Pattern.Diamond, Card.Pattern.Heart, Card.Pattern.Spade};

        for (int i = 0; i < patterns.length; i++)
        {
            for (int j = 0; j < Points.length; j++)
            {
                shuffle.add(new Card(Points[j], patterns[i]));
            }
        }

        Collections.shuffle(shuffle);
        return shuffle;
    }

    public static List[] GetShuffledCardGroup()
    {
        List<Card> shuffled = GetShuffledCard();

        List<Card> g1 = new ArrayList<Card>(shuffled.subList(0, 13));
        List<Card> g2 = new ArrayList<Card>(shuffled.subList(13, 26));
        List<Card> g3 = new ArrayList<Card>(shuffled.subList(26, 39));
        List<Card> g4 = new ArrayList<Card>(shuffled.subList(39, 52));

        Collections.sort(g1, new CardComparator());
        Collections.sort(g2, new CardComparator());
        Collections.sort(g3, new CardComparator());
        Collections.sort(g4, new CardComparator());

        return new List[]{g1, g2, g3, g4};
    }


    public static List[] GroupCardByPattern(List<Card> cards)
    {
        List[] result = new List[5];
        for (int i = 1; i < result.length; i++)
        {
            result[i] = new ArrayList();
        }

        for (Card card : cards)
        {
            int type = card.getPattern().size;
            result[type].add(card);
        }
        return result;
    }

    public static List<List<Card>> FindAllFlush(List<Card> singlePattern)
    {
        List<List<Card>> result = new ArrayList<>();
        for (int i = 0; i <= singlePattern.size() - 5; i++)
        {
            if (CardComparator.isFlush(singlePattern.subList(i, i + 5)))
            {
                result.add(new ArrayList<>(singlePattern.subList(i, i + 5)));
            }
        }
        return result;
    }

    public static List<List<Card>> FindAllStraight(List<Card> handCards)
    {
        List<Card> copy = CopyCard(handCards);
        System.out.println("以A作为最小牌探测杂顺");
        Collections.sort(copy, new CardComparatorInFive());

        List<List<Card>> firstTempResult,
                firstResult = new ArrayList<>();


        firstTempResult = GetPointContinue(copy, 5);
        if (!firstTempResult.isEmpty())
        {
            for (List<Card> list : firstTempResult)
            {
                // 要求满足A必须只在头尾，并且不能是同花顺（这里求的是杂顺）
                if (CardValidationRules.AShouldNotAppearInCenter(list)
                        && CardComparator.isStraight(list))
                {
                    firstResult.add(list);
                }
            }
        } else
        {
            System.out.println("以A作为最小牌不可能探测到杂顺");
        }

        return firstResult;
    }

    public static List<Card> CopyCard(List<Card> original)
    {
        return new ArrayList<>(original);
    }

    public static List<List<Card>> CombineTwoCardGroup(List<List<Card>> first, List<List<Card>> second)
    {
        HashMap<String, List<Card>> diff = new HashMap<>();
        for (List<Card> list : first)
        {
            diff.put(GenerateCardsKey(list), list);
        }
        for (List<Card> list : second)
        {
            diff.put(GenerateCardsKey(list), list);
        }

        return new ArrayList<>(diff.values());
    }

    public static List<List<Card>> FindAllFlushStraight(List<Card> singlePattern)
    {

        // 寻找同花顺，传入的参数要保证同花，已完成
        System.out.println("尝试探测同花顺");

        List<Card> copy = new ArrayList<>(singlePattern);
        Collections.sort(copy, new CardComparatorInFive());

        List<List<Card>> firstResult = new ArrayList<>();
//        for (int i = 0; i <= copy.size()-5; i++)
//        {
////            List<Card> sub = copy.subList(i,i+5);
////            if(CardValidationRules.AShouldNotAppearInCenter(sub)
////                    && CardComparator.isFlushStraight(sub))
////            {
////                System.out.println("找到一对同花顺!");
////                List<Card> oneFlushStraight = new ArrayList<>(sub);
////                firstResult.add(oneFlushStraight);
////            }
//
//            GetPointContinue()
//        }

        List<List<Card>> ASmallMaybe = GetPointContinue(copy, 5);

        for (List<Card> cards : ASmallMaybe)
        {
            if (CardValidationRules.AShouldNotAppearInCenter(cards)
                    && CardComparator.isFlushStraight(cards))
            {
                System.out.println("找到一对同花顺!");
                List<Card> oneFlushStraight = new ArrayList<>(cards);
                firstResult.add(oneFlushStraight);
            }
        }
        if (firstResult.isEmpty())
        {
            System.out.println("找不到任何的同花顺!");
        }

        return firstResult;
    }


    public static List<List<Card>> FindThreeBindTwo(List<Card> cards)
    {
        // 寻找三带二,  已完成
        List<Card> copy = new ArrayList<>(cards);
        Collections.sort(copy, new CardComparatorInFive());

        List<List<Card>> ContinuePoint = GetPointSameContinue(cards, 3);
        List<List<Card>> result = new ArrayList<>();
        if (!ContinuePoint.isEmpty())
        {
            for (List<Card> cardList : ContinuePoint)
            {
                List<Card> tmpCopy = new ArrayList<>(copy);
                tmpCopy.removeAll(cardList);

                List<List<Card>> ContinueTwo = GetPointSameContinue(tmpCopy, 2);

                if (!ContinueTwo.isEmpty())
                {
                    for (List<Card> two : ContinueTwo)
                    {
                        List<Card> one = new ArrayList<>();
                        one.addAll(cardList);
                        one.addAll(two);
                        result.add(one);
                    }
                }
            }

        }
        return result;
    }


    public static List<List<Card>> FindFourBindOne(List<Card> cards)
    {
        // 寻找四带一，已完成


        List<Card> copy = new ArrayList<>(cards);
        Collections.sort(copy, new CardComparatorInFive());

        List<List<Card>> ContinuePoint = GetPointSameContinue(cards, 4);
        List<List<Card>> result = new ArrayList<>();

        if (!ContinuePoint.isEmpty())
        {
            // 这样的话剩下的就是些不能成对的单张了
            for (List<Card> cardList : ContinuePoint)
            {

                List<Card> tmpCopy = new ArrayList<>(copy);
                tmpCopy.removeAll(cardList);
                for (Card card : tmpCopy)
                {
                    List<Card> one = new ArrayList<>();
                    one.addAll(cardList);
                    one.add(card);

                    result.add(one);
                }
            }
        }
        return result;
    }

    public static List<List<Card>> GetPointSameContinue(List<Card> sorted, int continueNum)
    {
        // 寻找点数相同的连续牌

        List<List<Card>> result = new ArrayList<>();
        int MaxThreshold = sorted.size() - continueNum;
        int begin = 0;
        while (begin <= MaxThreshold)
        {
            boolean CanAdd = true;
            for (int i = 1; i <= continueNum - 1; i++)
            {
                int CurrPtr = CardHelper.GetPointSizeInFive(sorted.get(begin + i - 1).getPoint());
                int NextPtr = CardHelper.GetPointSizeInFive(sorted.get(begin + i).getPoint());
                if (CurrPtr != NextPtr)
                {
                    begin += i;
                    CanAdd = false;
                    break;
                }
            }
            if (CanAdd)
            {
                result.add(new ArrayList<>(sorted.subList(begin, begin + continueNum)));
                begin += continueNum;
            }

        }
        return result;
    }


    public static String GenerateCardsKey(List<Card> cards)
    {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards)
        {
            sb.append(card.getPoint());
        }
        return sb.toString();
    }

    public static List<List<Card>> GetPointContinue(List<Card> sorted, int continueNum)
    {
        // 寻找点数递增的连续牌
        List<List<List<Card>>> tmpResult = new ArrayList<>();

        HashMap<Integer, HashSet<Card>> slots = new HashMap<>();
        for (int i = 1; i <= 14; i++)
        {
            slots.put(i, new HashSet<>());
        }

        List<Card> copy = new ArrayList<>(sorted);
        Collections.sort(copy, new CardComparatorInFive());

        for (Card card : copy)
        {
            int pointNum = CardHelper.GetPointSizeInFive(card.getPoint());
            if (card.getPoint().equals("A"))
            {
                int pointBigNum = CardHelper.GetPointSize(card.getPoint());
                slots.get(pointNum).add(card);
                slots.get(pointBigNum).add(card);
            } else
            {
                slots.get(pointNum).add(card);
            }
        }

        int begin = 1;
        int threshold = 14 - continueNum + 1;
        while (begin <= threshold)
        {
            boolean CanAdd = true;
            for (int i = 1; i <= continueNum; i++)
            {

                HashSet<Card> left = slots.get(begin + i - 1);
                HashSet<Card> right = slots.get(begin + i);
                if ((i != continueNum) && (left.isEmpty() || right.isEmpty()))
                {
                    CanAdd = false;
                    begin += i;
                    break;
                }
            }
            if (CanAdd)
            {
                // 找到一组
                List<List<Card>> one = new ArrayList<>();
                for (int i = begin; i < begin + continueNum; i++)
                {
                    List<Card> oneSlotCard = Arrays.asList(slots.get(i).toArray(new Card[0]));
                    one.add(oneSlotCard);
                }
                tmpResult.add(one);
                begin++;
            }
        }
        return GetAllCombinations(tmpResult);
    }

    public static List<List<Card>> GetAllCombinations(List<List<List<Card>>> cardLists)
    {
        List<List<Card>> result;

        List<List<List<Card>>> resultCollections = new ArrayList<>();

        for (List<List<Card>> cardList : cardLists)
        {

            result = new ArrayList<>();
            List<Card> cl = cardList.get(cardList.size() - 1);

            for (Card card : cl)
            {
                List<Card> tmpContainer = new ArrayList<>();
                tmpContainer.add(card);
                result.add(tmpContainer);
            }

            List[] process = cardList.toArray(new List[0]);

            for (int i = process.length - 2; i >= 0; i--)
            {
                List<List<Card>> tempResult = new ArrayList<>();

                for (int i1 = 0; i1 < process[i].size(); i1++)
                {

                    for (int i2 = 0; i2 < result.size(); i2++)
                    {
                        List<Card> each = new ArrayList<>();
                        each.add((Card) process[i].get(i1));
                        each.addAll(result.get(i2));
                        tempResult.add(each);
                    }
                }
                result = tempResult;
            }
            resultCollections.add(result);
        }

        List<List<Card>> finalResult = new ArrayList<>();


        for (List<List<Card>> rc : resultCollections)
        {
            finalResult.addAll(rc);
        }
        return finalResult;
    }


    public static HashMap<Integer,List<List<Card>>> TryGroupCards(List<Card> handCards)
    {

        List<List<Card>> FlushStraights = null,
                FourBindOne = null,
                ThreeBindTwo = null,
                Flush = null,
                Straight = null;

        HashMap<Integer,List<List<Card>>> cardGroups = new HashMap<>();
        for (int i = 1; i <= 8 ; i++)
        {
            cardGroups.put(i,new ArrayList<>());
        }

        // Show What cards we get.
        //System.out.println("拿到手牌!如下所示!");
//
//        for (Object o : handCards)
//        {
//            System.out.println(o);
//        }
       // System.out.println("将手牌按照花色大小排列");

        List[] groupByPattern = GroupCardByPattern(handCards);

       // System.out.println("判断是否能够组成各种顺");
        boolean MayHaveFiveCardGroup = false;
        for (int i = 1; i <= 4; i++)
        {
            if (groupByPattern[i].size() >= 5)
            {
                MayHaveFiveCardGroup = true;
            }
        }
        if (MayHaveFiveCardGroup)
        {
            // 查找各种五张牌的情景
            for (int i = 1; i <= 4; i++)
            {
                // 同花顺
                FlushStraights = FindAllFlushStraight(groupByPattern[i]);
                // 五同花
                Flush = FindAllFlush(groupByPattern[i]);
                // 杂顺
                if (FlushStraights != null && !FlushStraights.isEmpty())
                {
                    System.out.println("找到同花顺!");
                   // PrintOutCardGroups(FlushStraights);
                    cardGroups.get(ShowCardType.FIFTH_FLUSH_STRAIGHT).addAll(FlushStraights);
                }
                if (Flush != null && !Flush.isEmpty())
                {
                    System.out.println("找到5同花!");
                    //PrintOutCardGroups(Flush);
                    cardGroups.get(ShowCardType.FIFTH_FLUSH).addAll(Flush);
                }

            }

            Straight = FindAllStraight(handCards);
            if (Straight != null && !Straight.isEmpty())
            {
                System.out.println("找到杂顺!");
               // PrintOutCardGroups(Straight);
                cardGroups.get(ShowCardType.FIFTH_STRAIGHT).addAll(Straight);
            }
        }

        List<List<Card>> fourBindOne = FindFourBindOne(handCards);
        if (fourBindOne != null && !fourBindOne.isEmpty())
        {
            System.out.println("找到四带一！");
          //  PrintOutCardGroups(fourBindOne);
            cardGroups.get(ShowCardType.FIFTH_FOUR_BIND_ONE).addAll(fourBindOne);
        } else
        {
            System.out.println("找不到四带一!");
        }

        System.out.println("检测下一项目: 三带二");
        List<List<Card>> threeBindTwo = FindThreeBindTwo(handCards);
        if (threeBindTwo != null && !threeBindTwo.isEmpty())
        {
            System.out.println("找到三带二！");
            //PrintOutCardGroups(threeBindTwo);
            cardGroups.get(ShowCardType.FIFTH_THREE_BIND_TWO).addAll(threeBindTwo);
        } else
        {
            System.out.println("找不到三带二!");
        }
        // find pairs

        List<List<Card>> pairs3 = GetPointSameContinue(handCards,3);
        cardGroups.get(ShowCardType.TRIPLE).addAll(pairs3);
        List<List<Card>> pairs2 = GetPointSameContinue(handCards,2);
        cardGroups.get(ShowCardType.DOUBLE).addAll(pairs2);
        // 单张就不加了

        return cardGroups;

    }

    public static void PrintOutCardGroups(List<List<Card>> cardGroups)
    {

        if (cardGroups != null && !cardGroups.isEmpty())
        {
            System.out.println("-----------开始打印牌组---------");
            for (List<Card> cardGroup : cardGroups)
            {
                System.out.println("------------------------------");
                for (Card card : cardGroup)
                {
                    System.out.println(card);
                }
            }
            System.out.println("-----------结束打印牌组---------");
        }
    }

    public static int GetPointSize(String point)
    {
        if (point.equals("J"))
        {
            return 11;
        }
        if (point.equals("Q"))
        {
            return 12;
        }
        if (point.equals("K"))
        {
            return 13;
        }
        if (point.equals("A"))
        {
            return 14;
        }
        if (point.equals("2"))
        {
            return 15;
        } else
        {
            return Integer.parseInt(point);
        }
    }

    public static int GetPointSizeInFive(String point)
    {
        if (point.equals("J"))
        {
            return 11;
        }
        if (point.equals("Q"))
        {
            return 12;
        }
        if (point.equals("K"))
        {
            return 13;
        }
        if (point.equals("A"))
        {
            return 1;
        } else
        {
            return Integer.parseInt(point);
        }
    }


    public static int JudgeCardGroupType(List<Card> cards)
    {
        // 判断当前的牌组是什么类型

        if (cards == null || cards.isEmpty())
        {
            return ShowCardType.UNKNOWN;
        } else
        {
            switch (cards.size())
            {
                case 1:
                    return ShowCardType.SINGLE;
                case 2:
                    if(CardComparator.isDouble(cards)){
                        return ShowCardType.DOUBLE;
                    }
                    break;
                case 3:
                    if(CardComparator.isTriple(cards)){
                        return ShowCardType.TRIPLE;
                    }
                    break;
                case 5:
                {
                    if (CardComparator.isFlushStraight(cards))
                    {
                        return ShowCardType.FIFTH_FLUSH_STRAIGHT;
                    }
                    if (CardComparator.isFourBindOne(cards))
                    {
                        return ShowCardType.FIFTH_FOUR_BIND_ONE;
                    }
                    if (CardComparator.isThreeBindTwo(cards))
                    {
                        return ShowCardType.FIFTH_THREE_BIND_TWO;
                    }
                    if (CardComparator.isFlush(cards))
                    {
                        return ShowCardType.FIFTH_FLUSH;
                    }
                    if (CardComparator.isStraight(cards))
                    {
                        return ShowCardType.FIFTH_STRAIGHT;
                    }
                    break;
                }
                default:
                    return ShowCardType.UNKNOWN;
            }
        }
        return ShowCardType.UNKNOWN;
    }
}
