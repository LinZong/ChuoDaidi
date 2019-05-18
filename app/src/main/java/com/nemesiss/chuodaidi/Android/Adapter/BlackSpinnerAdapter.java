package com.nemesiss.chuodaidi.Android.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BlackSpinnerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{

    protected OnChildItemClickedListener mListener;

    public interface OnChildItemClickedListener
    {
        void handle(int position);
    }

    public void setOnChildItemClickedListener(OnChildItemClickedListener listener)
    {
        mListener = listener;
    }

    public OnChildItemClickedListener getOnChildItemClickedListener() {
        return mListener;
    }


    public static void FixPadding(View view)
    {
        int left = view.getPaddingLeft(),
                top = view.getPaddingBottom(),
                right = view.getPaddingRight(),
                bottom = view.getPaddingBottom();
        view.setPadding(left,top,right,bottom);
    }

}
