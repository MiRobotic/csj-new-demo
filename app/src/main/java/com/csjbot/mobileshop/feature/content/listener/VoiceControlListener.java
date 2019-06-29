package com.csjbot.mobileshop.feature.content.listener;

/**
 * @author ShenBen
 * @date 2018/12/14 21:58
 * @email 714081644@qq.com
 */

public interface VoiceControlListener {
    /**
     * 语音控制
     *
     * @param text 用户话术
     * @return 该话术是否被消耗
     */
    boolean voiceControl(String text);
}
