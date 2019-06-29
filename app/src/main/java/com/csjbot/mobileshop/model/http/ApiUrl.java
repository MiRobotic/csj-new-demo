package com.csjbot.mobileshop.model.http;

import com.csjbot.mobileshop.BuildConfig;

/**
 * Created by jingwc on 2017/9/18.
 */

public class ApiUrl {
    //    public static String DEFAULT_ADRESS = "http://118.178.188.27:8080/";
//    public static String DEFAULT_ADRESS = "http://120.27.233.57:8080/";
    // 如果机器人类型是 i18n的，则用i18n的域名访问
    public static String DEFAULT_ADRESS =
            BuildConfig.robotType.contains("i18n") ? BuildConfig.DEFAULT_ADRESS_I18N : BuildConfig.DEFAULT_ADRESS;

    public static String LINUX_ARRESS =
            BuildConfig.robotType.contains("i18n") ? BuildConfig.LINUX_ADRESS_I18N : BuildConfig.LINUX_ADRESS;

    public static String LINUX_PORT = BuildConfig.LINUX_PORT;
}