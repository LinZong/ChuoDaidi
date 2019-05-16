package com.nemesiss.chuodaidi.Game.Component.RobotAI;

import com.nemesiss.chuodaidi.Game.Component.Card.CardComparator;
import com.nemesiss.chuodaidi.Game.Component.Card.CardComparatorMultiple;
import com.nemesiss.chuodaidi.Game.Component.Card.CardHelper;
import com.nemesiss.chuodaidi.Game.Component.Card.CardValidationRules;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowCardRules
{

    private static List<Card> BuildSingleCardGroup(Card Single)
    {
        List<Card> cards = new ArrayList<>();
        cards.add(Single);
        return cards;
    }


    public static List<Card> MultipleShowCard(List<List<Card>> MyCardGroup, List<Card> LastHostShownCard)
    {
        for (List<Card> pair : MyCardGroup)
        {
            if (CardComparatorMultiple.Compare(pair, LastHostShownCard))
            {
                return pair;
            }
        }
        return new ArrayList<>();
    }

    public static List<Card> DetermineShowCard(List<Card> MyCard, List<Card> LastHostShownCard, boolean IsFirstShow)
    {
        if (LastHostShownCard == null)
        {
            // Should show club 3, and other cards as much as possible
            HashMap<Integer, List<List<Card>>> groups = CardHelper.TryGroupCards(MyCard);

            for (int i = 4; i <= 6; i++)
            {
                List<List<Card>> eachGroup = groups.get(i);
                if(IsFirstShow) {
                    for (List<Card> cards : eachGroup)
                    {
                        if (CardValidationRules.IsContainSomeCard(cards, CardValidationRules.Diamond3Sample))
                        {
                            // 就出这张
                            return cards;
                        }
                    }
                }
                else if(!eachGroup.isEmpty()) {
                    return eachGroup.get(0);
                }
            }
            // 不要再拆大的牌了 ，拆小牌

            for (int i = 2; i <= 3; i++)
            {
                List<List<Card>> eachGroup = groups.get(i);
                if(IsFirstShow) {
                    for (List<Card> cards : eachGroup)
                    {
                        if (CardValidationRules.IsContainSomeCard(cards, CardValidationRules.Diamond3Sample))
                        {
                            // 就出这张
                            return cards;
                        }
                    }
                }
                else if(!eachGroup.isEmpty()) {
                    return eachGroup.get(0);
                }
            }
            // 三张和两张都不存在，只能单张出。
            return BuildSingleCardGroup(MyCard.get(0));
        } else
        {
            // 看看上家出的是什么牌型
            CardComparator cp = new CardComparator();
            int LastHostShownCardType = CardHelper.JudgeCardGroupType(LastHostShownCard);
            switch (LastHostShownCardType)
            {
                case 1:
                {
                    for (Card card : MyCard)
                    {
                        if (cp.compare(card, LastHostShownCard.get(0)) == 1)
                        {
                            return BuildSingleCardGroup(card);
                        }
                    }
                    return new ArrayList<>();
                }
                case 2:
                {
                    List<List<Card>> pairs = CardHelper.GetPointSameContinue(MyCard, 2);
                    return MultipleShowCard(pairs, LastHostShownCard);
                }
                case 3:
                {
                    List<List<Card>> pairs = CardHelper.GetPointSameContinue(MyCard, 3);
                    return MultipleShowCard(pairs, LastHostShownCard);
                }
                case 4:
                {

                    // 杂顺

                    List<List<Card>> straights = CardHelper.FindAllStraight(MyCard);
                    List<List<Card>> flushes = CardHelper.FindAllFlush(MyCard);

                    List<List<Card>> combine = new ArrayList<>();
                    combine.addAll(straights);
                    combine.addAll(flushes);

                    return MultipleShowCard(combine, LastHostShownCard);

                }
                case 5:
                {
                    // 五同花
                    List<List<Card>> flushes = CardHelper.FindAllFlush(MyCard);
                    List<List<Card>> threeBindTwo = CardHelper.FindThreeBindTwo(MyCard);

                    List<List<Card>> combine = new ArrayList<>();
                    combine.addAll(flushes);
                    combine.addAll(threeBindTwo);

                    return MultipleShowCard(combine, LastHostShownCard);

                }
                case 6:
                {
                    // 三带二
                    List<List<Card>> threeBindTwo = CardHelper.FindThreeBindTwo(MyCard);
                    List<List<Card>> fourBindOne = CardHelper.FindFourBindOne(MyCard);

                    List<List<Card>> combine = new ArrayList<>();
                    combine.addAll(threeBindTwo);
                    combine.addAll(fourBindOne);

                    return MultipleShowCard(combine, LastHostShownCard);

                }
                case 7:
                {
                    // 四带一
                    List<List<Card>> fourBindOne = CardHelper.FindFourBindOne(MyCard);
                    List<List<Card>> flushStraight = CardHelper.FindAllFlushStraight(MyCard);

                    List<List<Card>> combine = new ArrayList<>();
                    combine.addAll(fourBindOne);
                    combine.addAll(flushStraight);

                    return MultipleShowCard(combine, LastHostShownCard);

                }
                case 8:
                {
                    // 同花顺
                    List<List<Card>> flushStraight = CardHelper.FindAllFlushStraight(MyCard);
                    return MultipleShowCard(flushStraight, LastHostShownCard);
                }
            }
        }
        return new ArrayList<>();
    }
}
