package com.nemesiss.chuodaidi.Android.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.nemesiss.chuodaidi.Game.Model.ScoreItem;
import com.nemesiss.chuodaidi.databinding.SingleStoryBoardItemBinding;

import java.util.List;

public class ScoreItemAdapter extends RecyclerView.Adapter<ScoreItemAdapter.ScoreItemViewHolder>
{

    private List<ScoreItem> scoreItemList;

    public ScoreItemAdapter(List<ScoreItem> scoreItems)
    {
        scoreItemList = scoreItems;
    }

    @NonNull
    @Override
    public ScoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return ScoreItemViewHolder.Create(LayoutInflater.from(viewGroup.getContext()),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreItemViewHolder scoreItemViewHolder, int i)
    {
        scoreItemViewHolder.BindTo(scoreItemList.get(i));
    }

    @Override
    public int getItemCount()
    {
        return scoreItemList.size();
    }

    static class ScoreItemViewHolder extends RecyclerView.ViewHolder
    {
        private SingleStoryBoardItemBinding mBinding;

        public ScoreItemViewHolder(@NonNull SingleStoryBoardItemBinding binding)
        {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void BindTo(ScoreItem item)
        {
            mBinding.setItem(item);
            mBinding.executePendingBindings();
        }

        public static ScoreItemViewHolder Create(LayoutInflater inflater,ViewGroup viewGroup)
        {
            return new ScoreItemViewHolder(SingleStoryBoardItemBinding.inflate(inflater,viewGroup,false));
        }
    }
}