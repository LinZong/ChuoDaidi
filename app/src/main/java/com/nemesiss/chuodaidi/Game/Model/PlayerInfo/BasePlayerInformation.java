package com.nemesiss.chuodaidi.Game.Model.PlayerInfo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class BasePlayerInformation extends DataSupport implements Serializable
{
    public int Id;
    public String NickName;
    public int TotalScore = 3000;

    public BasePlayerInformation() {
        if(NickName == null) {
            NickName = this.getClass().getSimpleName();
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
