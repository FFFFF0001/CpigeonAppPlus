package com.cpigeon.app.modular.matchlive.presenter;

import android.os.Handler;

import com.cpigeon.app.modular.matchlive.model.bean.MatchInfo;
import com.cpigeon.app.modular.matchlive.model.dao.IMatchInfo;
import com.cpigeon.app.modular.matchlive.model.daoimpl.MatchInfoImpl;
import com.cpigeon.app.modular.matchlive.view.fragment.IMatchSubView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MatchLiveSubPre {
    private IMatchSubView iMatchSubView;
    private IMatchInfo iMatchInfo;
    private Handler mHandler = new Handler();

    public MatchLiveSubPre(IMatchSubView iMatchSubView) {
        this.iMatchSubView = iMatchSubView;
        this.iMatchInfo = new MatchInfoImpl();
    }

    public void loadXHData(final int type) {
        // 0 加载协会数据，显示
        // 1 加载协会数据，不显示
        iMatchSubView.showLoading();
        iMatchInfo.loadXHDatas(new IMatchInfo.OnLoadCompleteListener() {
            @Override
            public void loadSuccess(final List<MatchInfo> matchInfoList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iMatchSubView.hideLoading();
                        iMatchSubView.showXHData(matchInfoList, type);
                    }
                });
            }

            @Override
            public void loadFailed(String msg) {

            }
        });


    }

    public void loadGPData(final int type) {
        // 0 加载公棚数据，显示
        // 1 加载公棚数据，不显示
        iMatchSubView.showLoading();
        iMatchInfo.loadGPDatas(new IMatchInfo.OnLoadCompleteListener() {
            @Override
            public void loadSuccess(final List<MatchInfo> matchInfoList) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iMatchSubView.hideLoading();
                        iMatchSubView.showGPData(matchInfoList, type);
                    }
                });
            }

            @Override
            public void loadFailed(String msg) {

            }
        });


    }

}