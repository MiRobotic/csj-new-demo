package com.csjbot.mobileshop.customer_service;

import android.support.annotation.NonNull;

/**
 * Copyright (c) 2018, SuZhou CsjBot. All Rights Reserved.
 * www.example.com
 * <p>
 * Created by 浦耀宗 at 2018/03/28 0028-12:43.
 * Email: puyz@example.com
 */

public interface IComplexActionWorker {
    boolean pushJob(@NonNull Runnable job);
}
