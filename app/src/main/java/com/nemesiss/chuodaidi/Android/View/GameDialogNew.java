package com.nemesiss.chuodaidi.Android.View;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.R;

public class GameDialogNew extends PopupWindow {

    private FrameLayout contentView;
    private Context mContext;
    private BaseGameFragment parent;

    public GameDialogNew(BaseGameFragment context, FrameLayout view) {
        super(context.getContext());

        parent = context;
        mContext = context.getContext();
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
        Window parentWindow = parent.getAttachedActivity().getWindow();
        WindowManager.LayoutParams lp = parentWindow.getAttributes();
        lp.alpha = amount;
        parentWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        parentWindow.setAttributes(lp);
    }

    public void Show()
    {

        this.showAtLocation(contentView,Gravity.CENTER,0,0);
        DimBackground(0.3f);
    }


    public static class Builder
    {

        private FrameLayout layout;

        private BaseGameFragment fragContext;
        private String dialogTitle;
        private String dialogCenterText;
        private String dialogPositiveButtonText = "YES";
        private String dialogNegativeButtonText = "CANCEL";
        private View.OnClickListener dialogPositiveBtnOnClick;
        private View.OnClickListener dialogNegativeBtnOnClick;

        private GameDialogNew instance;

        public Builder with(BaseGameFragment ctx)
        {
            fragContext = ctx;
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

        public GameDialogNew Build()
        {
            FrameLayout layout = (FrameLayout) LayoutInflater.from(fragContext.getContext()).inflate(R.layout.dialog,null);


            instance = new GameDialogNew(fragContext,layout);

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
