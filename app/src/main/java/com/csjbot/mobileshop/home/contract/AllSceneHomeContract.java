package com.csjbot.mobileshop.home.contract;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;

import java.util.List;

/**
 * @author ShenBen
 * @date 2019/1/18 9:23
 * @email 714081644@qq.com
 */

public interface AllSceneHomeContract {
    interface View {

        void setPresenter(Presenter presenter);

        RecyclerView getRecycler();

        LinearLayout getDots();

        void prattleText(String text);

        void setRobotChatText(String text);

        void speakText(String text);

        boolean isSpeaking();

        void restoreMapDialog();

        void setChatPicture(String msg, List<ChatBean.PictureBean> list);

    }

    interface Presenter {
        /**
         * 初始化
         */
        void init();

        /**
         * 设置场景
         *
         * @param sceneKey 场景标识
         */
        void setScene(String sceneKey);

        void initData();

        void resume();

        /**
         * 语音跳转
         *
         * @param text 客户说的话
         * @return 是否进行跳转
         */
        boolean speakToIntent(String text);

        void loadMap();

        void pause();

        void destroy();

    }
}
