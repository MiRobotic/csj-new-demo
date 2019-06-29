package com.csjbot.coshandler.handle_msg.task;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.coshandler.log.CsjlogProxy;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author ShenBen
 * @date 2019/2/19 13:49
 * @email 714081644@qq.com
 */
public class RbCustomer extends RbBase {
    @Override
    protected void handleNTFMessage(String dataSource, String msgId) {

        CsjlogProxy.getInstance().debug("RSPMessage : " + dataSource);

//        case "NTF_HANGUP_HUMAN_SERVICE":
//            try {
//                JSONObject object = new JSONObject(dataSource);
//                int state = object.getInt("state");
//                Robot.getInstance().putCustomerState(1, state);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        break;

    }

    @Override
    protected void handleRSPMessage(String dataSource, String msgId) {

        CsjlogProxy.getInstance().debug("RS123 : " + dataSource);

        switch (msgId) {

            case "RSP_CALL_HUMAN_SERVICE":
                try {
                    JSONObject object = new JSONObject(dataSource);
                    int state = object.getInt("status");
                    if (0 == state){
                        Robot.getInstance().putCustomerState(0, 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "RSP_HANGUP_HUMAN_SERVICE":
                try {
                    JSONObject object = new JSONObject(dataSource);
                    int state = object.getInt("reason");
                    Robot.getInstance().putCustomerState(1, state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case "RSP_HUMAN_SERVICE_STATUS":
                try {
                    JSONObject object = new JSONObject(dataSource);
                    int state = object.getInt("status");
                    Robot.getInstance().putCustomerState(2, state);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
