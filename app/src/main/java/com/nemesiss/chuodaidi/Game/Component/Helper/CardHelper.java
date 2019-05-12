package com.nemesiss.chuodaidi.Game.Component.Helper;

import com.nemesiss.chuodaidi.Game.Model.Card;

import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
}
