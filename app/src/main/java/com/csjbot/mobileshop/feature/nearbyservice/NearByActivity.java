package com.csjbot.mobileshop.feature.nearbyservice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.ai.NearByAI;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.router.BRouterPath;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiasuhuei321 on 2017/10/17.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.NEAR_BY_MAIN)
public class NearByActivity extends BaseModuleActivity {

    @BindView(R.id.ll_food)
    LinearLayout mLlFood;
    @BindView(R.id.ll_scenic)
    LinearLayout mLlScenic;
    @BindView(R.id.ll_hotel)
    LinearLayout mLlHotel;
    @BindView(R.id.ll_relax)
    LinearLayout mLlRelax;
    @BindView(R.id.ll_share_bike)
    LinearLayout mLlShareBike;
    @BindView(R.id.ll_supermarket)
    LinearLayout mLlSupermarket;
    @BindView(R.id.ll_atm)
    LinearLayout mLlAtm;
    @BindView(R.id.ll_wc)
    LinearLayout mLlWc;
    @BindView(R.id.ll_fast_hotel)
    LinearLayout mLlFastHotel;
    @BindView(R.id.ll_cyber_bar)
    LinearLayout mLlCyberBar;
    @BindView(R.id.ll_underground)
    LinearLayout mLlUnderground;
    @BindView(R.id.ll_gas_station)
    LinearLayout ll_gas_station;

    NearByAI mAI;
    @BindView(R.id.ll_bus_station)
    LinearLayout llBusStation;
    @BindView(R.id.ll_charge)
    LinearLayout llCharge;
    @BindView(R.id.ll_park)
    LinearLayout llPark;
    @BindView(R.id.ll_hospital)
    LinearLayout llHospital;
    @BindView(R.id.ll_lv_scene)
    LinearLayout llLvScene;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        return getCorrectLayoutId(R.layout.activity_nearby, R.layout.activity_vertical_nearby);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, false));
    }

    @Override
    public void init() {
        super.init();
        if (TextUtils.equals(Constants.Scene.CurrentScene,"lvyou")) {
            llLvScene.setVisibility(View.VISIBLE);
        }
        mAI = NearByAI.newInstance();
        mAI.initAI(this);
        if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {
            setRobotChatMsg("您可以点击屏幕上的按钮，或者通过语音对我说：\n" +
                    "1、附近的美食\n" +
                    "2、附近的景点\n" +
                    "3、附近的酒店");
            speak("您可以通过语音对我说：附近的美食");
        } else {
            speak(getString(R.string.nearby_init_speak), true);
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        NearByAI.Intent intent = mAI.getIntent(text);
        if (intent != null) {
            mAI.handleIntent(intent);
            return true;
        }
        prattle(answerText);
        return true;
    }


    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    public boolean isOpenChat() {
        return true;
    }


    @OnClick({R.id.ll_food, R.id.ll_scenic, R.id.ll_hotel,
            R.id.ll_relax, R.id.ll_share_bike, R.id.ll_supermarket,
            R.id.ll_atm, R.id.ll_wc, R.id.ll_fast_hotel, R.id.ll_cyber_bar,
            R.id.ll_underground, R.id.ll_gas_station,
            R.id.ll_bus_station, R.id.ll_charge, R.id.ll_park, R.id.ll_hospital})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_food:
                goPoiSearchAct("美食");
                break;
            case R.id.ll_scenic:
                goPoiSearchAct("景点");
                break;
            case R.id.ll_hotel:
                goPoiSearchAct("酒店");
                break;
            case R.id.ll_relax:
                goPoiSearchAct("休闲娱乐");
                break;
            case R.id.ll_share_bike:
                goPoiSearchAct("共享单车");
                break;
            case R.id.ll_supermarket:
                goPoiSearchAct("超市");
                break;
            case R.id.ll_atm:
                goPoiSearchAct("ATM");
                break;
            case R.id.ll_wc:
                goPoiSearchAct("厕所");
                break;
            case R.id.ll_fast_hotel:
                goPoiSearchAct("快捷酒店");
                break;
            case R.id.ll_cyber_bar:
                goPoiSearchAct("网吧");
                break;
            case R.id.ll_underground:
                goPoiSearchAct("地铁");
                break;
            case R.id.ll_gas_station:
                goPoiSearchAct("加油站");
                break;
            case R.id.ll_bus_station:
                goPoiSearchAct("公交车站");
                break;
            case R.id.ll_charge:
                goPoiSearchAct("共享充电宝");
                break;
            case R.id.ll_park:
                goPoiSearchAct("停车场");
                break;
            case R.id.ll_hospital:
                goPoiSearchAct("医院");
                break;
            default:
                break;
        }
    }

    public void goPoiSearchAct(String text) {
        ARouter.getInstance()
                .build(BRouterPath.NEAR_BY_SEARCH)
                .withString(PoiSearchActivity.KEYWORD, text)
                .navigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
