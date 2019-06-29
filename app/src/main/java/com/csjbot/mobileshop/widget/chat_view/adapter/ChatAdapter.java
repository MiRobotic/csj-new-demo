package com.csjbot.mobileshop.widget.chat_view.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseSectionMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.GlideApp;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatBean;
import com.csjbot.mobileshop.widget.chat_view.bean.ChatSectionMultipleItemBean;

import java.io.File;

import jaygoo.widget.wlv.WaveLineView;

/**
 * @author ShenBen
 * @date 2019/2/25 9:17
 * @email 714081644@qq.com
 */
public class ChatAdapter extends BaseSectionMultiItemQuickAdapter<ChatSectionMultipleItemBean, BaseViewHolder> {
    /**
     *
     */
    public static final int ROBOT_CHAT_PRE_VIEWER = 0;

    /**
     * 机器人对话普通文本
     */
    public static final int ROBOT_CHAT_NORMAL = 1;

    /**
     * 机器人对话折叠文本
     */
    public static final int ROBOT_CHAT_FOLD = 2;

    /**
     * 机器人对话带图片文本
     */
    public static final int ROBOT_CHAT_PICTURE = 3;

    /**
     * 机器人对话带带视频文本
     */
    public static final int ROBOT_CHAT_VIDEO = 4;

    /**
     * 机器人对话超链接文本
     */
    public static final int ROBOT_CHAT_HYPERLINK = 5;

    /**
     * 机器人对话天气文本
     */
    public static final int ROBOT_CHAT_WEATHER = 6;

    /**
     * 机器人对话天气文本
     */
    public static final int ROBOT_CHAT_AUDIO = 7;

    /**
     * 人对话普通文本
     */
    public static final int PEOPLE_CHAT_NORMAL = 99;

    /**
     * 退出
     */
    public static final int ROBOT_CHAT_DISMISS = -1;

    public ChatAdapter() {
        super(0, null);
        addItemType(ROBOT_CHAT_PRE_VIEWER, R.layout.item_chat_robot_pre_viewer);
        addItemType(ROBOT_CHAT_NORMAL, R.layout.item_chat_robot_normal);
//        addItemType(ROBOT_CHAT_FOLD, R.layout.item_chat_robot_fold);
        addItemType(ROBOT_CHAT_PICTURE, R.layout.item_chat_robot_picture);
        addItemType(ROBOT_CHAT_VIDEO, R.layout.item_chat_robot_video);
        addItemType(ROBOT_CHAT_AUDIO, R.layout.item_chat_robot_audio);
        addItemType(ROBOT_CHAT_HYPERLINK, R.layout.item_chat_robot_hyperlink);
//        addItemType(ROBOT_CHAT_WEATHER, R.layout.item_chat_robot_weather);

        addItemType(PEOPLE_CHAT_NORMAL, R.layout.item_chat_people_normal);

    }

    @Override
    protected void convertHead(BaseViewHolder holder, ChatSectionMultipleItemBean item) {

    }

    @Override
    protected void convert(BaseViewHolder holder, ChatSectionMultipleItemBean item) {
        ChatBean bean = item.getChatBean();

        holder.setText(R.id.tv_msg, bean.getMessage());
        if (holder.getItemViewType() == PEOPLE_CHAT_NORMAL) {//用户聊天内容
            if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.JiuDianScene)
                    || TextUtils.equals(Constants.Scene.CurrentScene, "qiche")) {
                holder.setTextColor(R.id.tv_msg, Color.parseColor("#0c0c0c"));
            }
//            holder.setImageResource(R.id.iv_portrait, R.drawable.iv_customer_portrait);
        } else {//机器人回答内容
            //设置机器人聊天头像
            if (TextUtils.equals(Constants.Scene.CurrentScene, Constants.Scene.JiuDianScene)
                    || TextUtils.equals(Constants.Scene.CurrentScene, "qiche")
                    || TextUtils.equals(Constants.Scene.CurrentScene, "chezhan")
                    || TextUtils.equals(Constants.Scene.CurrentScene, "yiyuan")){
                holder.setTextColor(R.id.tv_msg, Color.parseColor("#ffffff"));
            }
            switch (BuildConfig.robotType) {
                case BuildConfig.ROBOT_TYPE_DEF_SNOW:
                case BuildConfig.ROBOT_TYPE_DEF_SNOW_i18n:
                    holder.setImageResource(R.id.iv_portrait, R.drawable.iv_robot_portrait_snow);
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_ALICE:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_i18n:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_BAIDU:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS:
                case BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS_i18n:
                    holder.setImageResource(R.id.iv_portrait, R.drawable.iv_robot_portrait);
                    break;
                case BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS:
                case BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS_i18n:
                    holder.setImageResource(R.id.iv_portrait, R.drawable.iv_robot_portrait_amy);
                    break;
                default:
                    // 默认是用爱丽丝
                    holder.setImageResource(R.id.iv_portrait, R.drawable.iv_robot_portrait);
                    break;
            }
            switch (holder.getItemViewType()) {
                case ROBOT_CHAT_PRE_VIEWER:
                    CsjlogProxy.getInstance().info("ROBOT_CHAT_PRE_VIEWER");
                    WaveLineView wave_line_view = holder.getView(R.id.wave_line_view);
                    wave_line_view.startAnim();
                    break;
                case ROBOT_CHAT_FOLD:
                    break;
                case ROBOT_CHAT_PICTURE:
                    holder.addOnClickListener(R.id.iv_picture);
                    if (!bean.getPictures().isEmpty()) {
                        String str = bean.getPictures().get(0).getPictureUrl() + "@w_200,q_80";
                        ImageView iv = holder.getView(R.id.iv_picture);
                        if (!TextUtils.isEmpty(str)) {
                            GlideApp.with(iv)
                                    .asBitmap()
                                    .load(str)
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(new SimpleTarget<Bitmap>(250, 250) {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            iv.setImageBitmap(resource);
                                        }
                                    });
                        } else {
                            str = bean.getPictures().get(0).getPicturePath();
                            File file = new File(str);
                            if (file.exists()) {
                                GlideApp.with(iv)
                                        .load(file)
                                        .skipMemoryCache(true)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(iv);
                            }
                        }
                    }
                    break;
                case ROBOT_CHAT_VIDEO:
                    holder.addOnClickListener(R.id.chat_view_video);
                    holder.addOnClickListener(R.id.chat_view_video_icon);
                    break;
                case ROBOT_CHAT_HYPERLINK:
                    holder.addOnClickListener(R.id.chat_view_hyperlink);
                    break;
                case ROBOT_CHAT_WEATHER:
                    break;
                case ROBOT_CHAT_AUDIO:
                    holder.addOnClickListener(R.id.chat_view_audio);
                    break;
                default:
                    break;
            }
        }
    }
}
