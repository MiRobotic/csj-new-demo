package com.csjbot.mobileshop.feature.media;

import android.content.pm.ActivityInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseMainPageActivity;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.feature.media.contract.MediaPublicizeContract;
import com.csjbot.mobileshop.feature.media.contract.MediaPublicizePresenterImpl;
import com.csjbot.mobileshop.router.BRouterPath;

import butterknife.BindView;
import cn.jzvd.Jzvd;

@Route(path = BRouterPath.MEDIA_PUBLICIZE)
public class MediaPublicizeActivity extends BaseModuleActivity implements MediaPublicizeContract.View {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.ll_data)
    LinearLayout llData;
    @BindView(R.id.fl_no_data)
    FrameLayout flNoData;

    private MediaPublicizeContract.Presenter mPresenter;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void beforeSetContentView() {
        if (isPlus()) {
            Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else {
            Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_media_publicize, R.layout.activity_plus_media_publictyize);
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public void init() {
        super.init();
        new MediaPublicizePresenterImpl(this, this);
        mPresenter.init();
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return true;
        }
        prattle(answerText);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setPresenter(MediaPublicizeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView getRecycler() {
        return rv;
    }

    @Override
    public LinearLayout getDots() {
        return llDots;
    }

    @Override
    public void isNoData(boolean isNoData) {
        if (isNoData) {
            llData.setVisibility(View.GONE);
            flNoData.setVisibility(View.VISIBLE);
        } else {
            prattle(getString(R.string.click_random_video));
            llData.setVisibility(View.VISIBLE);
            flNoData.setVisibility(View.GONE);
        }
    }
}
