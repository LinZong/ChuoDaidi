package com.nemesiss.chuodaidi.Game.Model;

import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;

public class ScoreItem
{
    private BasePlayerInformation player;
    private int DeltaScore;
    private int EndTotalScore;

    public ScoreItem()
    {

    }

    public ScoreItem(BasePlayerInformation target, int deltaScore, int endTotalScore)
    {
        player = target;
        DeltaScore = deltaScore;
        EndTotalScore = endTotalScore;
    }

    public BasePlayerInformation getPlayer()
    {
        return player;
    }

    public int getDeltaScore()
    {
        return DeltaScore;
    }

    public int getEndTotalScore()
    {
        return EndTotalScore;
    }

    public void setPlayer(BasePlayerInformation tg)
    {
        player = tg;
    }

    public void setDeltaScore(int deltaScore)
    {
        DeltaScore = deltaScore;
    }

    public void setEndTotalScore(int endTotalScore)
    {
        EndTotalScore = endTotalScore;
    }
}
