package com.nemesiss.chuodaidi.Game.Component.Card.Score;

import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;

public class ScoreCalculator
{
    public static int[] GetFinalScore(int... numberOfCards)
    {
        int[] resultScore = new int[4];
        resultScore[0] = numberOfCards[1] + numberOfCards[2] + numberOfCards[3] - 3 * numberOfCards[0];//玩家得分
        resultScore[1] = numberOfCards[0] + numberOfCards[2] + numberOfCards[3] - 3 * numberOfCards[1];//下家得分
        resultScore[2] = numberOfCards[0] + numberOfCards[1] + numberOfCards[3] - 3 * numberOfCards[2];//对家得分
        resultScore[3] = numberOfCards[0] + numberOfCards[1] + numberOfCards[2] - 3 * numberOfCards[3];//上家得分
        return resultScore;
    }
    public static void Penalty()
    {
        UserInformation player = Characters.GetPlayer();
        int penalty = player.getTotalScore() - 20;
        player.setTotalScore(penalty);
        Characters.UpdatePlayer(player);
    }
}
