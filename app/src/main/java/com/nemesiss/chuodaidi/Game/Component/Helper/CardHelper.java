package com.nemesiss.chuodaidi.Game.Component.Helper;

import com.nemesiss.chuodaidi.Game.Component.Interact.CardDesk;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardHelper {
    public static List<Card> GetShuffledCard()
    {
        List<Card> shuffle = new ArrayList<>();
        String[] Points = {"2","3","4","5","6","7","8","9","10","A","J","Q","K"};
        Card.Pattern[] patterns = {Card.Pattern.Club,Card.Pattern.Diamond,Card.Pattern.Heart,Card.Pattern.Spade};

        for (int i = 0; i < patterns.length; i++) {
            for (int j = 0; j < Points.length; j++) {
                shuffle.add(new Card(Points[j],patterns[i]));
            }
        }


        Collections.shuffle(shuffle);
        return shuffle;
    }

    public static List[] GetShuffledCardGroups()
    {

        List<Card> shuffled = CardHelper.GetShuffledCard();

        List<Card> c1 = new ArrayList<>(shuffled.subList(0, 13));
        List<Card> c2 = new ArrayList<>(shuffled.subList(13, 26));
        List<Card> c3 = new ArrayList<>(shuffled.subList(26,39));
        List<Card> c4 = new ArrayList<>(shuffled.subList(39,52));

        Collections.sort(c1, new CardComparator());
        Collections.sort(c2, new CardComparator());
        Collections.sort(c3, new CardComparator());
        Collections.sort(c4, new CardComparator());

        List[] result = new List[4];
        result[CardDesk.SELF] = c1;
        result[CardDesk.RIGHT] = c2;
        result[CardDesk.TOP] = c3;
        result[CardDesk.LEFT] = c4;

        return result;
    }

    public static int GetPointSize(String point)
    {
        if(point.equals("J")) {
            return 11;
        }
        if(point.equals("Q")) {
            return 12;
        }
        if(point.equals("K")) {
            return 13;
        }
        if(point.equals("A")) {
            return 14;
        }
        if(point.equals("2")) {
            return 15;
        }
        else {
            return Integer.parseInt(point);
        }
    }

    public static int GetPointSizeInFive(String point)
    {
        if(point.equals("J")) {
            return 11;
        }
        if(point.equals("Q")) {
            return 12;
        }
        if(point.equals("K")) {
            return 13;
        }
        if(point.equals("A")) {
            return 1;
        }

        else {
            return Integer.parseInt(point);
        }
    }

}
