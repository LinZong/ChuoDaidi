package com.nemesiss.chuodaidi.Android.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.R;

public class GameDialog
{

    public static final int REQUEST_CUSTOM_DIALOG_PERMISSION = 10;

    public static class Builder
    {
        private WindowManager wm;
        private FrameLayout layout;

        private BaseGameFragment context;
        private String dialogTitle;
        private String dialogCenterText;
        private String dialogPositiveButtonText = "YES";
        private String dialogNegativeButtonText = "CANCEL";
        private View.OnClickListener dialogPositiveBtnOnClick;
        private View.OnClickListener dialogNegativeBtnOnClick;

        public Builder with(BaseGameFragment ctx)
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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(!Settings.canDrawOverlays(context.getContext()))
                {
                    context.PendingDialogBuilder = this;

                    Toast.makeText(context.getContext(),"To show custom dialog on Oreo or higher, you need to grant overlay window permissions.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + context.getContext().getPackageName()));
                    context.startActivityForResult(intent,REQUEST_CUSTOM_DIALOG_PERMISSION);
                    return;
                }
            }

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            wm = (WindowManager) context.getContext().getSystemService(Context.WINDOW_SERVICE);

            // 设置LayoutParam

            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.format = PixelFormat.TRANSLUCENT;

            lp.x = 0;
            lp.y = 0;

            lp.gravity = Gravity.START|Gravity.TOP;

            // Android 8.0 permissions resolve.
            if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O)
            {
                lp.type =  WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
            else
            {
                lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            lp.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;

            layout = (FrameLayout) LayoutInflater.from(context.getContext()).inflate(R.layout.dialog,null);

            // 拦截返回键请求
            layout.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK) {
                        wm.removeViewImmediate(layout);
                    }
                    return true;
                }
            });

            TextView title = layout.findViewById(R.id.Dialog_Title);
            title.setText(dialogTitle);

            TextView innerText = layout.findViewById(R.id.Dialog_InnerView);
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
