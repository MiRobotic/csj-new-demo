
package com.csjbot.mobileshop.advertisement.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.adapter.FloatQrCodeAdapter;
import com.csjbot.mobileshop.advertisement.event.AdvertisementEvent;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridLayoutManager;
import com.csjbot.mobileshop.widget.pagergrid.PagerGridSnapHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2019/1/18 17:22
 * @email 714081644@qq.com
 */
public class FloatQrCodeService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mParams;
    private View mLayout;
    private ImageView ivQrCodeHide;
    private ConstraintLayout clQrCode;
    private LinearLayout llDots;
    private PagerGridLayoutManager mLayoutManager;
    private FloatQrCodeAdapter mAdapter;
    private List<QrCodeBean> mList;
    private boolean isNoData = false;//是否无数据
    /**
     * 是否可以显示
     */
    private boolean isCanShow = true;
    /**
     * 二维码自动收缩倒计时
     */
    private CountDownTimer mTimer;
    private String speakWord;//当前要说的话术

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mTimer = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ivQrCodeHide.setVisibility(View.VISIBLE);
                clQrCode.setVisibility(View.GONE);
            }
        };
        createQrCode();
        getQrCodeData();
    }

    private void createQrCode() {
        mList = new ArrayList<>();
        mAdapter = new FloatQrCodeAdapter();
        mParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        //设置window type
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        mParams.gravity = Gravity.END | Gravity.BOTTOM;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.x = 0;
        mParams.y = isPlus() ? 1000 : 200;
        mLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_float_qr_code, null);
        ivQrCodeHide = mLayout.findViewById(R.id.iv_qr_code_hide);
        clQrCode = mLayout.findViewById(R.id.cl_qr_code);
        RecyclerView rvQrCode = mLayout.findViewById(R.id.rv_qr_code);
        llDots = mLayout.findViewById(R.id.ll_dots);
        mLayoutManager = new PagerGridLayoutManager(1, 1, PagerGridLayoutManager.HORIZONTAL);
        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(rvQrCode);
        rvQrCode.setLayoutManager(mLayoutManager);
        rvQrCode.setHasFixedSize(true);
        mAdapter.bindToRecyclerView(rvQrCode);
//        rvQrCode.addItemDecoration(new SpacesItemDecoration(10));
        ivQrCodeHide.setOnClickListener(this);
        clQrCode.setOnClickListener(this);
        mLayoutManager.setPageListener(new PagerGridLayoutManager.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {
                mTimer.cancel();
                mTimer.start();
                speakWord = mList.get(pageIndex).getQrWords();
                if (!TextUtils.isEmpty(speakWord)) {
                    EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.ASK_EVENT, speakWord));
                }
                ImageView imageView;
                for (int i = 0; i < llDots.getChildCount(); i++) {
                    imageView = (ImageView) llDots.getChildAt(i);
                    if (pageIndex == i) {
                        imageView.setImageResource(R.drawable.ic_float_qr_code_selected);
                    } else {
                        imageView.setImageResource(R.drawable.ic_float_qr_code_unselected);
                    }
                }
            }
        });
        mWindowManager.addView(mLayout, mParams);
    }

    /**
     * 获取二维码数据
     */
    private void getQrCodeData() {
        ServerFactory.createApi().getQrCode(Robot.SN, new Observer<ResponseBean<List<QrCodeBean>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ResponseBean<List<QrCodeBean>> listResponseBean) {
                mList.clear();
                Constants.QrCode.sFloatQrCodes.clear();
                Constants.QrCode.sPaymentQrCodes.clear();
                if (listResponseBean.getCode() == 200 && listResponseBean.getRows() != null
                        && !listResponseBean.getRows().isEmpty()) {
                    for (QrCodeBean qrCodeBean : listResponseBean.getRows()) {
                        switch (qrCodeBean.getQrCodeUsage()) {
                            case "1"://如果二维码是悬浮使用
                                Constants.QrCode.sFloatQrCodes.add(qrCodeBean);
                                mList.add(qrCodeBean);
                                break;
                            case "2"://如果是查缴指南使用
                                Constants.QrCode.sPaymentQrCodes.add(qrCodeBean);
                                break;
                        }
                    }
                } else {//如果没有二维码，则隐藏悬浮二维码
                    mLayout.setVisibility(View.INVISIBLE);
                    isNoData = true;
                }
                if (!mList.isEmpty()) {//不为空，则显示
                    if (isCanShow) {
                        mLayout.setVisibility(View.VISIBLE);
                    }
                    ivQrCodeHide.setVisibility(View.VISIBLE);
                    clQrCode.setVisibility(View.GONE);
                    mAdapter.setNewData(mList);
                    updateDots();
                    isNoData = false;
                } else {
                    mLayout.setVisibility(View.INVISIBLE);
                    isNoData = true;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ivQrCodeHide.setVisibility(View.GONE);
                clQrCode.setVisibility(View.GONE);
                isNoData = false;
                CsjlogProxy.getInstance().error("请求悬浮二维码数据失败： " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 操作悬浮二维码的相关操作
     *
     * @param event 更新、显示隐藏、弹出等操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void hideFloat(FloatQrCodeEvent event) {
        if (event != null) {
            switch (event.getEventType()) {
                case FloatQrCodeEvent.UPDATE_QR_CODE://更新
                    getQrCodeData();
                    break;
                case FloatQrCodeEvent.SHOW_HIDE_QR_CODE://显示隐藏
                    if (isNoData) {
                        return;
                    }
                    isCanShow = !event.isHide();
                    if (event.isHide()) {
                        if (mLayout.getVisibility() == View.VISIBLE) {
                            mLayout.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if (mLayout.getVisibility() != View.VISIBLE) {
                            mLayout.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case FloatQrCodeEvent.POP_QR_CODE://弹出
                    if (isNoData) {
                        return;
                    }
                    if (mLayout.getVisibility() != View.VISIBLE) {//如果当前父布局不显示的话，直接退出
                        return;
                    }
                    int size = mList.size();
                    QrCodeBean bean;
                    for (int i = 0; i < size; i++) {
                        bean = mList.get(i);
                        if (TextUtils.equals(bean.getQrName(), event.getQrName())) {
                            if (TextUtils.equals(speakWord, bean.getQrWords())) {
                                EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.ASK_EVENT, speakWord));
                            }
                            speakWord = bean.getQrWords();
                            mLayoutManager.scrollToPage(i);
                            break;
                        }
                    }
                    if (clQrCode.getVisibility() != View.VISIBLE) {//如果当前的二维码状态为未显示状态，则显示
                        ivQrCodeHide.setVisibility(View.GONE);
                        clQrCode.setVisibility(View.VISIBLE);
                        mTimer.start();
                    }
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mLayout);
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_qr_code_hide://半隐藏二维码点击
                ivQrCodeHide.setVisibility(View.GONE);
                clQrCode.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(speakWord)) {//话术为空，则不说话
                    EventBus.getDefault().post(new AdvertisementEvent(AdvertisementEvent.ASK_EVENT, speakWord));
                }
                mTimer.start();
                break;
            case R.id.cl_qr_code://二维码点击
                mTimer.cancel();
                clQrCode.setVisibility(View.GONE);
                ivQrCodeHide.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 是否是大屏
     *
     * @return
     */
    private boolean isPlus() {
        return Constants.isPlus();
    }

    private void updateDots() {
        llDots.removeAllViews();
        int pageSize = mList.size();
        if (pageSize > 1) {
            ImageView dot;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;
            params.rightMargin = 10;
            for (int i = 0; i < pageSize; i++) {
                //小圆点
                dot = new ImageView(getApplicationContext());
                dot.setScaleType(ImageView.ScaleType.CENTER_CROP);
                dot.setLayoutParams(params);
                if (i == 0) {
                    dot.setImageResource(R.drawable.ic_float_qr_code_selected);
                } else {
                    dot.setImageResource(R.drawable.ic_float_qr_code_unselected);
                }
                llDots.addView(dot);
            }
        }
    }

    private static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) != 0)
                outRect.left = space;
        }
    }
}
