package com.cpigeon.app.modular.usercenter.presenter;

import android.support.annotation.NonNull;

import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.IView;
import com.cpigeon.app.modular.usercenter.model.bean.UserScore;
import com.cpigeon.app.modular.usercenter.model.dao.IScoreDao;
import com.cpigeon.app.modular.usercenter.model.daoimpl.ScoreDaoImpl;
import com.cpigeon.app.modular.usercenter.view.activity.viewdao.IScoreView;

import java.util.List;

/**
 * Created by chenshuai on 2017/4/13.
 */

public class ScorePresenter extends BasePresenter<IScoreView, IScoreDao> {

    public ScorePresenter(IScoreView mView) {
        super(mView);
    }

    @Override
    protected IScoreDao initDao() {
        return new ScoreDaoImpl();
    }

    IBaseDao.OnCompleteListener<List<UserScore>> onCompleteListener = new IBaseDao.OnCompleteListener<List<UserScore>>() {
        @Override
        public void onSuccess(final List<UserScore> data) {
            if (isDetached()) return;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAttached()) {
                        if (mView.isRefreshing()) {
                            mView.hideRefreshLoading();
                        } else if (mView.isMoreDataLoading()) {
                            mView.loadMoreComplete();
                        }
                        mView.showMoreData(data);
                    }
                }
            }, 300);
        }

        @Override
        public void onFail(String msg) {
            if (isDetached()) return;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAttached()) {
                        if (mView.isRefreshing()) {
                            mView.hideRefreshLoading();
                            mView.showTips("获取鸽币记录失败", IView.TipType.View);
                        } else if (mView.isMoreDataLoading()) {
                            mView.loadMoreFail();
                        }
                    }
                }
            }, 300);
        }
    };

    public void loadScoreRecord() {
        if (isDetached()) return;
        if (mView.getPageIndex() == 1) mView.showRefreshLoading();
        mDao.loadScoreRecord(mView.getPageIndex(), mView.getPageSize(), onCompleteListener);
    }
}
