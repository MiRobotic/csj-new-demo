package com.csjbot.mobileshop.feature.selfhelp;

import android.content.Intent;

import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;

/**
 * Created by xiasuhuei321 on 2017/10/18.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class SelfhelpActivity extends BaseModuleActivity {
    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }
}
