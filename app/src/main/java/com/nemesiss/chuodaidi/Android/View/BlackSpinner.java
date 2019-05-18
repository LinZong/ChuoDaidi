package com.nemesiss.chuodaidi.Android.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.nemesiss.chuodaidi.Android.Adapter.BlackSpinnerAdapter;
import com.nemesiss.chuodaidi.Android.Adapter.FakeSpinnerAdapter;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.BlackSpinner.BlackSpinnerExpandAnimation;
import com.nemesiss.chuodaidi.R;

public class BlackSpinner extends RelativeLayout {

    private RelativeLayout rootView;
    private LinearLayoutManager layoutManager;
    private BlackSpinnerAdapter mAdapter;

    RecyclerView SpinnerItemList;
    FrameLayout SelectedItem;
    ImageView arrow;

    Drawable arrowDrawable;

    private boolean IsExpanded = false;

    private int SelectedItemListOriginalHeight = 0;

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
        SpinnerItemList = rootView.findViewById(R.id.Spinner_ItemList);
        SelectedItem = rootView.findViewById(R.id.Spinner_SelectedItem);
        arrow = rootView.findViewById(R.id.Spinner_ExpandItemGroupArrow);

        SelectedItem.setOnClickListener((v) -> AnimateListAndArrow());
        arrow.setOnClickListener((v) -> AnimateListAndArrow());

        // Measure SpinnerItemList Original Height.
        SpinnerItemList.post(() -> {
            SelectedItemListOriginalHeight = SpinnerItemList.getHeight();
            SpinnerItemList.setVisibility(GONE);
        });

        arrowDrawable = arrow.getDrawable();
        layoutManager = new LinearLayoutManager(getContext());
        SpinnerItemList.setLayoutManager(layoutManager);
    }

    public void setAdapter(BlackSpinnerAdapter adapter)
    {
        mAdapter = adapter;
        SpinnerItemList.setAdapter(mAdapter);

        //拿到原先设置的监听器，进行包装后注入Adapter
        BlackSpinnerAdapter.OnChildItemClickedListener oldListener = mAdapter.getOnChildItemClickedListener();

        mAdapter.setOnChildItemClickedListener(position -> {
            // work around for off-list response ( position is -1 )
            if(position!=-1) {
                LoadSelectedViewToContainer(position);
                if(oldListener!=null) {
                    oldListener.handle(position);
                }
            }
            AnimateListAndArrow();
        });
        SpinnerItemList.post(() -> {
            if(SpinnerItemList.getChildCount() > 0) {

                LoadSelectedViewToContainer(0);
            }
        });

    }

    private void LoadSelectedViewToContainer(int position)
    {
        RecyclerView.ViewHolder vh = mAdapter.onCreateViewHolder(SelectedItem,position);
        mAdapter.onBindViewHolder(vh,position);

        View view = vh.itemView;

        SelectedItem.removeAllViews();
        FakeSpinnerAdapter.FixPadding(view);

        SelectedItem.addView(vh.itemView);
    }

    private synchronized void AnimateListAndArrow()
    {
        RotateAnimation animation = new RotateAnimation(!IsExpanded ? 0 : 180, !IsExpanded ? 180 : 0, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        arrow.startAnimation(animation);

        if(!IsExpanded) {
            SpinnerItemList.setVisibility(VISIBLE);
        }
        int currHeight = !IsExpanded ? 0 : SelectedItemListOriginalHeight;
        int finalHeight = !IsExpanded ? SelectedItemListOriginalHeight : 0;
        ValueAnimator valA = BlackSpinnerExpandAnimation.Expand(SpinnerItemList,currHeight,finalHeight,300);
        valA.start();

        IsExpanded = !IsExpanded;
    }
}
