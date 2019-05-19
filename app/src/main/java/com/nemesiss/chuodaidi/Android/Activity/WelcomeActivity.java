package com.nemesiss.chuodaidi.Android.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.ikidou.fragmentBackHandler.BackHandlerHelper;
import com.nemesiss.chuodaidi.Android.Adapter.RankingItemAdapter;
import com.nemesiss.chuodaidi.Android.Fragment.BaseGameFragment;
import com.nemesiss.chuodaidi.Android.Fragment.WelcomeFragment;
import com.nemesiss.chuodaidi.Android.Utils.AppUtil;
import com.nemesiss.chuodaidi.Android.View.GameDialogNew;
import com.nemesiss.chuodaidi.Android.View.ViewProcess.AnimateWelcomeTitle;
import com.nemesiss.chuodaidi.Game.Component.Helper.Persistence.Characters;
import com.nemesiss.chuodaidi.Game.Component.Trending.TrendingComparator;
import com.nemesiss.chuodaidi.Game.Model.ObservableModel.UserNickNameObservable;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.BasePlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.RobotPlayerInformation;
import com.nemesiss.chuodaidi.Game.Model.PlayerInfo.UserInformation;
import com.nemesiss.chuodaidi.R;
import com.nemesiss.chuodaidi.databinding.PlayerInfoSettingBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WelcomeActivity extends ChuoDaidiActivity
{

    @BindView(R.id.WelcomeFuncFragmentContainer)
    FrameLayout container;

    public BaseGameFragment[] fragments;
    public FragmentManager fm;
    private int CurrentFragmentTag = -1;

    // 用户名设置相关
    private UserNickNameObservable userNickNameObservable = new UserNickNameObservable();
    private PlayerInfoSettingBinding BindingUserNickNameSetting;

    //排行榜相关
    private RankingItemAdapter rankingItemAdapter;
    private View TrendingView;
    private RecyclerView TrendingRecyclerView;
    private GameDialogNew TrendingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        UserInformation player = Characters.GetPlayer();
        userNickNameObservable.NickName.set(player.getNickName());

        TrendingView = LayoutInflater.from(this).inflate(R.layout.trending_board, null);
        TrendingRecyclerView = TrendingView.findViewById(R.id.TrendingBoardRecyclerView);

        fragments = new BaseGameFragment[6];
        fm = getSupportFragmentManager();

        AppUtil.LoadFragmentToActivity(WelcomeActivity.this, R.id.WelcomeFuncFragmentContainer, new WelcomeFragment());
    }

    @Override
    public void onBackPressed()
    {
        if (!BackHandlerHelper.handleBackPress(this))
        {
            super.onBackPressed();
        }
    }


    @OnClick({R.id.Setting_NickName})
    public void SettingNickName()
    {
        BindingUserNickNameSetting = PlayerInfoSettingBinding.inflate(LayoutInflater.from(this));
        BindingUserNickNameSetting.setUser(userNickNameObservable);
        View view = BindingUserNickNameSetting.getRoot();
        new GameDialogNew.Builder()
                .with(this)
                .setTitle("设置玩家昵称")
                .setPositiveButton("确认", this::HandleUserNickNameUpdate)
                .setNegativeButton("取消", null)
                .Build(view)
                .Show();
    }

    private void HandleUserNickNameUpdate(View view)
    {
        String NewNickName = userNickNameObservable.NickName.get();
        UserInformation player = Characters.GetPlayer();
        player.setNickName(NewNickName);
        Characters.UpdatePlayer(player);
    }


    @OnClick({R.id.Setting_Trending})
    public void SettingTrending(View v)
    {
        if (TrendingDialog != null)
        {
            ((FrameLayout) (TrendingDialog.getContentView().findViewById(R.id.Dialog_InnerView))).removeView(TrendingView);
        }

        List<RobotPlayerInformation> robots = Characters.GetAllRobots();
        UserInformation user = Characters.GetPlayer();

        List<BasePlayerInformation> all = new ArrayList<>();
        all.addAll(robots);
        all.add(user);

        Collections.sort(all, new TrendingComparator());
        rankingItemAdapter = new RankingItemAdapter(all);

        TrendingRecyclerView.setLayoutManager(new LinearLayoutManager(TrendingView.getContext()));
        TrendingRecyclerView.setAdapter(rankingItemAdapter);

        TrendingDialog = new GameDialogNew.Builder()
                .with(this)
                .setTitle("排行榜")
                .setPositiveButton("确定", null)
                .setNegativeButton("返回", null)
                .Build(TrendingView)
                .Show();

    }
}
