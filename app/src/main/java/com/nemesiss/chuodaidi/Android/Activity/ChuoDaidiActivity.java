package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

public class ChuoDaidiActivity extends AppCompatActivity
{
    private static float Scale;

    @Override
    public void onCreate( Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Scale = getResources().getDisplayMetrics().density;
    }

    public static float getScale() {
        return Scale;
    }

}
