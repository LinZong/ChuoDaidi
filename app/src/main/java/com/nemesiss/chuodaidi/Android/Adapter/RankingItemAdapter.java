package com.nemesiss.chuodaidi.Android.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.databinding.SingleTrendRankingItemBinding;

import java.util.List;

public class RankingItemAdapter extends RecyclerView.Adapter<RankingItemAdapter.RankingItemHolder>
{

    private List<BasePlayerInformation> userInformationList;
    public RankingItemAdapter(List<BasePlayerInformation> users)
    {
        userInformationList = users;
    }

    @NonNull
    @Override
    public RankingItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return RankingItemHolder.Create(LayoutInflater.from(viewGroup.getContext()),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingItemHolder rankingItemHolder, int i)
    {
        rankingItemHolder.BindTo(userInformationList.get(i));
    }

    @Override
    public int getItemCount()
    {
        return userInformationList.size();
    }

    static class RankingItemHolder extends RecyclerView.ViewHolder
    {

        private SingleTrendRankingItemBinding mBinding;
        private RankingItemHolder(@NonNull SingleTrendRankingItemBinding itemView)
        {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        void BindTo(BasePlayerInformation viewItem)
        {
            mBinding.setItem(viewItem);
        }

        static RankingItemHolder Create(LayoutInflater inflater, ViewGroup parent)
        {
            return new RankingItemHolder(SingleTrendRankingItemBinding.inflate(inflater,parent,false));
        }
    }
}
