package com.nemesiss.chuodaidi.Game.Component.Trending;

import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;

import java.util.Comparator;

public class TrendingComparator implements Comparator<BasePlayerInformation>
{

    @Override
    public int compare(BasePlayerInformation o1, BasePlayerInformation o2)
    {
        if(o1.TotalScore > o2.TotalScore)
        {
            return -1;
        }
        if(o1.TotalScore == o2.TotalScore)
        {
            return 0;
        }
        else return 1;
    }
}
