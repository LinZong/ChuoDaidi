package com.nemesiss.chuodaidi.Android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;

public class ChuoDaidiActivity extends AppCompatActivity
{
    private static float Scale;
    private static LinkedList<Activity> activities = new LinkedList<>();

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scale = getResources().getDisplayMetrics().density;
        activities.add(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        activities.remove(this);
    }

    public static float getScale() {
        return Scale;
    }

    public static LinkedList<Activity> getActivities()
    {
        return activities;
    }
}
