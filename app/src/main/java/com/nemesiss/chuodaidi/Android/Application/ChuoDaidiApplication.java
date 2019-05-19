package com.nemesiss.chuodaidi.Android.Application;

import android.app.Application;
import android.content.Context;
import com.nemesiss.chuodaidi.Game.Component.Helper.GameHelper;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class ChuoDaidiApplication extends Application {

    private static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        Characters.PrepareDatabases();
    }

    public static Context getContext() {
        return context;
    }


}
