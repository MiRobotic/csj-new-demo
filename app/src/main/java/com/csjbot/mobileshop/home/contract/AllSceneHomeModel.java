package com.csjbot.mobileshop.home.contract;

import android.graphics.Bitmap;

import com.csjbot.mobileshop.model.http.bean.rsp.VipGreetBean;

import io.reactivex.functions.Consumer;

/**
 * @author ShenBen
 * @date 2019/1/18 11:42
 * @email 714081644@qq.com
 */

public interface AllSceneHomeModel {
    /**
     * 上传图片到oss
     *
     * @param bitmap   图片bitmap
     * @param faceId   人脸标识
     * @param userName 用户名
     */
    void uploadBitmap(Bitmap bitmap, String faceId, String userName);

    void uploadVisitor(String imageUrl, String faceId, String userName);

    void getVipGreet(String faceId, Consumer<VipGreetBean> onNext, Consumer<Throwable> onError);
}
