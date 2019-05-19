package com.nemesiss.chuodaidi.Android.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.databinding.SingleSelectRobotItemBinding;

import java.util.List;

public class RobotSelectionAdapter extends BlackSpinnerAdapter<RobotSelectionAdapter.PlayerInfoViewHolder>
{

    List<BasePlayerInformation> robotPlayers;

    public RobotSelectionAdapter(List<BasePlayerInformation> _robots)
    {
        robotPlayers = _robots;
    }

    @NonNull
    @Override
    public PlayerInfoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        PlayerInfoViewHolder vh  =  PlayerInfoViewHolder.Create(LayoutInflater.from(viewGroup.getContext()), viewGroup);
        View v = vh.itemView;
        if(i == 0)
        {
            FixPadding(v);
        }
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull PlayerInfoViewHolder playerInfoViewHolder, int i)
    {
        playerInfoViewHolder.BindTo(robotPlayers.get(i));
        playerInfoViewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = playerInfoViewHolder.getAdapterPosition();
                if (mListener != null)
                {
                    mListener.handle(pos);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return robotPlayers.size();
    }

    static class PlayerInfoViewHolder extends RecyclerView.ViewHolder
    {
        SingleSelectRobotItemBinding mBinding;


        private PlayerInfoViewHolder(@NonNull SingleSelectRobotItemBinding binding)
        {
            super(binding.getRoot());
            mBinding = binding;
        }

        public static PlayerInfoViewHolder Create(LayoutInflater inflater, ViewGroup parent)
        {
            return new PlayerInfoViewHolder(SingleSelectRobotItemBinding.inflate(inflater, parent, false));
        }

        public void BindTo(BasePlayerInformation bpi)
        {
            mBinding.setRobot(bpi);
            mBinding.executePendingBindings();
        }
    }
}
