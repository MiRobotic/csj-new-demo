package com.csjbot.mobileshop.jpush.diaptch.handler;

import com.csjbot.coshandler.client_req.ReqFactory;
import com.csjbot.coshandler.core.Robot;
import com.csjbot.mobileshop.advertisement.event.FloatQrCodeEvent;
import com.csjbot.mobileshop.advertisement.manager.AdvertisementManager;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.jpush.diaptch.CsjPushDispatch;
import com.csjbot.mobileshop.jpush.diaptch.constants.ConstantsId;
import com.csjbot.mobileshop.jpush.manager.CsjJPushManager;
import com.csjbot.mobileshop.model.http.apiservice.proxy.LogoProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.MenuProxy;
import com.csjbot.mobileshop.model.http.apiservice.proxy.UnreachablePointProxy;
import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.network.ShellUtil;
import com.csjbot.mobileshop.util.FileUtil;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by jingwc on 2018/3/2.
 */

public class MessageHandler implements Runnable {

    private String json;

    public MessageHandler(String json) {
        this.json = json;
    }

    @Override
    public void run() {
        try {
            JSONObject object = new JSONObject(json);
            String id = object.getString("msg_id");
            int sid = object.getInt("sid");
            String data = object.getJSONObject("data").toString();
            if (id.contains(ConstantsId.GlobalNoticeId.TAG)) {
                CsjPushDispatch.getInstance().pushGlobalNoticeMessage(id, sid, data);
            } else {
                switch (id) {
                    case ConstantsId.ExpressionId.EXPRESSION_UPDATE://表情变化
                        if (!Robot.isLinuxConnect) {
                            CsjJPushManager.getInstance().addSendFailedReq(json);
                        } else {
                            ServerFactory.getExpressionInstantce().updateExpression();
                        }
                        break;
                    case ConstantsId.UserInformationId.NOTICE_FACE_INFO_ADD://会员信息新增
                    case ConstantsId.UserInformationId.NOTICE_FACE_INFO_CHANGED://会员信息变化
                        if (!Robot.isLinuxConnect) {
                            CsjJPushManager.getInstance().addSendFailedReq(json);
                        } else {
                            JSONObject jo = object.getJSONObject("data");
                            JSONArray array = jo.getJSONArray("reguserId");
                            String status = jo.getString("status");
                            CsjlogProxy.getInstance().info("会员信息变化 : " + array.toString());
                            ServerFactory.getFaceInstance().faceInfoAdd(array.toString(), status);
                        }
                        break;
                    case ConstantsId.UserInformationId.NOTICE_FACE_INFO_DELETE://会员信息删除
                        if (!Robot.isLinuxConnect) {
                            CsjJPushManager.getInstance().addSendFailedReq(json);
                        } else {
                            JSONObject jo = object.getJSONObject("data");
                            JSONArray array = jo.getJSONArray("faceId");
                            CsjlogProxy.getInstance().info("会员信息删除 : " + array.toString());
                            ServerFactory.getFaceInstance().faceDelList(array.toString());
                        }
                        break;
                    case ConstantsId.AllContentTypesChanged.NOTICE_ALL_CONTENT_TYPES_CHANGED://内容管理变化通知
                        MenuProxy.newInstance().getAllContentType(true);
                        break;
                    case ConstantsId.AdvertisementChanged.NOTICE_ADVERTISEMENT_CHANGED://广告变化
                        AdvertisementManager.getInstance().updateData();
                        break;
                    case ConstantsId.HomeModule.NOTICE_HOME_MODULE_CHANGED://主页模块变化
                        MenuProxy.newInstance().getModule();
                        break;
                    case ConstantsId.HomeModule.ROBOT_NAME_UPDATE://机器人名称变化
                    case ConstantsId.HomeModule.GREET_UPDATE://自定义问候语变化
                        MenuProxy.newInstance().getGreetContent();
                        break;
                    case ConstantsId.HomeModule.LOGO_UPDATE://LOGO变化通知
                        LogoProxy.newInstance().getLogo();
                        break;
                    case ConstantsId.Scene.SCENE_CHANGE://场景变化
                        CsjPushDispatch.getInstance().pushSceneMessage(id, sid, data);
                        break;
                    case "NOTICE_ALL_QR_CHANGED"://二维码变化
                        EventBus.getDefault().post(new FloatQrCodeEvent(FloatQrCodeEvent.UPDATE_QR_CODE));
                        break;
                    case "NOTICE_ROBOT_RESET"://出厂设置
                        //内容管理缓存移除
                        SharedPreUtil.removeString(SharedKey.CONTENT_NAME);
                        //场景缓存清除
                        SharedPreUtil.removeString(SharedKey.MAINPAGE);
                        //位置相关
                        SharedPreUtil.removeString(SharedKey.LOCATION);
                        //主页模块
                        SharedPreUtil.removeString(SharedKey.HOME_MODULE);
                        //敏感词
                        SharedPreUtil.removeString(SharedKey.SENSITIVE_WORDS);
                        //默认问候语
                        SharedPreUtil.removeString(SharedKey.ROBOT_DEFAULT_GREET_CONTENT);
                        //删除logo
                        FileUtil.deleteFile(Constants.Path.LOGO_PATH + Constants.Path.LOGO_FILE_NAME);
                        //删除电量设置相关
                        SharedPreUtil.removeString(SharedKey.CHARGING_NAME);
                        if (ServerFactory.getSpeakInstance().isSpeaking()) {
                            ServerFactory.getSpeakInstance().stopSpeaking();
                        }
                        ServerFactory.getSpeakInstance().startSpeaking("解绑机器人！机器人应用即将重启！", null);
                        Constants.clear();
                        ReqFactory.getExtraFunctionInstance().resetRobot();//重置机器人信息
                        Thread.sleep(5000);
                        ShellUtil.execCmd("am force-stop com.csjbot.mobileshop\n  " +
                                        "am start -n com.csjbot.mobileshop/com.csjbot.mobileshop.SplashActivity",
                                true, false);
                        break;
                    case "NOTICE_UNREACHABLE_NAME_CHANGED"://不可到达点更新
                        UnreachablePointProxy.newInstance().getUnreachableProxy(Robot.SN);
                        break;
                }
            }
        } catch (Exception e) {
            CsjlogProxy.getInstance().error("e:" + e.toString());
        }
    }
}
