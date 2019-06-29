package com.csjbot.mobileshop.model.http.logo;


import com.csjbot.mobileshop.model.http.bean.LogoBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jingwc on 2018/3/22.
 */

public interface LogoApiService {
    /**
     * 请求 LOGO 图标
     *
     * @param SN sn参数
     */
    @GET("csjbotservice/api/getLogoTitle/")
    Observable<LogoBean> getLogo(@Query("sn") String SN);
}
