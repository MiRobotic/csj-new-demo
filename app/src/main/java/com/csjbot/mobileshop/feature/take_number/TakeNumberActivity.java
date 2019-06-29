package com.csjbot.mobileshop.feature.take_number;

import android.content.Intent;
import android.os.Bundle;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;

import butterknife.OnClick;

/**
 * Created by jingwc on 2017/9/26.
 */

public class TakeNumberActivity extends BaseModuleActivity implements TakeNumberContract.View {

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_take_number;
    }

    TakeNumberContract.Presenter mPresenter;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        mPresenter = new TakeNumberPresenter();
        mPresenter.initView(this);
    }

    @OnClick(R.id.bt_take_number)
    public void takeNumber(){
        mPresenter.takeNumber();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }
}
