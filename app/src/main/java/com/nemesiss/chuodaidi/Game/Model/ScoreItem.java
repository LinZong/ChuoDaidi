package com.nemesiss.chuodaidi.Game.Model;

public class ScoreItem
{
    private String NickName;
    private int DeltaScore;
    private int EndTotalScore;

    public ScoreItem()
    {

    }

    public ScoreItem(String nickName, int deltaScore, int endTotalScore)
    {
        NickName = nickName;
        DeltaScore = deltaScore;
        EndTotalScore = endTotalScore;
    }

    public String getNickName()
    {
        return NickName;
    }

    public int getDeltaScore()
    {
        return DeltaScore;
    }

    public int getEndTotalScore()
    {
        return EndTotalScore;
    }

    public void setNickName(String nickName)
    {
        NickName = nickName;
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
