package com.csjbot.mobileshop.feature.vipcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnFaceSaveListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.ai.VipCenterAI;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSAwsProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.OSSProxy;
import com.csjbot.mobileshop.model.http.bean.req.VipInfoReq;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by xiasuhuei321 on 2017/10/18.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

@Route(path = BRouterPath.VIP_CENTER)
public class VipCenterActivity extends BaseModuleActivity {
    public static final String QRCODE = "qrcode";

    private ImageView iv_qrcode;

    VipCenterAI mAI;

    RobotManager mRobotManager;

    boolean mPausePreview;

    boolean mCorrectPhoto;

    Drawable mPhoto;

    @BindView(R.id.iv_preview)
    ImageView iv_preview;

    @BindView(R.id.iv_preview_photo)
    ImageView iv_preview_photo;

    @BindView(R.id.ll_register)
    LinearLayout ll_register;

    @BindView(R.id.ll_register_success)
    LinearLayout ll_register_success;

    @BindView(R.id.fl_take_photo)
    FrameLayout fl_take_photo;

    @BindView(R.id.ll_complete_and_retake)
    LinearLayout ll_complete_and_retake;

    @BindView(R.id.bt_complete)
    Button bt_complete;

    @BindView(R.id.bt_retake)
    Button bt_retake;

    @BindView(R.id.bt_take_photo)
    Button bt_take_photo;

    @BindView(R.id.et_name)
    EditText et_name;

//    @BindView(R.id.et_company)
//    EditText etCompany;

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;

    @BindView(R.id.rb_man)
    RadioButton rb_man;

    @BindView(R.id.rb_woman)
    RadioButton rb_woman;

    Bitmap bmpPhoto;

    volatile boolean isTakeIn;

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_vip, R.layout.activity_vertical_vip);
    }

    @Override
    protected boolean isDisableEntertainment() {
        return true;
    }

    private Runnable finishRb = this::finish;

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
        mAI = VipCenterAI.newInstance();
        mAI.initAI(this);

        mRobotManager = RobotManager.getInstance();
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
//        mRobotManager.cameraConnect(this);
        rb_man.setButtonDrawable(R.drawable.radio_btn_selected);
        rb_woman.setButtonDrawable(R.drawable.radio_btn_unselected);
        rb_man.setOnClickListener((v) -> {
            rb_man.setButtonDrawable(R.drawable.radio_btn_selected);
            rb_woman.setButtonDrawable(R.drawable.radio_btn_unselected);
        });
        rb_woman.setOnClickListener((v) -> {
            rb_woman.setButtonDrawable(R.drawable.radio_btn_selected);
            rb_man.setButtonDrawable(R.drawable.radio_btn_unselected);
        });
        if (CsjLanguage.CURRENT_LANGUAGE == CsjLanguage.CHINESE) {
            String robotname = "";
            if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS) || BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_ALICE)) {
                robotname = "爱丽丝";
            } else if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)) {
                robotname = "艾米";
            } else if (BuildConfig.FLAVOR.equals(BuildConfig.ROBOT_TYPE_DEF_SNOW)) {
                robotname = "小雪";
            }
            StringBuilder builder = new StringBuilder();
            builder.append("您可以点击屏幕上的按钮，").append(robotname).append("就将自动给您拍照哟。\n")
                    .append("注册之后，").append(robotname).append("就将能认识您，可以更好的为您服务！");
            setRobotChatMsg(builder.toString());
            StringBuilder builder1 = new StringBuilder();
            builder1.append("您可以点击屏幕上的按钮，").append(robotname).append("就将自动给您拍照哟。");
            speak(builder1.toString());
        } else {
            speak(getString(R.string.member_register_speak_text), true);
        }

        mainHandler.postDelayed(checkRb, 3000);
    }

    private Runnable checkRb = new Runnable() {
        @Override
        public void run() {
            if (bmpPhoto == null) {
                RobotManager.getInstance().cameraDisconnect(VipCenterActivity.this);
                mainHandler.postDelayed(() -> RobotManager.getInstance().cameraConnect(VipCenterActivity.this), 1000);
            }
        }
    };

    @OnClick(R.id.bt_take_photo)
    public void takePhoto() {
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

    @OnClick(R.id.bt_complete)
    public void complete() {
        if (!mCorrectPhoto) {
            String errorText = getString(R.string.retake_photo_speak_text);
            speak(errorText);
            setRobotChatMsg(errorText);
            return;
        }
        mCorrectPhoto = false;
        fl_take_photo.setVisibility(View.GONE);
        ll_register.setVisibility(View.VISIBLE);

        iv_preview_photo.setImageDrawable(mPhoto);
    }

    @OnClick(R.id.bt_retake)
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

    @OnClick(R.id.bt_ok)
    public void ok() {
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            String speakText = getString(R.string.input_name);
            speak(speakText);
            setRobotChatMsg(speakText);
            return;
        }

        if (!isUserName(name)) {
            String speakText = getString(R.string.name_format);
            speak(speakText);
            setRobotChatMsg(speakText);
            return;
        }

        mRobotManager.addListener((OnFaceSaveListener) response -> {
            try {
                BlackgagaLogger.debug("response:" + response);
                int errorCode = new JSONObject(response).getInt("error_code");
                BlackgagaLogger.debug("error_code:-------------------->" + errorCode);
                if (errorCode == 0) {
                    runOnUiThread(() -> {
                        ll_register.setVisibility(View.GONE);
                        ll_register_success.setVisibility(View.VISIBLE);
                        String text = getString(R.string.register_success_speak_text);
                        speak(text);
                        setRobotChatMsg(text);
                        mainHandler.postDelayed(finishRb, 3000);
                    });

                    /** 会员信息上传至服务器 */

                    //人脸唯一性标识符
                    String faceInfoId = new JSONObject(response).getString("person_id");

                    // Bitmap转Base64
                    String resource;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmpPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    resource = Base64.encodeToString(bytes, Base64.NO_WRAP);

                    // 获取性别
                    int id = rg_gender.getCheckedRadioButtonId();
                    int sex = 0;
                    if (id == R.id.rb_man) {
                        sex = 0;
                    } else if (id == R.id.rb_woman) {
                        sex = 1;
                    }

                    // 获取日期
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    String date = sDateFormat.format(new Date());

                    getLog().info("vipinfo resource:" + resource);
                    String phone = etPhone.getText().toString().trim();

                    if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                        phone = "";
                    }
                    if (TextUtils.isEmpty(faceInfoId)) {
                        faceInfoId = "";
                    }

                    VipInfoReq vipInfoReq = new VipInfoReq();
                    vipInfoReq.setName(name);
                    vipInfoReq.setSex(sex);
                    vipInfoReq.setTelephone(phone);
                    vipInfoReq.setSn(Robot.SN);
                    vipInfoReq.setFaceId(faceInfoId);
                    List<VipInfoReq.RobotReguserFacesBean> robotReguserFacesBeans = new ArrayList<>();
                    VipInfoReq.RobotReguserFacesBean robotReguserFacesBean = new VipInfoReq.RobotReguserFacesBean();
                    robotReguserFacesBeans.add(robotReguserFacesBean);
                    vipInfoReq.setRobotReguserFaces(robotReguserFacesBeans);

                    getLog().info("vipinfo json:" + new Gson().toJson(vipInfoReq));

                    if (Constants.isI18N()) {
                        OSSAwsProxy.newInstance().uploadFile(bmpPhoto, new OSSAwsProxy.OSSListener() {
                            @Override
                            public void onSuccess(String url, String name1) {
                                robotReguserFacesBean.setName(name1);
                                robotReguserFacesBean.setUrl(url);
                                com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().saveVip(new Gson().toJson(vipInfoReq), new Observer<ResponseBean<String>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ResponseBean<String> stringResponseBean) {
                                        if (stringResponseBean != null) {
                                            getLog().info("saveVip onNext:json" + new Gson().toJson(stringResponseBean));
                                        }
                                        if (stringResponseBean != null && stringResponseBean.getCode() == 200) {

                                        }
                                        getLog().info("会员信息上传至服务成功");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        getLog().info("会员信息上传至服务失败" + e.toString());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    } else {
                        OSSProxy.newInstance().uploadFile(bmpPhoto, new OSSProxy.OSSListener() {
                            @Override
                            public void onSuccess(String url, String name1) {
                                robotReguserFacesBean.setName(name1);
                                robotReguserFacesBean.setUrl(url);
                                com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().saveVip(new Gson().toJson(vipInfoReq), new Observer<ResponseBean<String>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ResponseBean<String> stringResponseBean) {
                                        if (stringResponseBean != null) {
                                            getLog().info("saveVip onNext:json" + new Gson().toJson(stringResponseBean));
                                        }
                                        if (stringResponseBean != null && stringResponseBean.getCode() == 200) {

                                        }
                                        getLog().info("会员信息上传至服务成功");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        getLog().info("会员信息上传至服务失败" + e.toString());
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                } else if (errorCode == 40002) {
                    runOnUiThread(() -> {
                        String text = getString(R.string.cannot_repeat_register);
                        speak(text);
                        setRobotChatMsg(text);
                    });
                } else if (errorCode == 40003) {
                    runOnUiThread(() -> {
                        String text = getString(R.string.name_is_error);
                        speak(text);
                        setRobotChatMsg(text);
                    });
                    return;
                } else {
                    runOnUiThread(() -> {
                        String text = getString(R.string.register_faild_speak_text);
                        speak(text);
                        setRobotChatMsg(text);
                    });
                }
                if (errorCode != 0) {
                    runOnUiThread(() -> {
                        mPausePreview = false;
                        ll_register.setVisibility(View.GONE);
                        fl_take_photo.setVisibility(View.VISIBLE);
                        ll_complete_and_retake.setVisibility(View.GONE);
                        bt_take_photo.setVisibility(View.VISIBLE);
                        et_name.setText("");
                    });
                }
            } catch (JSONException e) {
                BlackgagaLogger.error(e.toString());
            }
        });
        ServerFactory.getFaceInstance().faceRegSave(name);
    }

    private RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mRobotManager.cameraDisconnect(this);
        mainHandler.removeCallbacks(finishRb);
        mainHandler.removeCallbacks(checkRb);
        if (bmpPhoto != null) {
            if (!bmpPhoto.isRecycled()) {
                // 释放bitmap
                bmpPhoto.recycle();
                bmpPhoto = null;
            }
        }
    }

    @Override
    protected boolean onSpeechMessage(String text, String answerText) {
        if (super.onSpeechMessage(text, answerText)) {
            return false;
        }
        if (text.contains(getString(R.string.retake_photo))) {
            retake();
        } else if (text.contains(getString(R.string.take_photo))) {
            takePhoto();
        } else if (text.contains(getString(R.string.complete)) || text.contains("拍好了")) {
            complete();
        } else if (text.contains(getString(R.string.sure))) {
            ok();
        }
        return true;
    }

    /**
     * 手机号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public boolean isPhone(String str) {
        Pattern p = Pattern.compile("0?(13|14|15|17|18|19)[0-9]{9}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证用户名格式
     *
     * @param str
     * @return
     */
    private boolean isUserName(String str) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{1,5}$");
        Matcher m = p.matcher(str);
        if (m.matches()) {
            return m.matches();
        } else {
            Pattern p1 = Pattern.compile("^[A-Za-z]{1,20}$");
            Matcher m1 = p1.matcher(str);
            return m1.matches();
        }
    }
}
