package com.csjbot.mobileshop.p2p.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.model.http.bean.rsp.P2PBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;
import com.csjbot.mobileshop.p2p.impl.P2PEventImpl;
import com.peergine.android.livemulti.pgLibLiveMultiCapture;
import com.peergine.plugin.lib.pgLibJNINode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author ShenBen
 * @date 2019/1/29 11:02
 * @email 714081644@qq.com
 */
public class P2PManager {
    private static final String TAG = P2PManager.class.getSimpleName() + "--->";

    private String mUser;
    private String mPassword;
    private String mServerAddress;
    private static final String DEFAULT_SERVER_ADDRESS = "p2p.example.com:7781";
    private static final String DEFAULT_PASSWD = "123456";
    private pgLibLiveMultiCapture mCapture;
    private Context mContext;
    private List<String> mRenderList;
    /**
     * 是否初始化成功
     */
    private boolean isInited = false;
    /**
     *
     */
    private boolean isConnected = false;

    private P2PManager() {
        mRenderList = new ArrayList<>();
        mCapture = new pgLibLiveMultiCapture();
        pgLibLiveMultiCapture.OnEventListener mListener = new P2PEventImpl();
        mCapture.SetEventListener(mListener);
    }

    private static final class Holder {
        @SuppressLint("StaticFieldLeak")
        private static final P2PManager INSTANCE = new P2PManager();
    }

    public static P2PManager newInstance() {
        return Holder.INSTANCE;
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * 初始化
     */
    public void init() {
        //先获取人工客服账号密码
        ServerFactory.createApi().getP2PCount(Robot.SN, new Observer<P2PBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(P2PBean p2PBean) {
                if (p2PBean != null && p2PBean.getCode() == 200) {
                    mUser = p2PBean.getCaptureid();
                    mPassword = p2PBean.getPassword();
                    mServerAddress = p2PBean.getServerAddr();
                }
                if (TextUtils.isEmpty(mUser) || TextUtils.isEmpty(mPassword) || TextUtils.isEmpty(mServerAddress)) {
                    CsjlogProxy.getInstance().error(TAG + "人工客服必要参数为空，请检查接口");
                    return;
                }
                //连接
                if (!checkPlugin()) {
                    return;
                }
                int code = mCapture.Initialize(mUser, mPassword, mServerAddress, "",
                        2, "", mContext);
                CsjlogProxy.getInstance().warn(TAG + "LiveStart: Live.Initialize failed! iErr = " + code);
                if (code != 0) {
                    return;
                }
                isInited = true;
            }

            @Override
            public void onError(Throwable e) {
                CsjlogProxy.getInstance().error(TAG + "获取人工客服接口请求失败： error： " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 发送消息
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        mCapture.NotifySend(message);
    }


    public void clean() {
        mCapture.Clean();
        mContext = null;
    }

    /**
     * 检查插件
     *
     * @return
     */
    private boolean checkPlugin() {
        if (pgLibJNINode.Initialize(mContext)) {
            pgLibJNINode.Clean();
            return true;
        } else {
            CsjlogProxy.getInstance().error(TAG + "Please import 'pgPluginLib' peergine middle ware!");
            return false;
        }
    }
}
