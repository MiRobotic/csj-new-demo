package com.csjbot.mobileshop.model.http.product.listeners;

import com.csjbot.mobileshop.cart.entity.RobotMenuListBean;

/**
 * @author : chenqi.
 * @e_mail : 1650699704@163.com.
 * @create_time : 2018/1/23.
 * @Package_name: BlackGaGa
 */

public interface MenuListListener extends BaseBackstageListener{
    void getMenuList(RobotMenuListBean bean);

    void onMenuError(Throwable e);

    void onLocaleMenuList(RobotMenuListBean bean);

    void ImageSize(int num);
}
