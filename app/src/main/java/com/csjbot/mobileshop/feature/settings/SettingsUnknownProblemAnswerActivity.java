package com.csjbot.mobileshop.feature.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 荆为成 on 2018/8/6.
 */

public class SettingsUnknownProblemAnswerActivity extends BaseModuleActivity {


    @BindView(R.id.rg)
    RadioGroup rg;

    @Override
    public void afterViewCreated(Bundle savedInstanceState) {
        super.afterViewCreated(savedInstanceState);

    }

    @Override
    public void init() {
        super.init();
        getTitleView().setSettingLayoutGone();

        boolean isUseDefaultAnswer = SharedPreUtil.getBoolean(SharedKey.UNKNOWN_PROBLEM_ANSWER,SharedKey.UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT,true);
        if(isUseDefaultAnswer){
            rg.check(R.id.rb1);
        }else{
            rg.check(R.id.rb2);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return Constants.isPlus()
                ? R.layout.vertical_activity_settings_unknown_problem_answer : R.layout.activity_settings_unknown_problem_answer;

    }

    @Override
    public boolean isOpenChat() {
        return false;
    }

    @Override
    public boolean isOpenTitle() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_SHOW));
    }


    @OnClick(R.id.bt_save)
    public void bt_save(){
        boolean isUseDefaultAnswer = true;
        int id = rg.getCheckedRadioButtonId();
        if(id == R.id.rb1){
            isUseDefaultAnswer = true;
        }else if(id == R.id.rb2){
            isUseDefaultAnswer = false;
        }
        SharedPreUtil.putBoolean(SharedKey.UNKNOWN_PROBLEM_ANSWER,SharedKey.UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT,isUseDefaultAnswer);
        Constants.UnknownProblemAnswer.isUseDefaultAnswer = isUseDefaultAnswer;
        Toast.makeText(context, getString(R.string.save_success), Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.bt_customer)
    public void bt_customer(){
        jumpActivity(SettingsUnknownProblemAnswerCustomerActivity.class);
    }

}
