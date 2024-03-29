package com.csjbot.mobileshop.model.http.qrcode;

import com.csjbot.mobileshop.model.http.bean.PayQrCodeBean;
import com.csjbot.mobileshop.model.http.bean.QrCodeBean;

import io.reactivex.Observer;

/**
 * Created by  Wql , 2018/3/1 18:13
 */
public interface IQrCode {
    void getQrCode(String sn, Observer<QrCodeBean> ob);

    void getPayQrCode(String sn,Observer<PayQrCodeBean> observer);
}
