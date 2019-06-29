package com.csjbot.mobileshop.widget.chat_view.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.SectionMultiEntity;

/**
 * @author ShenBen
 * @date 2019/2/25 9:19
 * @email 714081644@qq.com
 */
public class ChatSectionMultipleItemBean extends SectionMultiEntity<ChatBean> implements MultiItemEntity {
    private int itemType;
    private ChatBean bean;

    public ChatSectionMultipleItemBean(int itemType, ChatBean chatBean) {
        super(chatBean);
        this.itemType = itemType;
        this.bean = chatBean;
    }

    public ChatBean getChatBean() {
        return bean;
    }

    public void setChatBean(ChatBean bean) {
        this.bean = bean;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
