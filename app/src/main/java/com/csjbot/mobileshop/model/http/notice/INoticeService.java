package com.csjbot.mobileshop.model.http.notice;

import com.csjbot.mobileshop.model.http.bean.NoticeBean;

import io.reactivex.Observer;

/**
 * Created by jingwc on 2018/3/1.
 */
public interface INoticeService {
    void getGlobalAnnouncement(String sn, Observer<NoticeBean> observer);
}
