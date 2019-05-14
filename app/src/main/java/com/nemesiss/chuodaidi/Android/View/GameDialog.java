package com.nemesiss.chuodaidi.Android.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nemesiss.chuodaidi.Android.Activity.ChuoDaidiActivity;
import com.nemesiss.chuodaidi.R;

public class GameDialog
{


    public static class Builder
    {
        private WindowManager wm;
        private FrameLayout layout;

        private ChuoDaidiActivity context;
        private String dialogTitle;
        private String dialogCenterText;
        private String dialogPositiveButtonText = "YES";
        private String dialogNegativeButtonText = "CANCEL";
        private View.OnClickListener dialogPositiveBtnOnClick;
        private View.OnClickListener dialogNegativeBtnOnClick;

        public Builder with(ChuoDaidiActivity ctx)
        {
            context = ctx;
            return this;
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
        public void Build()
        {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            wm = context.getWindowManager();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;

            layout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.dialog,null);

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

            wm.addView(layout,lp);
        }

        private View.OnClickListener OnClickListenerWrapper(View.OnClickListener userDefined)
        {
            return v -> {
                if(userDefined!=null)
                    userDefined.onClick(v);
                wm.removeViewImmediate(layout);
            };
        }
    }

}
