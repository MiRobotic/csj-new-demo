package com.csjbot.mobileshop.feature.visit.temp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnFaceSaveListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.navigation.NaviActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSAwsProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSProxy;
import com.csjbot.mobileshop.model.http.bean.req.VipInfoReq;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.csjbot.mobileshop.util.StrUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by 孙秀艳 on 2019/6/10.
 */
@Route(path = BRouterPath.TEMP_VISIT)
public class TempVisitorActivity extends BaseModuleActivity {

    @BindView(R.id.iv_preview)
    ImageView iv_preview;//预览

    @BindView(R.id.ll_visitor_success)
    LinearLayout ll_visitor_success;//访问成功

    @BindView(R.id.ll_visitor_info)
    LinearLayout ll_visitor_info;//访客信息

    @BindView(R.id.fl_take_photo)
    FrameLayout fl_take_photo;//拍照访问

    @BindView(R.id.ll_complete_and_retake)
    LinearLayout ll_complete_and_retake;//拍照确认按钮

    @BindView(R.id.ll_invitor_success)//受访的进入了临时的
    LinearLayout ll_invitor_success;

    @BindView(R.id.ll_repeat_login)
    LinearLayout ll_repeat_login;//受访的进入临时的，并且重复签到

    @BindView(R.id.bt_complete)
    Button bt_complete;//拍照完成

    @BindView(R.id.bt_retake)
    Button bt_retake;//重新拍照

    @BindView(R.id.bt_take_photo)
    Button bt_take_photo;//拍照按钮

    @BindView(R.id.btn_navi)
    Button btn_navi;//访客带路按钮

    @BindView(R.id.btn_home)
    Button btn_home;//返回首页

    @BindView(R.id.et_visit_name)
    EditText et_visit_name;//访客姓名

    @BindView(R.id.et_visit_phone)
    EditText et_visit_phone;//访客电话

    @BindView(R.id.et_join_phone)
    EditText et_join_phone;//接待人电话

    @BindView(R.id.et_visit_person)
    EditText et_visit_person;//访客事由

    @BindView(R.id.btn_submit)
    Button btn_submit;//提交

    @BindView(R.id.tv_visit_people)
    TextView tv_visit_people;//签到人

    @BindView(R.id.tv_visit_goal)
    TextView tv_visit_goal;//签到目的

    @BindView(R.id.tv_visited_people)
    TextView tv_visited_people;//签到人

    @BindView(R.id.tv_visited_goal)
    TextView tv_visited_goal;//签到目的

    @BindView(R.id.tv_join)
    TextView tv_join;//邀请人

    @BindView(R.id.btn_invitor_navi)
    Button btn_invitor_navi;

    @BindView(R.id.btn_invitor_home)
    Button btn_invitor_home;

    RobotManager mRobotManager;

    boolean mPausePreview;

    boolean mCorrectPhoto;

    Drawable mPhoto;

    volatile boolean isTakeIn;

    private boolean isReturnHome = false;//是访客带路还是返回首页
    private String faceid;
    private int sex;//0男1女
    private String visitName;//访客名称
    private Boolean flag;//是否第一次签到
    private String receptionist;//接待人
    private String visitCause;//访问事由
    private int visitType;//访问类型 0 受邀访客  1 临时访客
    private String receptionistPhone;//接待人电话
    private String visitPhone;//访客电话
    private Bitmap bmpPhoto;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_temp_visitor, R.layout.activity_vertical_temp_visitor);
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
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.SHOW_HIDE_QR_CODE, true));
        sendBroadcast(new Intent(AdvertisementService.PLUS_VIDEO_MIN_VOICE));
    }

    @Override
    public void init() {
        super.init();
        mRobotManager = RobotManager.getInstance();
        RobotManager.getInstance().addListener(mFaceListener);
        mRobotManager.addListener(new OnCameraListener() {
            @Override
            public void response(Bitmap reponse) {
                if (!mPausePreview) {
                    runOnUiThread(() -> iv_preview.setImageBitmap(bmpPhoto = reponse));
                }
            }
        });
        mRobotManager.addListener(new OnSnapshotoListener() {
            @Override
            public void response(String response) {
                try {
                    BlackgagaLogger.debug("response:" + response);
                    int errorCode = new JSONObject(response).getInt("error_code");
                    BlackgagaLogger.debug("error_code:-------------------->" + errorCode);
                    if (errorCode == 0) {
                        mCorrectPhoto = true;
                    } else {
                        mCorrectPhoto = false;
                    }
                } catch (JSONException e) {
                    BlackgagaLogger.error(e.toString());
                }
            }
        });

        String str = String.format(Locale.getDefault(), getString(R.string.photo_speak), getRobotName());
        speak(str);
        setRobotChatMsg(str);
    }

    //拍照
    private void takePhoto() {
        if (isTakeIn) {
            return;
        }
        new Thread(() -> {
            isTakeIn = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(R.string.preare_camera);
                setRobotChatMsg(R.string.preare_camera);
            });
            runOnUiThread(() -> {
                speak(getString(R.string.three));
                setRobotChatMsg(getString(R.string.three));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.two));
                setRobotChatMsg(getString(R.string.two));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.num_one));
                setRobotChatMsg(getString(R.string.num_one));
            });
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                mPausePreview = true;
                mPhoto = iv_preview.getDrawable();
                ll_complete_and_retake.setVisibility(View.VISIBLE);
                bt_take_photo.setVisibility(View.GONE);
                ServerFactory.getFaceInstance().snapshot();

                isTakeIn = false;
            });
        }).start();
    }

    //完成
    public void complete() {
        if (!mCorrectPhoto) {
            String errorText = getString(R.string.retake_photo_speak_text);
            speak(errorText);
            setRobotChatMsg(errorText);
            return;
        }
//        mCorrectPhoto = false;
        fl_take_photo.setVisibility(View.GONE);
        ll_visitor_info.setVisibility(View.VISIBLE);
        setInfo();
    }

    private void setInfo() {
        com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().findVisitByFaceid(Robot.SN, faceid,
                new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String bodyJson = "";
                        try {
                            bodyJson = responseBody.string();
                        } catch (IOException e) {}
                        if (!TextUtils.isEmpty(bodyJson)) {
                            try {
                                JSONObject jsonObject = new JSONObject(bodyJson);
                                String status = jsonObject.getString("code");
                                if (status.equals("200")) {
                                    JSONObject rowsObj = jsonObject.getJSONObject("rows");
                                    visitName = rowsObj.getString("visitName");
                                    flag = rowsObj.getBoolean("flag");
                                    receptionist = rowsObj.getString("receptionist");
                                    visitCause = rowsObj.getString("visitCause");
                                    visitType = rowsObj.getInt("visitType");
                                    receptionistPhone = rowsObj.getString("receptionistPhone");
                                    visitPhone = rowsObj.getString("visitPhone");
                                    if (visitType == 1) {//临时访客
                                        fl_take_photo.setVisibility(View.GONE);
                                        ll_visitor_info.setVisibility(View.VISIBLE);
                                        et_visit_name.setText(visitName);
                                        et_visit_phone.setText(visitPhone);
                                        et_join_phone.setText(receptionistPhone);
                                        et_visit_person.setText(visitCause);
                                        speak(getString(R.string.input_temp_visit_info));
                                        setRobotChatMsg(getString(R.string.input_temp_visit_info));
                                    } else {//受邀访客
                                        if (flag) {//没签到
                                            fl_take_photo.setVisibility(View.GONE);
                                            ll_invitor_success.setVisibility(View.VISIBLE);
                                            tv_visit_people.setText("["+visitName+"]");
                                            tv_visit_goal.setText(getString(R.string.join)+receptionist);
                                            tv_join.setText(getString(R.string.contracted)+"[" +receptionist+"]"+getString(R.string.wait));
                                            setVisitorSpeakAndDisplay();
                                            timer.start();
                                        } else {//已签到
                                            isReturnHome = true;
                                            fl_take_photo.setVisibility(View.GONE);
                                            ll_repeat_login.setVisibility(View.VISIBLE);
                                            tv_visited_people.setText("["+visitName+"]");
                                            tv_visited_goal.setText(getString(R.string.reception)+receptionist);
                                            timer.start();
                                        }
                                    }
                                } else {
                                    String msg = jsonObject.getString("msg");
//                                    Toast.makeText(TempVisitorActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                getLog().error("e:" + e.toString());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setFinishSpeakAndDisplay() {
        String str = String.format(Locale.getDefault(), getString(R.string.temp_visit_display), getRobotName());
        speak(str);
        setRobotChatMsg(str);
    }

    private void setVisitorSpeakAndDisplay() {
        String str = String.format(Locale.getDefault(), getString(R.string.visited_display), visitName, visitCause, receptionist, getRobotName());
        speak(str);
        setRobotChatMsg(str);
    }

    public void retake() {
        mPausePreview = false;
        ll_complete_and_retake.setVisibility(View.INVISIBLE);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(R.string.preare_camera);
                setRobotChatMsg(R.string.preare_camera);
            });
            runOnUiThread(() -> {
                speak(getString(R.string.three));
                setRobotChatMsg(getString(R.string.three));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.two));
                setRobotChatMsg(getString(R.string.two));
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                speak(getString(R.string.num_one));
                setRobotChatMsg(getString(R.string.num_one));
            });
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {
                mPausePreview = true;
                mPhoto = iv_preview.getDrawable();
                ll_complete_and_retake.setVisibility(View.VISIBLE);
                ServerFactory.getFaceInstance().snapshot();
            });

        }).start();
    }

    @OnClick({R.id.btn_navi, R.id.btn_home, R.id.bt_take_photo, R.id.btn_invitor_navi,R.id.btn_invitor_home,
            R.id.bt_complete, R.id.bt_retake, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_invitor_navi:
            case R.id.btn_navi:
                if (true) {//是不是恢复地图
                    jumpActivity(NaviActivity.class);
                    finish();
                }
                break;
            case R.id.btn_invitor_home:
            case R.id.btn_home:
                finish();
                break;
            case R.id.bt_take_photo:
                takePhoto();
                break;
            case R.id.bt_complete:
                complete();
                break;
            case R.id.bt_retake:
                retake();
                break;
            case R.id.btn_submit:
                submitBmp();
                break;
            default:
                break;
        }
    }

    CountDownTimer timer = new CountDownTimer(31000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            StringBuilder builder = new StringBuilder();
            if (isReturnHome) {
                builder.append(getString(R.string.back_to_home));
            } else {
                builder.append(getString(R.string.visit_way));
            }
            builder.append("(");
            builder.append(millisUntilFinished/1000+"");
            builder.append("s)");
            if (isReturnHome) {
                btn_home.setText(builder.toString());
                btn_invitor_home.setText(builder.toString());
            } else {
                btn_invitor_navi.setText(builder.toString());
                btn_navi.setText(builder.toString());
            }
        }

        @Override
        public void onFinish() {
            finish();
        }
    };

    //提交
    private void submit(String url) {
        try {
            JSONObject object = new JSONObject();
            object.put("sex", sex);
            object.put("visitName", et_visit_name.getText().toString());
            object.put("visitPhone", et_visit_phone.getText().toString());
            object.put("visitCause", et_visit_person.getText().toString());
            object.put("receptionistPhone", et_join_phone.getText().toString());
            JSONObject robotVisitSnEntity = new JSONObject();
            robotVisitSnEntity.put("visitUrl", url);
            robotVisitSnEntity.put("sn", Robot.SN);
            robotVisitSnEntity.put("faceid", faceid);
            object.put("robotVisitSnEntity", robotVisitSnEntity);
            String body = object.toString().replace("\\/", "/");
            CsjlogProxy.getInstance().info("临时访客信息json : " + body);
            //上传临时访客信息
            com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().addVisitInfo(Robot.SN, body, new Observer<ResponseBean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResponseBean stringResponseBean) {
                    if (stringResponseBean.getCode() == 200) {
                        ll_visitor_info.setVisibility(View.GONE);
                        ll_visitor_success.setVisibility(View.VISIBLE);
                        isReturnHome = true;
                        setFinishSpeakAndDisplay();
                        timer.start();
                    }
                }

                @Override
                public void onError(Throwable e) {}

                @Override
                public void onComplete() {}
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitBmp() {
        if (StrUtil.isBlank(et_visit_name.getText().toString()) || StrUtil.isBlank(et_visit_phone.getText().toString())
                || StrUtil.isBlank(et_join_phone.getText().toString()) || StrUtil.isBlank(et_visit_person.getText().toString())) {
            Toast.makeText(TempVisitorActivity.this, getString(R.string.input_all_info), Toast.LENGTH_SHORT).show();
        } else if ((StrUtil.isNotBlank(et_visit_phone.getText().toString()) && et_visit_phone.getText().toString().length() < 3) ||
                (StrUtil.isNotBlank(et_join_phone.getText().toString()) && et_join_phone.getText().toString().length() < 3)) {
            Toast.makeText(TempVisitorActivity.this, getString(R.string.phone_limit_length), Toast.LENGTH_SHORT).show();
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmpPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
            if (Constants.isI18N()) {
                OSSAwsProxy.newInstance().uploadFile(bmpPhoto, new OSSAwsProxy.OSSListener() {
                    @Override
                    public void onSuccess(String url, String name1) {
                        if(TextUtils.isEmpty(faceid)){
                            registerVIP(url);
                        } else {
                            submit(url);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else {
                OSSProxy.newInstance().uploadFile(bmpPhoto, new OSSProxy.OSSListener() {
                    @Override
                    public void onSuccess(String url, String name1) {
                        if(TextUtils.isEmpty(faceid)){
                            registerVIP(url);
                        } else {
                            submit(url);
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e("sunxy", "onError");
                    }
                });
            }
        }
    }

    /**
     * 注册会员
     */
    private void registerVIP(String url) {
        mRobotManager.addListener((OnFaceSaveListener) response -> {
            try {
                BlackgagaLogger.debug("response:" + response);
                int errorCode = new JSONObject(response).getInt("error_code");
                BlackgagaLogger.debug("error_code:-------------------->" + errorCode);
                if (errorCode == 0) {
                    //人脸唯一性标识符
                    faceid = new JSONObject(response).getString("person_id");
                    submit(url);
                }
            } catch (JSONException e) {
                BlackgagaLogger.error(e.toString());
            }
        });
        ServerFactory.getFaceInstance().faceRegSave(et_visit_name.getText().toString());
    }

    private OnFaceListener mFaceListener = new OnFaceListener() {
        @Override
        public void personInfo(String json) {
            try {
                JSONObject jsonObject = (JSONObject) new JSONObject(json).getJSONArray("face_list").get(0);
                JSONObject faceRecg = jsonObject.getJSONObject("face_recg");
                int confidence = faceRecg.getInt("confidence");
                String name = faceRecg.getString("name");
                if (confidence > 80) {
                    faceid = faceRecg.getString("person_id");
                    sex = jsonObject.getJSONObject("face_detect").getInt("gender");
                    Log.e("sunxy", "faceid" + faceid + "sex" + sex);
                }
            } catch (JSONException e) {
            }
        }

        @Override
        public void personNear(boolean person) {}

        @Override
        public void personList(String json) {}
    };

    private String getRobotName() {
        String robotname = "";
        if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
            robotname = getString(R.string.robot_name_alice);
        } else if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
            robotname = getString(R.string.robot_name_amy);
        } else if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
            robotname = getString(R.string.robot_name_snow);
        }
        if (Constants.greetContentBean != null) {
            if (!TextUtils.isEmpty(Constants.greetContentBean.getRobotName())) {
                robotname = Constants.greetContentBean.getRobotName();
            }
        }
        return robotname;
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        RobotManager.getInstance().removeListener(mFaceListener);
        if (bmpPhoto != null) {
            if (!bmpPhoto.isRecycled()) {
                // 释放bitmap
                bmpPhoto.recycle();
                bmpPhoto = null;
            }
        }
    }
}
