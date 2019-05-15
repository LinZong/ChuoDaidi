package com.nemesiss.chuodaidi.Android.View;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.R;

import java.util.LinkedList;

public class GameDialogNew extends PopupWindow {

    private View contentView;
    private Context mContext;

    public GameDialogNew(Context context, View view) {
        super(context);
        mContext = context;
        contentView = view;
        setContentView(contentView);

        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // Set this to handle outside close.

        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        setBackgroundDrawable(new ColorDrawable());

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                DimBackground(1f);
            }
        });


    }


    private void DimBackground(float amount)
    {
        LinkedList<Activity> activities = ChuoDaidiActivity.getActivities();
        if(!activities.isEmpty()) {
            Activity top = activities.getLast();
            Window wd = top.getWindow();
            WindowManager.LayoutParams wdlp = wd.getAttributes();
            wdlp.alpha = amount;
            wd.setAttributes(wdlp);
        }
    }

    public void Show()
    {
        this.showAtLocation(contentView,Gravity.CENTER,0,0);
        DimBackground(0.3f);
    }


    public static class Builder
    {

        private FrameLayout layout;

        private Activity activity;
        private BaseGameFragment fragContext;
        private String dialogTitle;
        private String dialogCenterText;
        private String dialogPositiveButtonText = "YES";
        private String dialogNegativeButtonText = "CANCEL";
        private View.OnClickListener dialogPositiveBtnOnClick;
        private View.OnClickListener dialogNegativeBtnOnClick;

        private GameDialogNew instance;

        public Builder with(Activity ctx)
        {
             activity = ctx;
            return this;
        }
        public Builder with(BaseGameFragment ctx)
        {
            fragContext = ctx;
            return this;
        }

        private Context getContext()
        {
            if(activity!=null) return activity;
            else return fragContext.getContext();
        }
        public Builder setTitle(String title)
        {
            dialogTitle = title;
            return this;
        }
        public Builder setText(String text)
        {
            dialogCenterText = text;
            return this;
        }
        public Builder setPositiveButton(String buttonText, View.OnClickListener onClickListener)
        {
            dialogPositiveButtonText = buttonText;
            dialogPositiveBtnOnClick = onClickListener;
            return this;
        }
        public Builder setNegativeButton(String buttonText, View.OnClickListener onClickListener)
        {
            dialogNegativeButtonText = buttonText;
            dialogNegativeBtnOnClick = onClickListener;
            return this;
        }

        public GameDialogNew Build()
        {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.dialog,null);


            instance = new GameDialogNew(getContext(),layout);

            TextView title = layout.findViewById(R.id.Dialog_Title);
            title.setText(dialogTitle);

            TextView innerText = layout.findViewById(R.id.Dialog_InnerText);
            innerText.setText(dialogCenterText);

            Button positive = layout.findViewById(R.id.Dialog_PositiveBtn);
            Button negative = layout.findViewById(R.id.Dialog_NegativeBtn);

            positive.setText(dialogPositiveButtonText);
            negative.setText(dialogNegativeButtonText);

            positive.setOnClickListener(OnClickListenerWrapper(dialogPositiveBtnOnClick));
            negative.setOnClickListener(OnClickListenerWrapper(dialogNegativeBtnOnClick));
            return instance;

        }

        private View.OnClickListener OnClickListenerWrapper(View.OnClickListener userDefined)
        {
            return v -> {
                if(userDefined!=null)
                    userDefined.onClick(v);
                if(instance!=null) {
                    instance.dismiss();
                }
            };
        }
    }
}
