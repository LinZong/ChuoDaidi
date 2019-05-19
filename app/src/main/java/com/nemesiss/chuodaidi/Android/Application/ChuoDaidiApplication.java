package com.nemesiss.chuodaidi.Android.Application;

import android.app.Application;
import android.content.Context;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;

public class ChuoDaidiApplication extends Application {

    private static Context context;
    private static UserInformation playerInformation;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        playerInformation = Characters.LoadUserInformation();
    }

    public static Context getContext() {
        return context;
    }

    public static UserInformation getPlayerInformation()
    {
        return playerInformation;
    }

    public static void setPlayerInformation(UserInformation playerInformation)
    {
        ChuoDaidiApplication.playerInformation = playerInformation;
    }
}
