package com.csjbot.mobileshop.feature.take_number;

import com.csjbot.mobileshop.model.tcp.factory.ServerFactory;
import com.csjbot.mobileshop.model.tcp.print.IPrint;

/**
 * Created by jingwc on 2017/9/26.
 */

public class TakeNumberPresenter implements TakeNumberContract.Presenter {

    TakeNumberContract.View view;

    IPrint print;

    public TakeNumberPresenter(){
        print = ServerFactory.getPrintInstance();
    }

    @Override
    public TakeNumberContract.View getView() {
        return view;
    }

    @Override
    public void initView(TakeNumberContract.View view) {
        this.view = view;
    }

    @Override
    public void releaseView() {
        if(view != null){
            this.view = null;
        }
    }

    @Override
    public void takeNumber() {
        String text = "123";
//        print.print(text);
//        SpeakProxy.getInstance().startSpeaking("您好,你的号码为"+text,null);
    }
}
