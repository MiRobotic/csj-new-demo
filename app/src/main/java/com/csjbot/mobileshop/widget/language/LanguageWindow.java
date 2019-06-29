package com.csjbot.mobileshop.widget.language;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BaseApplication;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.SplashActivity;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.service.HomeService;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

/**
 * Created by jingwc on 2018/4/4.
 */

public class LanguageWindow extends PopupWindow {

    private Context mContext;

    private LanguageView mLanguageView;

    private int mCurrentLanguage;

    public LanguageWindow(Context context) {
        super(context);
        this.mContext = context;

        setContentView(mLanguageView = new LanguageView(context));

        setWidth(270);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        setAnimationStyle(R.style.language_list_anim_style);

        setBackgroundDrawable(new ColorDrawable());

        setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);

        setTouchInterceptor((v, event) -> event.getAction() == MotionEvent.ACTION_OUTSIDE);

        initView();

        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        mCurrentLanguage = SharedPreUtil.getInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, CsjLanguage.CHINESE);
        mLanguageView.setLanguageClickListener(language -> {
            if (CsjLanguage.CURRENT_LANGUAGE != language) {
                CsjLanguage.CURRENT_LANGUAGE = language;
                SharedPreUtil.putInt(SharedKey.LANGUAGEMODE_NAME, SharedKey.LANGUAGEMODE_KEY, language);
                CsjlogProxy.getInstance().error("language  =" + mCurrentLanguage);
//                refreshLanguage(language);
                /**
                 * 会调用 HomeService
                 * @see HomeService#onStartCommand(android.content.Intent, int, int)
                 */
                jumpSplashAct();
//                ShellUtils.execCommand("am force-stop com.csjbot.blackgaga\nam start -n com.csjbot.blackgaga/com.csjbot.blackgaga.SplashActivity", true, false);

            }

            LanguageWindow.this.dismiss();

            if (Constants.isI18N()) {
                CsjlogProxy.getInstance().info("切换语言:" + CsjLanguage.getISOLanguage(language));
                try {
                } catch (RuntimeException e) {
                    BaseApplication.restartApp();
                }
            }

        });
    }


    private void jumpSplashAct() {
        if (mContext != null) {
            Constants.ChangeLan = true;
            Intent intent = new Intent(mContext, SplashActivity.class);
            intent.putExtra(SplashActivity.START, SplashActivity.HEAT_START);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }

    /**
     * 刷新本地语言
     *
     * @param mode
     */
    private void refreshLanguage(int mode) {
        Configuration config = mContext.getResources().getConfiguration();

        config.locale = CsjLanguage.getLocaleByDef(mode);
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
    }
}
