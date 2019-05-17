package com.nemesiss.chuodaidi.Android.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.Android.Adapter.FakeSpinnerAdapter;
import com.nemesiss.chuodaidi.R;

public class BlackSpinner extends RelativeLayout {

    private RelativeLayout rootView;
    private LinearLayoutManager layoutManager;
    private FakeSpinnerAdapter mAdapter;

    @BindView(R.id.Spinner_ItemList)
    private RecyclerView SpinnerItemList;

    @BindView(R.id.Spinner_SingleText)
    private TextView SelectedItem;

    @BindView(R.id.Spinner_ExpandItemGroupArrow)
    private ImageView arrow;

    public BlackSpinner(Context context) {
        super(context);
        InitInnerComponents();
    }

    public BlackSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitInnerComponents();
   }

    public BlackSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitInnerComponents();
    }

    private void InitInnerComponents()
    {
        rootView = (RelativeLayout) inflate(getContext(), R.layout.black_spinner,this);
        ButterKnife.bind(this,rootView);

        layoutManager = new LinearLayoutManager(getContext());
        SpinnerItemList.setLayoutManager(layoutManager);


    }

    public void setAdapter(FakeSpinnerAdapter adapter)
    {
        mAdapter = adapter;
        SpinnerItemList.setAdapter(mAdapter);
    }

    private void HandleArrowClickedAnimation()
    {

    }
}
