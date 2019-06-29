package com.csjbot.mobileshop.model.tcp.version;

/**
 * Created by jingwc on 2017/11/8.
 */

public interface IVersion {
    void getVersion();


    void softwareCheck();

    void softwareUpgrade();
}
