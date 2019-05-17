package com.nemesiss.chuodaidi.Android.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nemesiss.chuodaidi.R;

import java.util.List;

public class FakeSpinnerAdapter extends RecyclerView.Adapter<FakeSpinnerAdapter.SpinnerItemHolder> {

    private List<String> spinners;
    private OnChildItemClickedListener mListener;

    public FakeSpinnerAdapter(List<String> content)
    {
        spinners = content;
    }

    @NonNull
    @Override
    public SpinnerItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.test_single_spinner_item,viewGroup,false);
        return new SpinnerItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerItemHolder spinnerItemHolder, int i) {

        spinnerItemHolder.inner.setText(spinners.get(i));
        spinnerItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) {
                    mListener.handle(spinnerItemHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return spinners.size();
    }

    class SpinnerItemHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.Spinner_SingleText)
        TextView inner;
        public SpinnerItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public interface OnChildItemClickedListener
    {
        void handle(int position);
    }

    public void setOnChildItemClickedListener(OnChildItemClickedListener listener)
    {
        mListener = listener;
    }
}
