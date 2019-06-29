package com.csjbot.mobileshop.model.http.product.TransactionOrder;

import com.csjbot.mobileshop.model.http.base.BaseImpl;
import com.csjbot.mobileshop.model.http.bean.OrderBean;

import io.reactivex.Observer;

/**
 * Created by 孙秀艳 on 2017/10/23.
 */

public class TransactionOrderImpl extends BaseImpl implements ITransactionOrder {

    @Override
    public void getProductDetailInfo(Observer<OrderBean> observer) {
        scheduler(getRetrofit().getProductDetailInfo()).subscribe(observer);
    }

    @Override
    public TransactionOrderService getRetrofit() {
        return getRetrofit(TransactionOrderService.class);
    }
}
