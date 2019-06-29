package com.csjbot.mobileshop.model.http.factory;

import android.util.Log;

import com.csjbot.coshandler.core.Robot;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.model.http.ApiUrl;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    /* 使用自定义的OKHttpClient */
    private static OkHttpClient httpClient;

    /**
     * 初始化OKHttpClient
     */
    public static void initClient() {
        // 系统日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 设置日志级别
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        // 自定义OkhttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS);
        // 添加拦截器
        client.addInterceptor(chain -> {
            // 1、取得本地时间：
            Calendar cal = Calendar.getInstance();
            // 2、取得时间偏移量：
            int timeOffSet = cal.get(Calendar.ZONE_OFFSET);
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .addHeader("sn", Robot.SN)
                    .addHeader("language", CsjLanguage.getISOLanguage())
                    .addHeader("timeOffset", String.valueOf(timeOffSet))
                    .build();
            client.addInterceptor(loggingInterceptor);
            return chain.proceed(request);
        });
        httpClient = client.build();
    }

    /**
     * 构建一个retrofit对象
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .baseUrl(ApiUrl.DEFAULT_ADRESS)
                .build();
        return retrofit.create(service);
    }

    /**
     * 构建一个retrofit对象
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createNew(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .baseUrl("http://dev.example.com:1234567890/")
                .build();
        return retrofit.create(service);
    }
}
