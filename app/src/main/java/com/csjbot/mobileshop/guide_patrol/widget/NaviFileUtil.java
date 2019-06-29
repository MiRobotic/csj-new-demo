package com.csjbot.mobileshop.guide_patrol.widget;

import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.util.StrUtil;

/**
 * Created by 孙秀艳 on 2019/1/18.
 */

public class NaviFileUtil {

    /** 根据文件名判断文件是不是图片*/
    public static boolean isImageFile(String filename) {
        boolean isImage = false;
        if (StrUtil.isBlank(filename)) {
            return isImage;
        }
        String[] strArray = filename.split("\\.");
        if (strArray.length == 2) {
            for (int i=0; i< Constants.ResourceSuffix.imageSuffix.length; i++) {
                if (Constants.ResourceSuffix.imageSuffix[i].equalsIgnoreCase(strArray[1])) {
                    isImage = true;
                    break;
                }
            }
        }
        return isImage;
    }

    /** 根据文件名判断文件是不是视频*/
    public static boolean isVideoFile(String filename) {
        boolean isVideo = false;
        if (StrUtil.isBlank(filename)) {
            return isVideo;
        }
        String[] strArray = filename.split("\\.");
        if (strArray.length == 2) {
            for (int i = 0; i< Constants.ResourceSuffix.videoSuffix.length; i++) {
                if (Constants.ResourceSuffix.videoSuffix[i].equalsIgnoreCase(strArray[1])) {
                    isVideo = true;
                    break;
                }
            }
        }
        return isVideo;
    }
}

