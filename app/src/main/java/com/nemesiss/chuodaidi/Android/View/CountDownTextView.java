package com.nemesiss.chuodaidi.Android.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownTextView extends android.support.v7.widget.AppCompatTextView
{

    private long Interval;
    private int Begin;
    private Runnable callback;
    private boolean IsRunning = false;
    private ChuoDaidiActivity CurrentActivity;


    private Timer timer;
    private int Counter;

    public CountDownTextView(Context context)
    {
        super(context);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CountDownTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void InitCountDown(long interval, int begin, @NonNull Runnable cb, ChuoDaidiActivity currentActivity)
    {
        Interval = interval;
        Begin = begin;
        callback = cb;
        setText(String.valueOf(Begin));
        CurrentActivity = currentActivity;

    }

    private void ResetFlags()
    {
        timer = new Timer();
        IsRunning = true;
        Counter = Begin;
        setText(String.valueOf(Counter));
    }

    public synchronized void Start()
    {
        if(!IsRunning)
        {
            ResetFlags();

            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    Counter--;
                    if(Counter == -1)
                    {
                        callback.run();
                        IsRunning = false;
                        timer.cancel();
                    }
                    else {
                        CurrentActivity.runOnUiThread(() -> CountDownTextView.this.setText(String.valueOf(Counter)));
                    }
                }
            },0,1000);
        }
    }

    public synchronized void Cancel()
    {
        if(IsRunning)
        {
            IsRunning = false;
            timer.cancel();
        }
    }

    public boolean GetIsRunning()
    {
        return IsRunning;
    }
}
