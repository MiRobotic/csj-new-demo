package com.csjbot.mobileshop.feature.visit.invite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnSnapshotoListener;
import com.csjbot.coshandler.listener.OnSpeakListener;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.service.AdvertisementService;
import com.csjbot.mobileshop.base.BasePresenter;
import com.csjbot.mobileshop.base.test.BaseModuleActivity;
import com.csjbot.mobileshop.core.RobotManager;
import com.csjbot.mobileshop.feature.navigation.NaviActivity;
import com.csjbot.mobileshop.feature.visit.temp.TempVisitorActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.BlackgagaLogger;
import com.iflytek.cloud.SpeechError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by 孙秀艳 on 2019/6/10.
 */
@Route(path = BRouterPath.INVITE_VISIT)
public class InviteVisitorActivity extends BaseModuleActivity {
    @BindView(R.id.btn_photo_visit)
    Button btn_photo_visit;//拍照签到

    @BindView(R.id.et_code_visit)
    EditText et_code_visit;//邀请码输入框

    @BindView(R.id.bt_ok)
    Button btn_ok;//邀请码确认

    @BindView(R.id.iv_preview)
    ImageView iv_preview;//预览

    @BindView(R.id.ll_visitor_type)
    LinearLayout ll_visitor_type;//访问方式

    @BindView(R.id.ll_visitor_success)
    LinearLayout ll_visitor_success;//访问成功

    @BindView(R.id.ll_repeat_login)
    LinearLayout ll_repeat_login;//重复签到

    @BindView(R.id.tv_visit_success)
    TextView tv_visit_success;//访问成功文字

    @BindView(R.id.fl_take_photo)
    FrameLayout fl_take_photo;//拍照访问

    @BindView(R.id.ll_complete_and_retake)
    LinearLayout ll_complete_and_retake;//拍照确认按钮

    @BindView(R.id.bt_complete)
    Button bt_complete;//拍照完成

    @BindView(R.id.bt_retake)
    Button bt_retake;//重新拍照

    @BindView(R.id.bt_take_photo)
    Button bt_take_photo;//拍照按钮

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

    @BindView(R.id.btn_navi)
    Button btn_navi;//访客带路按钮

    @BindView(R.id.btn_home)
    Button btn_home;//返回首页

    @BindView(R.id.btn_jump)
    Button btn_jump;//跳转

    @BindView(R.id.layout_jump)
    RelativeLayout layout_jump;//跳转界面

    Bitmap bmpPhoto;

    private boolean isVisitSucc = false;//是不是成功登录
    private String faceid;
    volatile boolean isTakeIn;
    RobotManager mRobotManager;

    boolean mPausePreview;

    boolean mCorrectPhoto;

    Drawable mPhoto;
    private String visitName;//访客名称
    private Boolean flag;//是否第一次签到
    private String receptionist;//接待人
    private String visitCause;//访问事由
    private int visitType;//访问类型 0 受邀访客  1 临时访客

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return getCorrectLayoutId(R.layout.activity_invite_visitor, R.layout.activity_vertical_invite_visitor);
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
        setRobotChatMsg(getString(R.string.choose_checkin_method));
        speak(getString(R.string.choose_checkin_method));

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
    }

    //验证邀请码
    private void validateCode() {
        com.csjbot.mobileshop.model.http.factory.ServerFactory.createApi().getVisitInfo(Robot.SN, et_code_visit.getText().toString(),
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
                            visitType = rowsObj.getInt("visitType");
                            flag = rowsObj.getBoolean("flag");
                            receptionist = rowsObj.getString("receptionist");
                            visitCause = rowsObj.getString("visitCause");
                            if (visitType == 1) {//临时访客
                                layout_jump.setVisibility(View.VISIBLE);
                                bt_retake.setEnabled(false);
                                bt_complete.setEnabled(false);
                                speak(getString(R.string.no_visit_record), new OnSpeakListener() {
                                    @Override
                                    public void onSpeakBegin() {

                                    }

                                    @Override
                                    public void onCompleted(SpeechError speechError) {
                                        timer1.start();
                                    }
                                });
                                setRobotChatMsg(getString(R.string.no_visit_record));
                            } else {
                                if (flag) {
                                    isVisitSucc = true;
                                    ll_visitor_type.setVisibility(View.GONE);
                                    ll_visitor_success.setVisibility(View.VISIBLE);
                                    tv_visit_people.setText("["+visitName+"]");
                                    tv_visit_goal.setText(getString(R.string.join)+receptionist);
                                    tv_join.setText(getString(R.string.contracted)+"[" +receptionist+"]"+getString(R.string.wait));
                                    setFinishSpeakAndDisplay();
                                    timer.start();
                                } else {
                                    ll_visitor_type.setVisibility(View.GONE);
                                    ll_repeat_login.setVisibility(View.VISIBLE);
                                    tv_visited_people.setText("["+visitName+"]");
                                    tv_visited_goal.setText(getString(R.string.join)+receptionist);
                                    setFinishSpeakAndDisplay();
                                    timer.start();
                                }
                            }
                        } else {
                            Toast.makeText(InviteVisitorActivity.this, getString(R.string.input_correct_code), Toast.LENGTH_SHORT).show();
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

    //验证是不是受邀人员
    private void validateFaceId() {
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
                            if (visitType == 1) {//临时访客
                                layout_jump.setVisibility(View.VISIBLE);
                                bt_retake.setEnabled(false);
                                bt_complete.setEnabled(false);
                                speak(getString(R.string.no_visit_record), new OnSpeakListener() {
                                    @Override
                                    public void onSpeakBegin() {

                                    }

                                    @Override
                                    public void onCompleted(SpeechError speechError) {
                                        timer1.start();
                                    }
                                });
                                setRobotChatMsg(getString(R.string.no_visit_record));
                            } else {
                                if (flag) {
                                    isVisitSucc = true;
                                    fl_take_photo.setVisibility(View.GONE);
                                    ll_visitor_success.setVisibility(View.VISIBLE);
                                    tv_visit_people.setText("["+visitName+"]");
                                    tv_visit_goal.setText(getString(R.string.join)+receptionist);
                                    tv_join.setText(getString(R.string.contracted)+"[" +receptionist+"]"+getString(R.string.wait));
                                    setFinishSpeakAndDisplay();
                                    timer.start();
                                } else {
                                    fl_take_photo.setVisibility(View.GONE);
                                    ll_repeat_login.setVisibility(View.VISIBLE);
                                    tv_visited_people.setText("["+visitName+"]");
                                    tv_visited_goal.setText("参加"+receptionist);
                                    timer.start();
                                }
                            }
                        } else {
                            String msg = jsonObject.getString("msg");
                            if ("NOT_FOUND".equals(msg)) {
                                layout_jump.setVisibility(View.VISIBLE);
                                bt_retake.setEnabled(false);
                                bt_complete.setEnabled(false);
                                speak(getString(R.string.no_visit_record), new OnSpeakListener() {
                                    @Override
                                    public void onSpeakBegin() {

                                    }

                                    @Override
                                    public void onCompleted(SpeechError speechError) {
                                        timer1.start();
                                    }
                                });
                                setRobotChatMsg(getString(R.string.no_visit_record));
                            } else {
                                Toast.makeText(InviteVisitorActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
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
        String str = String.format(Locale.getDefault(), getString(R.string.visited_display), visitName, visitCause, receptionist, getRobotName());
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
        validateFaceId();
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

    CountDownTimer timer = new CountDownTimer(31000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            StringBuilder builder = new StringBuilder();
            if (isVisitSucc) {
                builder.append(getString(R.string.visit_way));
            } else {
                builder.append(getString(R.string.back_to_home));
            }
            builder.append("(");
            builder.append(millisUntilFinished/1000+"");
            builder.append("s)");
            if (isVisitSucc) {
                btn_navi.setText(builder.toString());
            } else {
                btn_home.setText(builder.toString());
            }
        }

        @Override
        public void onFinish() {
            finish();
        }
    };

    CountDownTimer timer1 = new CountDownTimer(3100, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.jump));
            builder.append("(");
            builder.append(millisUntilFinished/1000+"");
            builder.append("s)");
            btn_jump.setText(builder.toString());
        }

        @Override
        public void onFinish() {
            jumpActivity(TempVisitorActivity.class);
            finish();
        }
    };

    @OnClick({R.id.btn_photo_visit, R.id.bt_ok, R.id.btn_navi, R.id.btn_home, R.id.bt_take_photo,
                R.id.bt_complete, R.id.bt_retake, R.id.btn_jump})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_photo_visit:
                ll_visitor_type.setVisibility(View.GONE);
                fl_take_photo.setVisibility(View.VISIBLE);
                bt_take_photo.setVisibility(View.VISIBLE);
                ll_complete_and_retake.setVisibility(View.GONE);
                String str = String.format(Locale.getDefault(), getString(R.string.photo_speak), getRobotName());
                speak(str);
                setRobotChatMsg(str);
                break;
            case R.id.bt_ok:
                validateCode();
                break;
            case R.id.btn_navi:
                if (true) {//是不是恢复地图
                    jumpActivity(NaviActivity.class);
                    finish();
                }
                break;
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
            case R.id.btn_jump:
                jumpActivity(TempVisitorActivity.class);
                finish();
                break;
            default:
                break;
        }
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
                    Log.e("sunxy", "faceid" + faceid);
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
