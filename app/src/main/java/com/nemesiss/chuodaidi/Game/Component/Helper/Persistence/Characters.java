package com.nemesiss.chuodaidi.Game.Component.Helper.Persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.nemesiss.chuodaidi.Android.Application.ChuoDaidiApplication;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;

public class Characters
{
    private static final String APPSETTING_RW_FLAG = "APPSETTING_RW_FLAG";

    private static final String USERINFORMATION_RW_FLAG = "USERINFORMATION_RW_FLAG";

    public static Context context;
    public static SharedPreferences sharedPreferences;

    static
    {
        context = ChuoDaidiApplication.getContext();
        sharedPreferences = context.getSharedPreferences(APPSETTING_RW_FLAG, Context.MODE_PRIVATE);
    }


    public static UserInformation LoadUserInformation()
    {
        String val = sharedPreferences.getString(USERINFORMATION_RW_FLAG,null);
        if(TextUtils.isEmpty(val)) {
            return new UserInformation();
        }
        UserInformation model = new Gson().fromJson(val,UserInformation.class);
        return model;
    }

    public static void SaveUserInformation(UserInformation NewInformation)
    {
        ChuoDaidiApplication.setPlayerInformation(NewInformation);
        String serialize = new Gson().toJson(NewInformation,UserInformation.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERINFORMATION_RW_FLAG,serialize);
        editor.apply();
    }

    public static BasePlayerInformation GetRobotInformation(String nickname,Class< ? extends BasePlayerInformation> robotClazz)
    {
        String clazzName = robotClazz.getSimpleName();
        String jsonVal = sharedPreferences.getString(nickname,null);
        if(!TextUtils.isEmpty(jsonVal))
        {
            return new Gson().fromJson(jsonVal,robotClazz);
        }
        else {
            try
            {
                BasePlayerInformation createdInfo = robotClazz.newInstance();
                SaveRobotInformation(createdInfo);
                return createdInfo;
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void SaveRobotInformation(BasePlayerInformation robots)
    {
        String serialize = new Gson().toJson(robots,robots.getClass());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(robots.getNickName(),serialize);
        editor.apply();
    }
}
