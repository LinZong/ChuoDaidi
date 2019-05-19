package com.nemesiss.chuodaidi.Game.Component.Card.Score;

public class ScoreCalculator
{
    public static int[] GetFinalScore(int... numberOfCards)
    {
        int[] resultScore = new int[4];
        for(int i=0;i<4;i++)
        {
            if(numberOfCards[i]<8)
                numberOfCards[i]=numberOfCards[i];
            else if(8<=numberOfCards[i]&&numberOfCards[i]<10)
                numberOfCards[i]=numberOfCards[i]*2;
            else if(10<=numberOfCards[i]&&numberOfCards[i]<13)
                numberOfCards[i]=numberOfCards[i]*3;
            else if(numberOfCards[i]==13)
                numberOfCards[i]=numberOfCards[i]*4;
        }
        resultScore[0] = numberOfCards[1] + numberOfCards[2] + numberOfCards[3] - 3 * numberOfCards[0];//玩家得分
        resultScore[1] = numberOfCards[0] + numberOfCards[2] + numberOfCards[3] - 3 * numberOfCards[1];//下家得分
        resultScore[2] = numberOfCards[0] + numberOfCards[1] + numberOfCards[3] - 3 * numberOfCards[2];//对家得分
        resultScore[3] = numberOfCards[0] + numberOfCards[1] + numberOfCards[2] - 3 * numberOfCards[3];//上家得分
        return resultScore;
    }
}
