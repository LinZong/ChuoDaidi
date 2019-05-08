package com.nemesiss.chuodaidi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout SelfPokeContainer;
    private LinearLayout SelfShowPokeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SelfPokeContainer = findViewById(R.id.SelfPokeCollection);
        SelfShowPokeContainer = findViewById(R.id.SelfShowPokeCollection);
    }


    private void LoadPokes()
    {

    }


}
