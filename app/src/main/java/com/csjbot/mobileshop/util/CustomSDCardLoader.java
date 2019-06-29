package com.csjbot.mobileshop.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.csjbot.mobileshop.global.Constants;

import java.io.File;

import skin.support.SkinCompatManager;
import skin.support.content.res.SkinCompatResources;
import skin.support.load.SkinSDCardLoader;
import skin.support.utils.SkinFileUtils;

public class CustomSDCardLoader extends SkinSDCardLoader {
    public static final int SKIN_LOADER_STRATEGY_SDCARD = Integer.MAX_VALUE;

    @Override
    protected String getSkinPath(Context context, String skinName) {
        return new File(Constants.SKIN_PATH, skinName).getAbsolutePath();
    }

//    @Override
//    public Drawable getDrawable(Context context, String skinName, int resId) {
//        String skinPkgPath = getSkinPath(context, skinName);
//        if (SkinFileUtils.isFileExists(skinPkgPath)) {
//            String pkgName = SkinCompatManager.getInstance().getSkinPackageName(skinPkgPath);
//            Resources resources = SkinCompatManager.getInstance().getSkinResources(skinPkgPath);
//            if (resources != null && !TextUtils.isEmpty(pkgName)) {
//                return resources.getDrawable(resId);
//            }
//        }
//        return super.getDrawable(context, skinName, resId);
//    }

    @Override
    public int getType() {
        return SKIN_LOADER_STRATEGY_SDCARD;
    }
}
