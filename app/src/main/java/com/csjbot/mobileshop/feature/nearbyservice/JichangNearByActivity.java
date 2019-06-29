package com.csjbot.mobileshop.feature.nearbyservice;

import android.content.Intent;
import android.os.Bundle;
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

@Route(path = BRouterPath.JICHANG_NEAR_BY_MAIN)
public class JichangNearByActivity extends BaseModuleActivity {

    @BindView(R.id.ll_food)
    LinearLayout mLlFood;
    @BindView(R.id.ll_hotel)
    LinearLayout mLlHotel;
    @BindView(R.id.ll_fast_hotel)
    LinearLayout mLlFastHotel;
    @BindView(R.id.ll_specialty)
    LinearLayout mLlSpecialty;
    @BindView(R.id.ll_information_desk)
    LinearLayout mLlInformationDesk;
    @BindView(R.id.ll_cosmetology)
    LinearLayout mLlCosmetology;
    @BindView(R.id.ll_scenic)
    LinearLayout mLlScenic;
    @BindView(R.id.ll_atm)
    LinearLayout mLlAtm;
    @BindView(R.id.ll_convenience_store)
    LinearLayout mLlConvenienceStore;
    @BindView(R.id.ll_bus)
    LinearLayout mLlBus;
    @BindView(R.id.ll_taxi)
    LinearLayout mLlTaxi;
    @BindView(R.id.ll_flower_shop)
    LinearLayout mLlFlower_shop;

    NearByAI mAI;


    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        //获取当前场景
        return getCorrectLayoutId(R.layout.activity_jichang_nearby, R.layout.activity_vertical_jichang_nearby);
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

        if (text.contains("美食")) {
            goPoiSearchAct("美食");
            return true;
        } else if (text.contains("星级酒店")) {
            goPoiSearchAct("星级酒店");
            return true;
        } else if (text.contains("快捷酒店")) {
            goPoiSearchAct("快捷酒店");
            return true;
        } else if (text.contains("特产")) {
            goPoiSearchAct("特产");
            return true;
        } else if (text.contains("咨询台")) {
            goPoiSearchAct("咨询台");
            return true;
        } else if (text.contains("美容")) {
            goPoiSearchAct("美容");
            return true;
        } else if (text.contains("景点")) {
            goPoiSearchAct("景点");
            return true;
        } else if (text.contains("ATM")) {
            goPoiSearchAct("ATM");
            return true;
        } else if (text.contains("便利店")) {
            goPoiSearchAct("便利店");
            return true;
        } else if (text.contains("大巴")) {
            goPoiSearchAct("大巴");
            return true;
        } else if (text.contains("出租车")) {
            goPoiSearchAct("出租车");
            return true;
        } else if (text.contains("花店")) {
            goPoiSearchAct("花店");
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


    @OnClick({R.id.ll_food, R.id.ll_hotel, R.id.ll_fast_hotel
            , R.id.ll_specialty, R.id.ll_information_desk, R.id.ll_cosmetology
            , R.id.ll_scenic, R.id.ll_atm, R.id.ll_convenience_store
            , R.id.ll_bus, R.id.ll_taxi, R.id.ll_flower_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_food:
                goPoiSearchAct("美食");
                break;
            case R.id.ll_hotel:
                goPoiSearchAct("星级酒店");
                break;
            case R.id.ll_fast_hotel:
                goPoiSearchAct("快捷酒店");
                break;
            case R.id.ll_specialty:
                goPoiSearchAct("特产");
                break;
            case R.id.ll_information_desk:
                goPoiSearchAct("咨询台");
                break;
            case R.id.ll_cosmetology:
                goPoiSearchAct("美容");
                break;
            case R.id.ll_scenic:
                goPoiSearchAct("景点");
                break;
            case R.id.ll_atm:
                goPoiSearchAct("ATM");
                break;
            case R.id.ll_convenience_store:
                goPoiSearchAct("便利店");
                break;
            case R.id.ll_bus:
                goPoiSearchAct("大巴");
                break;
            case R.id.ll_taxi:
                goPoiSearchAct("出租车");
                break;
            case R.id.ll_flower_shop:
                goPoiSearchAct("花店");
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
