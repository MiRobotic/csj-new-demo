package com.csjbot.mobileshop.widget.chat_view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.advertisement.service.ChatService;
import com.csjbot.mobileshop.widget.chat_view.adapter.ChatAdapter;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatSectionMultipleItemBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBen
 * @date 2019/2/25 9:06
 * @email 714081644@qq.com
 */
public class ChatView extends FrameLayout {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    //    private PictureDialog pictureDialog;
//    private AudioDialog audioDialog;
    private Intent chatVideoServer;
    private HyperlinkDialog hyperlinkDialog;

    public ChatView(@NonNull Context context) {
        this(context, null);
    }

    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ChatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        pictureDialog = new PictureDialog(context);
//        audioDialog = new AudioDialog(context);
        hyperlinkDialog = new HyperlinkDialog(context);

        LayoutInflater.from(context).inflate(R.layout.layout_chat_view, this);
        mRecyclerView = findViewById(R.id.rv_chat_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChatAdapter();
        mAdapter.bindToRecyclerView(mRecyclerView);

        chatVideoServer = new Intent(context, ChatService.class);
        context.startService(chatVideoServer);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            ChatSectionMultipleItemBean itemBean = (ChatSectionMultipleItemBean) adapter.getItem(position);
            if (itemBean == null) {
                return;
            }
            ChatBean bean = itemBean.getChatBean();
            switch (view.getId()) {
                case R.id.iv_picture://图片被点击了
//                        mDialog.setImageUrl(bean.getPictures().get(0).getPictureUrl());
                    List<String> pictures = new ArrayList<>();
                    for (ChatBean.PictureBean pictureBean : bean.getPictures()) {
                        pictures.add(pictureBean.getPictureUrl());
                    }
//                        pictureDialog.setAutoPlay();
//                        pictureDialog.setPictures(pictures);
//                        pictureDialog.show();
                    EventBus.getDefault().post(new ChatVideoEvent(ChatAdapter.ROBOT_CHAT_PICTURE, pictures));
                    break;
                case R.id.chat_view_video:
                case R.id.chat_view_video_icon: {
                    String url = bean.getPictures().get(0).getPictureUrl();
                    EventBus.getDefault().post(new ChatVideoEvent(ChatAdapter.ROBOT_CHAT_VIDEO, url));
//                    EventBus.getDefault().post(new AudioEvent(AudioAction.START));
                }
                break;
                case R.id.chat_view_audio: {
                    String url = bean.getPictures().get(0).getPictureUrl();
                    EventBus.getDefault().post(new ChatVideoEvent(ChatAdapter.ROBOT_CHAT_AUDIO, url));
                }
                break;
                case R.id.chat_view_hyperlink: {
                    String url = bean.getPictures().get(0).getPictureUrl();
                    hyperlinkDialog.setWebViewUrl(url);
                }
                break;
                default:
                    break;

            }
        });
    }

    /**
     * 添加机器人聊天普通文本
     *
     * @param message 内容
     */
    public void addRobotNormalChat(@NonNull CharSequence message) {
        ChatBean bean = new ChatBean(message);
        notifyChatView(ChatAdapter.ROBOT_CHAT_NORMAL, bean);
    }

    /**
     * 添加机器人图片聊天文本
     *
     * @param message  内容
     * @param pictures 图片列表
     */
    public void addRobotVideoChat(@NonNull CharSequence message, @NonNull List<ChatBean.PictureBean> pictures) {
        ChatBean bean = new ChatBean(message, pictures);
        notifyChatView(ChatAdapter.ROBOT_CHAT_VIDEO, bean);
    }

    /**
     * 添加机器人图片聊天文本
     *
     * @param message  内容
     * @param pictures 图片列表
     */
    public void addRobotAudioChat(@NonNull CharSequence message, @NonNull List<ChatBean.PictureBean> pictures) {
        ChatBean bean = new ChatBean(message, pictures);
        notifyChatView(ChatAdapter.ROBOT_CHAT_AUDIO, bean);
    }

    ChatSectionMultipleItemBean preViewItemBean;

    /**
     * 添加机器人图片聊天文本
     */
    public void addRobotPreViewChat() {
        preViewItemBean =
                new ChatSectionMultipleItemBean(ChatAdapter.ROBOT_CHAT_PRE_VIEWER, new ChatBean());
        mAdapter.addData(preViewItemBean);
        mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1);
    }

    /**
     * 添加机器人图片聊天文本
     *
     * @param message 内容
     * @param links   超链接列表
     */
    public void addRobotHyperlinkChat(@NonNull CharSequence message, @NonNull List<ChatBean.PictureBean> links) {
        ChatBean bean = new ChatBean(message, links);
        notifyChatView(ChatAdapter.ROBOT_CHAT_HYPERLINK, bean);
    }

    /**
     * 添加机器人图片聊天文本
     *
     * @param message  内容
     * @param pictures 图片列表
     */
    public void addRobotPictureChat(@NonNull CharSequence message, @NonNull List<ChatBean.PictureBean> pictures) {
        ChatBean bean = new ChatBean(message, pictures);
        notifyChatView(ChatAdapter.ROBOT_CHAT_PICTURE, bean);
    }

    /**
     * 添加人聊天普通文本
     *
     * @param message 内容
     */
    public void addPeopleNormalChat(@NonNull CharSequence message) {
        ChatBean bean = new ChatBean(message);
        notifyChatView(ChatAdapter.PEOPLE_CHAT_NORMAL, bean);
    }

    /**
     * 清空聊天内容
     */
    public void clear() {
        mAdapter.setNewData(null);
    }

    /**
     * 刷新聊天框
     *
     * @param item 新增的内容
     */
    private void notifyChatView(int itemType, ChatBean item) {
        EventBus.getDefault().post(new ChatVideoEvent(ChatAdapter.ROBOT_CHAT_DISMISS, null));
        ChatSectionMultipleItemBean itemBean = new ChatSectionMultipleItemBean(itemType, item);

        ChatSectionMultipleItemBean lastData = mAdapter.getItem(mAdapter.getData().size() - 1);
        if (lastData != null && (lastData.getItemType() == ChatAdapter.ROBOT_CHAT_PRE_VIEWER)) {
            mAdapter.remove(mAdapter.getData().size() - 1);
        }

        mAdapter.addData(itemBean);
        postDelayed(() -> mRecyclerView.smoothScrollToPosition(mAdapter.getData().size() - 1), 200);
    }

}
