package com.csjbot.mobileshop.model.http.weather;

import com.csjbot.mobileshop.model.http.bean.WeatherBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */
public interface IWeather {
    void getWeather(String sn,String city, Observer<WeatherBean> observer);
}
