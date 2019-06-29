package com.csjbot.mobileshop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.global.Constants;

/**
 * Created by jingwc on 2017/11/10.
 */

public class LoadMapFileDialog extends Dialog {

    public LoadMapFileDialog(@NonNull Context context) {
        super(context,R.style.NewRetailDialog);
        initView();
    }


    private void initView() {

        setContentView(R.layout.dialog_load_map_fail);
        setCanceledOnTouchOutside(false);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (Constants.isPlus()) {//大屏
            lp.width = (int) (d.widthPixels * 0.8);
            lp.height = (int) (d.heightPixels * 0.3);
        } else {
            lp.width = (int) (d.widthPixels * 0.4);
            lp.height = (int) (d.heightPixels * 0.5);
        }
        dialogWindow.setAttributes(lp);

    }

}
