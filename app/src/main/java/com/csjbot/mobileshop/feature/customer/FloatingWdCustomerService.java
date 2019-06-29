package com.csjbot.mobileshop.feature.customer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.event.AdvertisementEvent;
import com.csjbot.mobileshop.feature.Learning.event.FloatWindowBackEvent;
import com.csjbot.mobileshop.global.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.OutputStream;


public class FloatingWdCustomerService extends Service {
    /**
     * 悬浮窗X坐标
     */
    private static int FLOATWINDOW_POSITION_X = 0;
    /**
     * 悬浮窗Y坐标
     */
    private static int FLOATWINDOW_POSITION_Y = 0;
    /**
     * 悬浮窗是否隐藏
     */
    public static boolean sIsShow = true;

    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;
    private ImageView mFloatView;
    private Handler mHandler = new Handler();
    private AudioManager audio;


    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        audio = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        createFloatView();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private AlphaAnimation mHideAnimation = null;

    private AlphaAnimation mShowAnimation = null;

    /**
     * View渐隐动画效果
     */
    private void setHideAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }

        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);

        view.startAnimation(mHideAnimation);
        mHandler.postDelayed(() -> view.setVisibility(View.INVISIBLE), duration);
    }

    /**
     * View渐现动画效果
     */
    private void setShowAnimation(final View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }

        if (null != mShowAnimation) {
            mShowAnimation.cancel();
        }

        mShowAnimation = new AlphaAnimation(0.0f, 1.0f);
        mShowAnimation.setDuration(duration);
        mShowAnimation.setFillAfter(true);

        view.startAnimation(mShowAnimation);
        mHandler.postDelayed(() -> view.setVisibility(View.VISIBLE), duration);
    }

    private void createFloatView() {
        wmParams = new LayoutParams();
        //获取WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags =
//          LayoutParams.FLAG_NOT_TOUCH_MODAL |
                LayoutParams.FLAG_NOT_FOCUSABLE
//          LayoutParams.FLAG_NOT_TOUCHABLE
        ;

        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.TOP | Gravity.START;

        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = 1880;
        wmParams.y = 400;

        //设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.floating_window, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        //浮动窗口按钮
        mFloatView = mFloatLayout.findViewById(R.id.iv_open);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //记住上次隐藏状态

        GestureDetector gestureDetector = new GestureDetector(this, new OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mFloatLayout.getVisibility() == View.VISIBLE) {
                    EventBus.getDefault().post(new OpenCustomerDialogEnvent());
                    setHideAnimation(mFloatLayout, 200);
                }
                sIsShow = false;
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                FLOATWINDOW_POSITION_X = (int) e2.getRawX() - mFloatLayout.getMeasuredWidth() + mFloatView.getMeasuredWidth() / 2;
                wmParams.x = FLOATWINDOW_POSITION_X;
                //25为状态栏的高度
                FLOATWINDOW_POSITION_Y = (int) e2.getRawY() - mFloatView.getMeasuredHeight() / 2;
                wmParams.y = FLOATWINDOW_POSITION_Y;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });


        //设置监听浮动窗口的触摸移动
        mFloatView.setOnTouchListener((v, event) -> {
            //getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
            return gestureDetector.onTouchEvent(event);
        });

        mFloatLayout.setVisibility(View.GONE);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isCustomerShow(FloatWindowCustomerEvent event) {
        if (event != null) {
            if (event.isShow()) {
                if (mFloatLayout.getVisibility() == View.GONE
                        || mFloatLayout.getVisibility() == View.INVISIBLE) {
                    mFloatLayout.setVisibility(View.VISIBLE);
                    sIsShow = true;
                }
            } else {
                if (mFloatLayout.getVisibility() == View.VISIBLE) {
                    mFloatLayout.setVisibility(View.GONE);
                    sIsShow = false;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            mWindowManager.removeView(mFloatLayout);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
