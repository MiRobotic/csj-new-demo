package com.csjbot.cameraclient;


import com.csjbot.cameraclient.constant.ClientConstant;
import com.csjbot.cameraclient.core.ClientManager;
import com.csjbot.cameraclient.core.inter.ActionListener;
import com.csjbot.cameraclient.core.inter.Callbacker;
import com.csjbot.cameraclient.core.util.ClientListenerWrapper;
import com.csjbot.cameraclient.core.util.ExecutorCallbacker;
import com.csjbot.cameraclient.entity.MessagePacket;
import com.csjbot.cameraclient.listener.ClientEvent;
import com.csjbot.cameraclient.listener.CameraEventListener;
import com.csjbot.cameraclient.utils.CameraLogger;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.example.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@example.com
 * <p>
 * 直接面向用户的类，直接对外的封装
 */
public class CameraClientAgent {
    private static CameraClientAgent ourInstance;

    private static final Object lock = new Object();
    private ClientManager mClientManager;
    private CameraEventListener mEventListener;
    private Callbacker mCallbacker;
    private boolean isConnected = false;

    /**
     * 创建client agent 的实例
     *
     * @param listener 所有事件的监听器
     * @param isDebug  是否开启日志
     * @return 返回RosClientAgent 的实例
     * @see ClientConstant
     */
    public static CameraClientAgent createRosClientAgent(CameraEventListener listener, boolean isDebug) {
        CameraLogger.setEnable(isDebug);
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = new CameraClientAgent(listener);
            }
        }

        return ourInstance;
    }

    /**
     * 创建client agent 的实例
     *
     * @param listener 所有事件的监听器
     * @return 返回RosClientAgent 的实例，默认会开启日志
     * @see ClientConstant
     */
    public static CameraClientAgent createRosClientAgent(CameraEventListener listener) {
        return createRosClientAgent(listener, true);
    }

    /**
     * 获取 client agent 实例
     *
     * @return 返回RosClientAgent 的实例
     */
    public static CameraClientAgent getRosClientAgent() {
        if (ourInstance == null) {
            throw new IllegalStateException("Please invoke createAgent firstly");
        }
        return ourInstance;
    }


    public void connect(String hostName, int port) {
        mClientManager.init(hostName, port, new ActionListener() {
            @Override
            public void onSuccess() {
                isConnected = true;
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_SUCCESS));
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_FAILD, errorCode));
            }
        });
    }


    private CameraClientAgent(CameraEventListener listener) {
        mClientManager = ClientManager.getInstance();
        mEventListener = listener;
        mCallbacker = new ExecutorCallbacker("RosClientAgentCallback");
        mClientManager.setRequestListener(new ClientListenerWrapper(mClientManager, mCallbacker, mEventListener));

    }

    /**
     * CameraClientAgent 构造函数， 初始化clientManager，设置 CameraEventListener <br/>
     * 这里会起一个线程 ExecutorCallbacker,处理所有的事件
     *
     * @param hostName 连接的主机名 一般是 192.168.x.3
     * @param port     端口
     * @param listener 监听器
     * @see ClientManager
     * @see CameraEventListener
     */
    private CameraClientAgent(String hostName, int port, CameraEventListener listener) {
        mClientManager = ClientManager.getInstance();
        mEventListener = listener;
        mCallbacker = new ExecutorCallbacker("RosClientAgentCallback");
        mClientManager.setRequestListener(new ClientListenerWrapper(mClientManager, mCallbacker, mEventListener));
        mClientManager.init(hostName, port, new ActionListener() {
            @Override
            public void onSuccess() {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_SUCCESS));
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.EVENT_CONNECT_FAILD, errorCode));
            }
        });
    }

    /**
     * 发送构造好的数据包
     *
     * @param packet 要发送的数据包
     */
    public void sendMessage(final MessagePacket packet) {
        mClientManager.sendRequest(packet, new ActionListener() {

            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailed(int errorCode) {
                notifyEvent(new ClientEvent(ClientConstant.SEND_FAILED, errorCode));
            }
        });
    }

    private void notifyEvent(ClientEvent event) {
        mCallbacker.notifyEvent(mEventListener, event);
    }

    /**
     * 关闭所有的东西
     */
    public void destroy() {
        mClientManager.destroy();
        mCallbacker.destroy();

        mClientManager = null;
        mCallbacker = null;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void disConnect() {
        isConnected = false;
        mClientManager.disConnect();
    }
}
