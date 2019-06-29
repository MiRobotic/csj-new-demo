package com.csjbot.mobileshop.feature.consult;

import android.content.Intent;
import android.os.Bundle;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;

/**
 * Created by jingwc on 2017/9/19.
 */

public class ConsultActivity extends BaseModuleActivity implements ConsultContract.View {

    private ConsultContract.Presenter mPresenter;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_consult;
    }

    @Override
    public void init() {
        mPresenter = new ConsultPresenter();
        mPresenter.initView(this);
    }

    @Override
    public void showConsult() {
        startActivity(new Intent(getApplicationContext(), ConsultActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }
}
