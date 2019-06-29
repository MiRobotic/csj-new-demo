package com.csjbot.mobileshop.model.http.apiservice.proxy;

import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UnreachablePointBean;
import com.csjbot.mobileshop.model.http.factory.ServerFactory;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 不可到达点工具类
 *
 * @author ShenBen
 * @date 2019/2/21 8:55
 * @email 714081644@qq.com
 */
public class UnreachablePointProxy {
    private UnreachablePointProxy() {

    }

    public static UnreachablePointProxy newInstance() {
        return new UnreachablePointProxy();
    }


    public void getUnreachableProxy(String sn) {
        ServerFactory.createApi().getUnreachablePoint(sn, new Observer<ResponseBean<List<UnreachablePointBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBean<List<UnreachablePointBean>> responseBean) {
                if (responseBean != null && responseBean.getCode() == 200 && !responseBean.getRows().isEmpty()) {
                    Constants.sUnreachablePoints.clear();
                    Constants.sUnreachablePoints.addAll(responseBean.getRows());
                    CsjlogProxy.getInstance().info("不可到达点数量: " + Constants.sUnreachablePoints.size());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

}
