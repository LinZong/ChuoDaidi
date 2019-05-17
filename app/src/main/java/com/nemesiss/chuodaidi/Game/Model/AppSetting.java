package com.nemesiss.chuodaidi.Game.Model;

import com.nemesiss.chuodaidi.Game.Model.Annotation.Description;

import java.io.Serializable;

public class AppSetting implements Serializable
{

    @Description("显示出牌特效")
    private int ShowCardGraphicEffects = 0;

    @Description("显示出牌音效")
    private int ShowCardAudioEffects = 0;


    public int getShowCardAudioEffects() {
        return ShowCardAudioEffects;
    }

    public int getShowCardGraphicEffects() {
        return ShowCardGraphicEffects;
    }

    public void setShowCardAudioEffects(int showCardAudioEffects) {
        ShowCardAudioEffects = showCardAudioEffects;
    }

    public void setShowCardGraphicEffects(int showCardGraphicEffects) {
        ShowCardGraphicEffects = showCardGraphicEffects;
    }
}
