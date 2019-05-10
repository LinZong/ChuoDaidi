package com.nemesiss.chuodaidi.Game.Component.Player;

import android.support.annotation.NonNull;
import com.nemesiss.chuodaidi.Game.Model.Card;

import java.util.List;

public interface Player {
    List<Card> GetHandCards();// 获取手中剩余全部牌
    void InitSetHandCards(@NonNull List<Card> InitHandCards);// 开局时候初始化手牌
    void ShowCard(List<Integer> CardIndex); // 出牌
    void NotifyTakeTurn();// 告知Player轮到他
    void HandleTakeTurn();// Player处理轮到他
}
