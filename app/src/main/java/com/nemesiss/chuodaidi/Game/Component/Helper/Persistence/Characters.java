package com.nemesiss.chuodaidi.Game.Component.Helper.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.HatsuneMiku;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotCharacters.RobotCharactersExport;
import com.nemesiss.chuodaidi.Game.Component.Player.RobotPlayer;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.RobotPlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;
import com.nemesiss.chuodaidi.Game.Model.ScoreItem;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class Characters
{
    public static void PrepareDatabases()
    {
        SQLiteDatabase db = Connector.getDatabase();
        List<RobotPlayerInformation> list = DataSupport.findAll(RobotPlayerInformation.class);
        if(list.isEmpty())
        {
            RobotPlayerInformation[] robots = RobotCharactersExport.AllRobotCharacters;


            for (int i = 0; i < robots.length; i++) {
                ContentValues cv = new ContentValues();
                cv.put("nickName",robots[i].getNickName());
                cv.put("totalScore",robots[i].getTotalScore());
                cv.put("FLAG",robots[i].getFLAG());
                db.insert("RobotPlayerInformation".toLowerCase(),null,cv);
            }
        }

        UserInformation player = Characters.GetPlayer();
        if(player == null) {
            player = new UserInformation();
            player.save();
        }

    }

    public static List<RobotPlayerInformation> GetAllRobots()
    {
        return DataSupport.findAll(RobotPlayerInformation.class);
    }
    public static RobotPlayerInformation GetRobot(String RobotBaseNickName)
    {
        return DataSupport.where("NickName",RobotBaseNickName).findFirst(RobotPlayerInformation.class);
    }
    public static void UpdateRobot(RobotPlayerInformation update)
    {
        ContentValues cv = new ContentValues();
        cv.put("totalScore",update.getTotalScore());
        DataSupport.update(RobotPlayerInformation.class,cv,update.getId());
    }

    public static UserInformation GetPlayer()
    {
        return DataSupport.findFirst(UserInformation.class);
    }

    public static void UpdatePlayer(UserInformation player)
    {
        ContentValues cv = new ContentValues();
        cv.put("totalScore",player.getTotalScore());
        DataSupport.update(UserInformation.class,cv,player.getId());
    }

    public static void UpdatePlayerScores(List<ScoreItem> ThisTurnScore) {
        for (ScoreItem scoreItem : ThisTurnScore) {
            BasePlayerInformation bi = scoreItem.getPlayer();
            if(bi instanceof RobotPlayerInformation) {
                bi.setTotalScore(scoreItem.getEndTotalScore());
                UpdateRobot((RobotPlayerInformation) bi);
            }
            else if(bi instanceof UserInformation) {
                bi.setTotalScore(scoreItem.getEndTotalScore());
                UpdatePlayer((UserInformation) bi);
            }
        }
    }
}
