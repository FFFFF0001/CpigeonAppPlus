package com.cpigeon.app.modular.usercenter.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseLazyLoadFragment;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.view.activity.ScoreActivity;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;
import com.cpigeon.app.modular.usercenter.view.adapter.ScoreAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chenshuai on 2017/4/13.
 */


public class UserScoreSub2Fragment extends BaseLazyLoadFragment implements IScoreView.IScoreSub2View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    int pageIndex = 1, pageSize = 10;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ScoreAdapter mAdapter;
    boolean canLoadMore = true;//标记是否还有更多数据

    @Override
    protected void initView(View view) {
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        mAdapter = new ScoreAdapter(null);
        mAdapter.setOnLoadMoreListener(this, recyclerview);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.setEnableLoadMore(true);
        ((ScoreActivity) getActivity()).getPresenter().attachScoreSub2View(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_user_score_s2;
    }

    @Override
    protected void lazyLoad() {
        loadMore();
    }

    private void loadMore() {
        ((ScoreActivity) getActivity()).getPresenter().loadScoreRecord();
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 加载数据的回调
     *
     * @param data
     */
    @Override
    public void loadScoreRecord(List<UserScore> data) {
        mAdapter.addData(data);
        mAdapter.loadMoreComplete();

        canLoadMore = data != null && data.size() == pageSize;
        Logger.d("canLoadMore=" + canLoadMore);
        mAdapter.setEnableLoadMore(canLoadMore);
        if (canLoadMore) {
            pageIndex++;
        } else {
            mAdapter.loadMoreEnd(true);//隐藏"加载更多"视图
        }
    }

    @Override
    public void loadScoreRecordError() {
        mAdapter.loadMoreFail();
    }

    @Override
    public void onLoadMoreRequested() {
        if (canLoadMore) {
            loadMore();
        }
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setEnableLoadMore(canLoadMore);
            }
        }, 500);
    }
}