package com.csjbot.mobileshop.model.http.factory;

import com.csjbot.mobileshop.model.http.advertisement.AdvertisementImpl;
import com.csjbot.mobileshop.model.http.advertisement.IAdvertisement;
import com.csjbot.mobileshop.model.http.apiservice.ApiImpl;
import com.csjbot.mobileshop.model.http.apiservice.IApi;
import com.csjbot.mobileshop.model.http.consult.ConsultImpl;
import com.csjbot.mobileshop.model.http.consult.IConsult;
import com.csjbot.mobileshop.model.http.customerservice.CustomerServiceImpl;
import com.csjbot.mobileshop.model.http.customerservice.ICustomerService;
import com.csjbot.mobileshop.model.http.location.IPosition;
import com.csjbot.mobileshop.model.http.location.PositionImpl;
import com.csjbot.mobileshop.model.http.notice.INoticeService;
import com.csjbot.mobileshop.model.http.notice.NoticeServiceImpl;
import com.csjbot.mobileshop.model.http.product.ProductProxy;
import com.csjbot.mobileshop.model.http.product.TransactionOrder.TransactionOrderImpl;
import com.csjbot.mobileshop.model.http.product.listeners.IProduct;
import com.csjbot.mobileshop.model.http.qrcode.IQrCode;
import com.csjbot.mobileshop.model.http.qrcode.QrCodeImpl;
import com.csjbot.mobileshop.model.http.vip.IVip;
import com.csjbot.mobileshop.model.http.vip.VipImpl;
import com.csjbot.mobileshop.model.http.weather.IWeather;
import com.csjbot.mobileshop.model.http.weather.WeatherImpl;

/**
 * Created by jingwc on 2017/9/18.
 */

public class ServerFactory {

    public static <T extends ProductProxy> T createProduct() {
        return (T) ProductProxy.newProxyInstance();
    }

    public static <T extends IConsult> T createConsult() {
        return (T) new ConsultImpl();
    }

    public static <T extends IProduct> T createProductDetail() {
        return (T) new TransactionOrderImpl();
    }

    public static <T extends IPosition> T createPosition() {
        return (T) new PositionImpl();
    }

    public static <T extends IVip> T createVip() {
        return (T) new VipImpl();
    }


    public static <T extends IWeather> T createWeather() {
        return (T) new WeatherImpl();
    }


    public static <T extends IQrCode> T createQrCode() {
        return (T) new QrCodeImpl();
    }

    public static <T extends ICustomerService> T createCustomerService() {
        return (T) new CustomerServiceImpl();
    }

    public static <T extends INoticeService> T createNoticeSerivce() {
        return (T) new NoticeServiceImpl();
    }

    public static <T extends IApi> T createApi() {
        return (T) new ApiImpl();
    }

    public static <T extends IAdvertisement> T createAdvertisement() {
        return (T) new AdvertisementImpl();
    }
}
