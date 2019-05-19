package com.nemesiss.chuodaidi.Game.Model.PlayerInfo;

import java.io.Serializable;

public class BasePlayerInformation implements Serializable
{
    protected String NickName;
    protected int TotalScore = 3000;

    public BasePlayerInformation() {
        if(NickName == null) {
            NickName = this.getClass().getSimpleName();
        }
    }

    public String getNickName()
    {
        return NickName;
    }

    public void setNickName(String nickName)
    {
        NickName = nickName;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        TotalScore = totalScore;
    }

}
