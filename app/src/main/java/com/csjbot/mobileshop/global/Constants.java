package com.csjbot.mobileshop.global;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.alibaba.android.arouter.launcher.ARouter;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.BaseApplication;
import com.csjbot.mobileshop.BuildConfig;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.base.test.Intents;
import com.csjbot.mobileshop.bean.ExpressionControlBean;
import com.csjbot.mobileshop.cart.entity.RobotSpListBean;
import com.csjbot.mobileshop.feature.product.ProductDetailsActivity;
import com.csjbot.mobileshop.feature.product.ProductListActivity;
import com.csjbot.mobileshop.localbean.NaviBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsTypeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GreetContentBean;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SensitiveWordsBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UnreachablePointBean;
import com.csjbot.mobileshop.model.http.product.ProductProxy;
import com.csjbot.mobileshop.router.BRouterPath;
import com.csjbot.mobileshop.util.GsonUtils;
import com.csjbot.mobileshop.util.NumberUtils;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by jingwc on 2017/10/14.
 */

public class Constants {

    // 上身板
    public static final String UP_PLATE = "up_sn";
    // 下身板
    public static final String DOWN_PLATE = "down_plate";
    // 上位机
    public static final String UP_COMPUTER = "up_computer";
    // 导航核心板
    public static final String NAV = "nav";
    // SN
    public static final String SN = "sn";
    // 与Linux连接状态
    public static final String CONNECT_LINUX_BROADCAST = "linux_broadcast";
    public static final String LINUX_CONNECT_STATE = "linux_connect_state";

    // 更新LOGO
    public static final String UPDATE_LOGO = "update_logo";

    public static final String PRODUCT_IMG_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "product_image" + File.separator;//产品图片路径（包括轮播图片）
    public static final String PRODUCT_VIDEO_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "product_video" + File.separator;//产品视频路径

    public static final String NAVI_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "navi" + File.separator;//导航音乐背景存放路径

    public static final String SKIN_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "skins" + File.separator;//皮肤包存放路径

    public static final String LOGO_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "logo" + File.separator;//logo存放路径

    public static final String WEB_CACHE_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "cache" + File.separator;//web缓存路径

    public static final String APP_PATH = Environment.getExternalStorageDirectory() +
            File.separator + "blackgaga" + File.separator + "app" + File.separator;//APP下载路径

    public static final String MEDIA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() +
            File.separator + "blackgaga" + File.separator + "media" + File.separator;//媒体宣传路径下载路径

    // u盘路径
    public static final String USB_PATH = "/mnt/usb_storage";
    // 导航数据存放路径
    public static String NAV_DATA_PATH = Environment.getExternalStorageDirectory() + File.separator + "blackgaga"
            + File.separator + "nav_data";
    public static String[] imageSuffixs = {
            ".jpg", ".jpeg", ".png", "gif"
    };
    public static String[] suffixs = {
            ".jpg", ".jpeg", ".png", "gif", "avi", "wma", "rmvb", "rm", "flash", "mp4", "mid", "3gp", "mov", "mkv"
    };

    //资源后缀
    public static class ResourceSuffix {
        public enum suffix {
            MUSIC,
            IMAGE,
            VIDEO,
            IMAGEANDVIDEO,
            TXT
        }

        public static final String[] musicSuffix = {"mp3"};
        public static final String[] imageSuffix = {"jpeg", "jpg", "png"};
        public static final String[] videoSuffix = {"3gp", "mkv", "mp4"};
        public static final String[] imageAndVideoSuffix = {"jpeg", "jpg", "png", "3gp", "mkv", "mp4"};
        public static final String[] txtSuffix = {"txt"};
    }

    //导航界面和导航设置界面，地图坐标系数
    public static final float MULTIPAL_DATA = 1.2f;//长
    public static final float MULTIPAL_DATA_WIDTH = 1.2f;//宽
    public static final float MULTIPAL_DATA_VERTICAL = 1.5f;//竖屏长
    public static final float MULTIPAL_DATA_WIDTH_VERTICAL = 1.23f;//竖屏宽
    // 搜索成功
    public static final int SEARCH_SUC = 0;
    // 复制成功
    public static final int COPY_SUC = 10;

    public static boolean isLoadMapSuccess = false;//地图是否是恢复成功
    public static long internalCheckLinux = 8000;//地图操作超时时间

    //声音
    public final static String WAV_PATH = "wav/";
    public final static String VOLUME_PATH = WAV_PATH + "volume.wav";

    public final static String KB = "KB";
    public final static String MB = "MB";
    public final static String GB = "GB";
    public final static String TB = "TB";
    public final static int descLimit = 10;//导航到点讲解大小设置 10kb
    public final static int inRunningLimit = 10;//导航途中讲解大小设置 10kb
    public final static int musicLimit = 5;//导航背景音乐大小设置 5Mb
    public final static int videoLimit = 300;//导航过程视频  300Mb
    public static boolean isNaviMove = false;//是不是在导航

    public static boolean ChangeLan = false;//是否正在切换语言


    // 是否是设置一键导览
    public static final String GUIDE_ALL = "guide_all";

    // 是否跳转其他界面
    public static final String JUMP_ACTIVITY_NAME = "jump_activity_name";

    public static final String WAKE_UP = "wake_up";

    public static final class Face {
        public static boolean person = false;
    }

    /**
     * 充电信息
     */
    public static final class Charging {

        /**
         * 低电量
         */
        private static int lowElectricity;

        /**
         * 是否自动充电
         */
        private static boolean isAutoCharging;

        /**
         * 是否使用充电桩
         */
        private static boolean isChargingPile;

        /**
         * 初始化本地电量信息
         */
        public static void initCharging() {
            isChargingPile = SharedPreUtil.getBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_PILE_KEY, false);
            isAutoCharging = SharedPreUtil.getBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_AUTO, true);
            lowElectricity = SharedPreUtil.getInt(SharedKey.CHARGING_NAME, SharedKey.CHARGING_LOW_ELECTRICITY, 20);
        }

        public static int getLowElectricity() {
            return lowElectricity;
        }

        public static void saveLowElectricity(int electricity) {
            SharedPreUtil.putInt(SharedKey.CHARGING_NAME, SharedKey.CHARGING_LOW_ELECTRICITY, electricity);
            lowElectricity = electricity;
        }

        public static void saveAutoCharging(boolean b) {
            SharedPreUtil.putBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_AUTO, b);
            isAutoCharging = b;
        }

        public static boolean getAutoCharging() {
            return isAutoCharging;
        }

        public static void saveChargingPile(boolean b) {
            SharedPreUtil.putBoolean(SharedKey.CHARGING_NAME, SharedKey.CHARGING_PILE_KEY, b);
            isChargingPile = b;
        }

        public static boolean getChargingPile() {
            return isChargingPile;
        }

        /**
         * 是否达到低电量回充的条件
         *
         * @param electricity
         * @return
         */
        public static boolean isGoCharging(int electricity) {
            if (lowElectricity >= electricity) {
                return true;
            } else {
                return false;
            }
        }

    }

    public static final class NearbyKeyWord {

        public static String[] intents = new String[]{
                "附近",
                "哪边",
                "哪里有"
        };

        public static String[] keyWords = new String[]{
                "美食",
                "景点",
                "休闲娱乐",
                "共享单车",
                "超市",
                "ATM",
                "厕所",
                "快捷酒店",
                "酒店",
                "网吧",
                "地铁",
                "加油站"
        };
    }

    public static final class ProductKeyWord {
        public static String[] intents = new String[]{
                "我想买", "我想要", "我要", "我买", "给我来", "给我拿"
        };

        public static List<RobotSpListBean.ResultBean.ProductBean> products;

        public static void initKeywords() {
            products = ProductProxy.newProxyInstance().getAllSpInformation();
            CsjlogProxy.getInstance().info("products:" + products.size());
        }

    }

    /**
     * 定位信息类
     */
    public static final class LocationInfo {

        // 纬度
        public static double latitude = 0;

        // 经度
        public static double longitude = 0;

        // 定位精度
        public static float radius = 0;

        // 地址
        public static String address;

        // 城市
        public static String city;
    }

    public static class NaviType {
        public static String Guide = "guide";//引领带路
        public static String Navigation = "Navigation";//展览讲解
        public static String Patrol = "patrol";//大厅巡视
        public static String none = "none";
    }

    public static class Scene {

        public static String CurrentScene = "";

        public static final String JiuDianScene = "jiudian";

        public static final String YinHangScene = "yinhang";

        public static final String JiChangScene = "jichang";

        public static final String XianYangJiChang = "xianyangjichang";

        public static final String XingZheng = "xingzheng";

        public static final String CheGuanSuo = "cheguansuo";

        public static final String ShuiWu = "shuiwu";

        public static final String Fuzhuang = "fuzhuang";

        public static final String Shebao = "shebao";

        public static final String Dianli = "dianli";

        public static final String Chezhan = "Chezhan";

        public static final String Zhanguan = "zhanguan";

        public static final String Qiantai = "qiantai";
    }

    public static class VirtualKey {
        public static boolean isShow = false;
    }

    public static class HomePage {
        public static boolean isHomePageLoadSuccess;
    }

    public static class Path {
        public static final String EXPRESSION_CONFIG_PATH = Environment.getExternalStorageDirectory() +
                File.separator + "blackgaga" + File.separator + "expression" + File.separator;//表情配置文件路径

        public static final String LOGO_PATH = Environment.getExternalStorageDirectory() +
                File.separator + "blackgaga" + File.separator + "logo" + File.separator;//logo存放路径

        public static final String EXPRESSION_CONFIG_FILE_NAME = "expression.txt";

        public static final String LOGO_FILE_NAME = "logo.jpg";
    }

    public static ExpressionControlBean expressionControlBean;


    public static class UnknownProblemAnswer {

        public static boolean isUseDefaultAnswer = true;

        static List<String> defaultAnswers = new ArrayList<>();


        static String getDefaultAnser() {
            if (defaultAnswers.size() <= 0) {
                defaultAnswers.add(BaseApplication.getAppContext().getString(R.string.default_answer));
                defaultAnswers.add(BaseApplication.getAppContext().getString(R.string.default_answer1));
                defaultAnswers.add(BaseApplication.getAppContext().getString(R.string.default_answer2));
                defaultAnswers.add(BaseApplication.getAppContext().getString(R.string.default_answer3));
                defaultAnswers.add(BaseApplication.getAppContext().getString(R.string.default_answer4));
            }

            try {
                CsjlogProxy.getInstance().info("getDefaultAnser:");
                int random = new Random().nextInt(defaultAnswers.size());
                if (random >= 0 && random < defaultAnswers.size()) {
                    return defaultAnswers.get(random);
                }
                return defaultAnswers.get(0);
            } catch (IndexOutOfBoundsException e) {
                return BaseApplication.getAppContext().getString(R.string.default_answer);
            }
        }

        static String getCustomerAnser() {
            if (customerAnswers.size() == 0) {
                return getDefaultAnser();
            }
            CsjlogProxy.getInstance().info("getCustomerAnser:");
            int random = (int) (Math.random() * customerAnswers.size());
            if (random >= 0 && random < customerAnswers.size()) {
                return customerAnswers.get(random);
            }
            return getDefaultAnser();
        }

        public static String getAnswer() {
            CsjlogProxy.getInstance().info("isUseDefaultAnswer:" + isUseDefaultAnswer);
            if (isUseDefaultAnswer) {
                return getDefaultAnser();
            } else {
                return getCustomerAnser();
            }
        }


        static List<String> customerAnswers = new ArrayList<>();

        public static void refreshCustomerAnswers(List<String> answers) {
            customerAnswers.clear();
            customerAnswers.addAll(answers);
        }

        public static void initUnknownProblemAnswer() {
            isUseDefaultAnswer = SharedPreUtil.getBoolean(SharedKey.UNKNOWN_PROBLEM_ANSWER, SharedKey.UNKNOWN_PROBLEM_ANSWER_IS_USE_DEFAULT, true);
            String customerAnswerJson = SharedPreUtil.getString(SharedKey.UNKNOWN_PROBLEM_ANSWER, SharedKey.UNKNOWN_PROBLEM_ANSWER_CUSTOMER_ANSWER);
            if (!TextUtils.isEmpty(customerAnswerJson)) {
                customerAnswers = new Gson().fromJson(customerAnswerJson, new TypeToken<List<String>>() {
                }.getType());
            }
        }
    }

    /**
     * 二维码相关
     */
    public static class QrCode {
        /**
         * 悬浮的二维码
         */
        public static List<QrCodeBean> sFloatQrCodes = new ArrayList<>();
        /**
         * 查缴指南的二维码
         */
        public static List<QrCodeBean> sPaymentQrCodes = new ArrayList<>();
    }

    /**
     * 产品缓存
     */
    public static class Product {
        public static List<GoodsTypeBean> sProductTypeLists = new ArrayList<>();
        public static SparseArray<ArrayList<GoodsDetailBean>> sProductDetails = new SparseArray<>();

        public static String getSpeakText(String str) {
            if (!sProductTypeLists.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                int index = 0;
                builder.append(str);
                for (GoodsTypeBean goodsTypeBean : sProductTypeLists) {
                    builder.append(goodsTypeBean.getName());
                    index++;
                    if (index == 3) {
                        break;
                    }
                    if (index < sProductTypeLists.size()) {
                        builder.append("、");
                    }
                }
                return builder.toString();
            }
            return null;
        }

        public static String getSpeakText() {
            if (!sProductTypeLists.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                int index = 0;
                builder.append("【").append(Constants.WELCOME_PRODUCT_MODULENAME).append("】:");
                for (GoodsTypeBean goodsTypeBean : sProductTypeLists) {
                    builder.append(goodsTypeBean.getName());
                    index++;
                    if (index == 3) {
                        break;
                    }
                    if (index < sProductTypeLists.size()) {
                        builder.append("、");
                    }
                }
                return builder.toString();
            }
            return null;
        }

        /**
         * 遍历商品
         *
         * @param productName 商品名
         * @return true: 跳转 false: 没有匹配到，处理后续操作
         */
        public boolean search(String productName) {
            if (TextUtils.isEmpty(productName)) {
                return false;
            }
            if (!sProductTypeLists.isEmpty() && sProductDetails.size() != 0) {
                for (GoodsTypeBean type : sProductTypeLists) {//先遍历商品种类
                    if (productName.contains(type.getName())) {
                        //跳转至商品列表界面
                        ArrayList<GoodsDetailBean> list = sProductDetails.get(type.getId());
                        if (list == null) {
                            list = new ArrayList<>();
                        }
                        ARouter.getInstance()
                                .build(BRouterPath.PRODUCT_CLOTHING_LIST)
                                .withParcelableArrayList(ProductListActivity.PRODUCT_DATA, list)
                                .navigation();
                        return true;
                    }
                }
                int size = sProductDetails.size();
                ArrayList<GoodsDetailBean> products;
                for (int i = 0; i < size; i++) {//种类没有则遍历所有商品名
                    products = sProductDetails.valueAt(i);
                    if (products == null || products.isEmpty()) {
                        continue;
                    }
                    for (GoodsDetailBean detailBean : products) {//遍历同一种类下的商品
                        if (productName.contains(detailBean.getName())) {
                            ARouter.getInstance()
                                    .build(BRouterPath.PRODUCT_CLOTHING_DETAIL)
                                    .withParcelable(ProductDetailsActivity.PRODUCT_DETAIL, detailBean)
                                    .navigation();
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        public static String getProductIntent(String text) {
            String empty = "";
            if (TextUtils.isEmpty(text)) {
                return empty;
            }
            if (!sProductTypeLists.isEmpty() && sProductDetails.size() != 0) {

                String intentRegEx = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"|\"我想要买\"].*";
                String intentRegEx2 = ".*我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                if (text.matches(intentRegEx)) {

                    boolean isCompleteSearch = false;

                    String regex = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"|\"我想要买\"]+[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                    String regex2 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"|\"我想要买\"]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                    String regex3 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"].*[\"个\"|\"我想要买\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]";
                    String regex4 = "[\"我想要\"|\"我要\"|\"我买\"|\"给我来\"|\"给我拿\"|\"我想买\"|\"我想要买\"]";

                    for (GoodsTypeBean type : sProductTypeLists) {//先遍历商品种类
                        if (text.contains(type.getName())) {
                            ArrayList<GoodsDetailBean> list = sProductDetails.get(type.getId());
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            isCompleteSearch = true;
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productCategory\",\"productData\":" + new Gson().toJson(list) + "}}";
                        }
                    }

                    int size = sProductDetails.size();
                    ArrayList<GoodsDetailBean> products;
                    for (int i = 0; i < size; i++) {//种类没有则遍历所有商品名
                        products = sProductDetails.valueAt(i);
                        for (GoodsDetailBean detailBean : products) {//遍历同一种类下的商品
                            if (text.matches((regex + detailBean.getName() + "+[。|.]"))) {
                                String gson = new Gson().toJson(detailBean);
                                int number = NumberUtils.getNumber(text);
                                CsjlogProxy.getInstance().info("number:" + number);
                                isCompleteSearch = true;
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":" + number + "}}";
                            } else if (text.matches((regex2 + detailBean.getName() + "+[。|.]"))) {
                                String gson = new Gson().toJson(detailBean);
                                isCompleteSearch = true;
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":0}}";
                            } else if (text.matches(regex3 + detailBean.getName() + "+[。|.]")) {
                                String gson = new Gson().toJson(detailBean);
                                int number = NumberUtils.numberParser(text);
                                CsjlogProxy.getInstance().info("number2:" + number);
                                isCompleteSearch = true;
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":" + number + "}}";
                            } else if (text.matches(regex4 + detailBean.getName() + "+[。|.]")) {
                                CsjlogProxy.getInstance().error(text.matches(regex4 + detailBean.getName() + "+[。|.]") + "");
                                CsjlogProxy.getInstance().error((regex4 + detailBean.getName() + "+[。|.]"));
                                String gson = new Gson().toJson(detailBean);
                                isCompleteSearch = true;
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":0}}";
                            } else {
                                if (text.contains(detailBean.getName())) {
                                    String gson = new Gson().toJson(detailBean);
                                    isCompleteSearch = true;
                                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":0}}";
                                }
                            }
                            if (text.contains(detailBean.getName())) {
                                String gson = new Gson().toJson(detailBean);
                                isCompleteSearch = true;
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":0}}";
                            }
                        }
                    }

                    if (!isCompleteSearch && (text.contains("我想买") || text.contains("我要买") || text.contains("我想要买"))) {
                        return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productNot\",\"productData\":\"null\",\"number\":0}}";
                    }

                } else if (text.matches(intentRegEx2)) {
                    String regex5 = "我要.*[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                    String regex6 = "我要[0-9]+[\"个\"|\"盒\"|\"包\"|\"根\"|\"块\"|\"箱\"|\"瓶\"|\"件\"]+[。|.]";
                    for (GoodsTypeBean type : sProductTypeLists) {//先遍历商品种类
                        if (text.contains(type.getName())) {
                            ArrayList<GoodsDetailBean> list = sProductDetails.get(type.getId());
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productCategory\",\"productData\":" + new Gson().toJson(list) + "}}";
                        }
                    }

                    int size = sProductDetails.size();
                    ArrayList<GoodsDetailBean> products;
                    for (int i = 0; i < size; i++) {//种类没有则遍历所有商品名
                        products = sProductDetails.valueAt(i);
                        for (GoodsDetailBean detailBean : products) {//遍历同一种类下的商品
                            if (text.matches(regex5)) {
                                String s = new StringBuilder(regex5).insert(0, detailBean.getName()).toString();
                                if (text.matches(s)) {
                                    String gson = new Gson().toJson(detailBean);
                                    int number = NumberUtils.numberParser(text);
                                    CsjlogProxy.getInstance().info("number2:" + number);
                                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":" + number + "}}";
                                }
                            } else if (text.matches(regex6)) {
                                String s = new StringBuilder(regex6).insert(0, detailBean.getName()).toString();
                                if (text.matches(s)) {
                                    String gson = new Gson().toJson(detailBean);
                                    int number = NumberUtils.getNumber(text);
                                    CsjlogProxy.getInstance().info("number:" + number);
                                    return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":" + number + "}}";
                                }
                            }
                        }
                    }
                } else {

                    for (GoodsTypeBean type : sProductTypeLists) {//先遍历商品种类
                        if (text.contains(type.getName())) {
                            ArrayList<GoodsDetailBean> list = sProductDetails.get(type.getId());
                            if (list == null) {
                                list = new ArrayList<>();
                            }
                            return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productCategory\",\"productData\":" + new Gson().toJson(list) + "}}";
                        }
                    }


                    int size = sProductDetails.size();
                    ArrayList<GoodsDetailBean> products;
                    for (int i = 0; i < size; i++) {//种类没有则遍历所有商品名
                        products = sProductDetails.valueAt(i);
                        for (GoodsDetailBean detailBean : products) {//遍历同一种类下的商品
                            if (text.contains(detailBean.getName())) {
                                String gson = new Gson().toJson(detailBean);
                                return "{\"text\":\"" + text + "\",\"intent\":\"" + Intents.PRODUCT_BUY + "\",\"result\":{\"action\":\"productDetail\",\"productData\":" + gson + ",\"number\":0}}";
                            }
                        }
                    }
                }


            }
            return empty;
        }


    }

    public static boolean isOpenChatView = true;

    /**
     * Nuance语音是否被激活
     */
    public static boolean isOpenNuance = false;
    /**
     * 是否跳转到其他App
     */
    public static boolean sIsIntoOtherApp = false;

    /**
     * 初始化场景颜色值
     *
     * @param scene 场景
     */
    public static void initSceneColor(String scene) {
        if (TextUtils.isEmpty(scene)) {
            return;
        }
        switch (scene) {
            case "shangsha":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#8b254e";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#8b254e";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "jiadian":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#1180ea";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#1180ea";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "yaodian":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#169e18";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#169e18";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "fuzhuang":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#D09B7F";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#393939";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#D09B7F";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "chaoshi":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#1180ea";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#1180ea";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "canting":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#30afaf";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#30afaf";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "qiche":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#3c3d3d";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#3c3d3d";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "shipin":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#677ce6";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#677ce6";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "jichang":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#0c0c0c";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#1e1d1d";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
//            case "jiudian":
//
//                break;
            case "yinhang":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#3341A6";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#3341A6";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "chezhan":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#2198db";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#2198db";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "jiaoyu":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#FFC448";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#ffffff";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#ffffff";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ff954c";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#ff974c";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "yiyuan":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#30afb0";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#30afb0";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "lvyou":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#24c290";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#266078";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "qiantai":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#30afb0";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#30afb0";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "yanjingdian":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#ed6a77";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#ed6a77";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "jiaju":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#eb6d90";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#eb6d90";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "kejiguan":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#2198db";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#2198db";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "xiedian":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#ffbf00";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#1a1a1a";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "shouji":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#3490df";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#3490df";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "dianli":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#007A77";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#007A77";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "cheguansuo":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#1f40a1";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#1f40a1";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "bowuguan":
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#9b0000";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#9c0000";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "jiudian": //酒店
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#302634";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#393939";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#302634";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "shebao"://社保场景
            case "gongshang"://工商场景
            case "shuiwu"://税务场景
            case "xingzheng"://行政场景
            case "gongan"://公安场景
            case "fayuan"://法院场景
            case "jianchayuan"://检察院场景
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#2E5EB9";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#2e5eb9";             //内容管理  Popup item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            case "zhanguan"://展馆
                SceneColor.sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
                SceneColor.sLanguagePopupItemSelectedBg = "#358EDE";              //语言选择popup  item选中背景
                SceneColor.sLanguagePopupItemTextUnSelectedColor = "#262626";     //语言选择popup  item未选中字体颜色
                SceneColor.sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

                SceneColor.sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
                SceneColor.sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
                SceneColor.sContentPopupItemUnSelectedBg = "#2198DA";             //内容管理  Popup  item未选中背景
                SceneColor.sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
                SceneColor.sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
                SceneColor.sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
                break;
            default:

                break;
        }
    }

    /**
     * 场景颜色
     * 内容管理、语言选择无法换肤，只能用这个方法
     */
    public static class SceneColor {
        /*
        语言选择部分
         */
        public static String sLanguagePopupItemUnSelectedBg = "#ffffff";            //语言选择popup  item未选中背景
        public static String sLanguagePopupItemSelectedBg = "#2198db";              //语言选择popup  item选中背景
        public static String sLanguagePopupItemTextUnSelectedColor = "#0c0c0c";     //语言选择popup  item未选中字体颜色
        public static String sLanguagePopupItemTextSelectedColor = "#ffffff";       //语言选择popup  item选中字体颜色

        /*
        内容管理部分
         */
        public static String sContentTitleTextUnSelectedColor = "#ffffff";          //内容管理  title字体未选中颜色
        public static String sContentTitleTextSelectedColor = "#ffffff";            //内容管理  title字体选中颜色
        public static String sContentPopupItemUnSelectedBg = "#2198db";             //内容管理  Popup  item未选中背景
        public static String sContentPopupItemSelectedBg = "#ffffff";               //内容管理  popup  item选中背景
        public static String sContentPopupItemTextUnSelectedColor = "#ffffff";      //内容管理  popup  item未选中字体颜色
        public static String sContentPopupItemTextSelectedColor = "#393939";        //内容管理  popup  item选中字体颜色
    }

    /******************************************主页模块场景ModuleId*************************************************/
    public static final int LEAD_WAY_ID = 2;//引领带路
    public static final int LOCAL_SERVICE_ID = 3;//周边服务
    public static final int VIP_CENTER_ID = 4;//会员注册
    public static final int ENTERTAINMENT_ID = 6;//互动娱乐
    public static final int PRODUCT_LIST_ID = 12;//商品列表
    public static final int EXHIBITION_EXPLANATION_ID = 13;//展览讲解(待定需修改)
    public static final int PAYMENT_GUIDE_ID = 14;//查缴指南
    public static final int MEDIA_PUBLICIZE_ID = 15;//媒体宣传
    public static final int HALL_TOUR_ID = 16;//大厅巡视
    public static final int LEARN_ENCYCLOPEDIA_ID = 99;//学习百科(暂时在android端写死，只有在教育场景下有效)
    public static final int MST_PLAYER_ID = 100;//课件播放(暂时在android端写死，只有在教育场景下有效(仅小雪机型))
    public static final int INVITED_VISITOR = 17;//受邀访客
    public static final int TEMP_VISITOR = 18;//临时访客

    public static List<SensitiveWordsBean> sensitiveWordsBeans;
    public static GreetContentBean greetContentBean;

    public static String WELCOME_PRODUCT_MODULENAME;

    /**
     * 创建场景欢迎语
     *
     * @param sceneKey   场景标识
     * @param greetWords 问候语
     * @return 欢迎语[0]:聊天框显示用 [1]:语音用
     */
    public static String[] createWelcomeText(String sceneKey, String greetWords) {
        if (TextUtils.isEmpty(greetWords)) {//问候语
            greetWords = "";
        }
        String robotName = "";//机器人名称

        if (BuildConfig.FLAVOR.contains("alice")) {
            robotName = BaseApplication.getAppContext().getString(R.string.robot_name_alice);
        } else if (BuildConfig.FLAVOR.contains("amy")) {
            robotName = BaseApplication.getAppContext().getString(R.string.robot_name_amy);
        } else if (BuildConfig.FLAVOR.contains("snow")) {
            robotName = BaseApplication.getAppContext().getString(R.string.robot_name_snow);
        }

        if (Constants.greetContentBean != null) {
            robotName = Constants.greetContentBean.getRobotName();
        }
        if (TextUtils.isEmpty(robotName)) {
            robotName = "";
        }
        String wakeupWords = "";//唤醒词
        if (BuildConfig.FLAVOR.contains("alice")) {
            wakeupWords = BaseApplication.getAppContext().getString(R.string.hello_alice);
        } else if (BuildConfig.FLAVOR.contains("amy")) {
            wakeupWords = BaseApplication.getAppContext().getString(R.string.hello_amy);
        } else if (BuildConfig.FLAVOR.contains("snow")) {
            wakeupWords = BaseApplication.getAppContext().getString(R.string.hello_snow);
        }

        if (isI18N() || CsjLanguage.isEnglish()) {
            if (TextUtils.isEmpty(greetWords)) {
                greetWords = BaseApplication.getAppContext().getString(R.string.default_i18n_greetWords);
            }
            return new String[]{greetWords, greetWords};
        } else {
            if (CsjLanguage.isEnglish()) {
                if (TextUtils.isEmpty(greetWords)) {
                    greetWords = BaseApplication.getAppContext().getString(R.string.default_i18n_greetWords);
                }
                return new String[]{greetWords, greetWords};
            }

            switch (sceneKey) {
                case "fuzhuang": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.fuzhuang_say1))
                            .append("\n" + BaseApplication.getAppContext().getString(R.string.fuzhuang_say2));
                    speakTempStr.append(BaseApplication.getAppContext().getString(R.string.fuzhuang_say3));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append(productText);
                        speakTempStr.append(productText);
                    } else {
                        showTempStr.append(BaseApplication.getAppContext().getString(R.string.fuzhuang_say4));
                        speakTempStr.append(BaseApplication.getAppContext().getString(R.string.fuzhuang_say4));
                    }
                    showTempStr.append(BaseApplication.getAppContext().getString(R.string.fuzhuang_say5)).append(wakeupWords);
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "shangsha": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }

                    String productText = Constants.Product.getSpeakText();

                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供大厅导航、品牌荟萃的介绍，");
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供大厅导航、品牌荟萃的介绍");
                    if(!TextUtils.isEmpty(naviName) || !TextUtils.isEmpty(productText)){
                        showTempStr.append("\n您可以对我说：");
                        if (!TextUtils.isEmpty(naviName)) {
                            showTempStr.append("\n【带\t\t\t路】：带我去{").append(naviName).append("}");
                        }
                        if (!TextUtils.isEmpty(productText)) {
                            showTempStr.append("\n").append(productText);
                        }
                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "bowuguan": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供智能导航、参观指南等的介绍。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append("【").append(sContentInfos.get(0).getName()).append("】");
                    }

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供智能导航、参观指南等的介绍。");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "jiadian": {
                    String productText = Constants.Product.getSpeakText();

                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供优惠特享、家居产品的介绍。");
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供优惠特享、家居产品的介绍。");
                    if(!TextUtils.isEmpty(productText)){
                        showTempStr.append("\n您可以对我说：");
                        showTempStr.append("\n").append(productText);
                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "xiedian": {
                    String productText = Constants.Product.getSpeakText();

                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供折扣优惠、本店鞋款的介绍。");
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供折扣优惠、本店鞋款的介绍。");
                    if(!TextUtils.isEmpty(productText)){
                        showTempStr.append("\n您可以对我说：");
                        showTempStr.append("\n").append(productText);
                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "cheguansuo": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供服务导航、信息咨询等服务。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为你提供展馆导航、信息咨询等服务。");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "shipin": { //食品
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.shipin_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.shipin_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.shipin_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }

                case "chaoshi": { //超市
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.jiaju_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.jiaju_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.shipin_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }

                case "lvyou": { //旅游
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }

                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName);
                    String welcomeStr = "";
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        welcomeStr = String.format(Locale.getDefault(),BaseApplication.getAppContext()
                                .getString(R.string.lvyou_say1),sContentInfos.get(0).getName());
                    } else {
                        welcomeStr = String.format(Locale.getDefault(),BaseApplication.getAppContext()
                                .getString(R.string.lvyou_say1),"周边服务");
                    }
                    showTempStr.append(welcomeStr);
                    speakTempStr.append(robotName).append(welcomeStr);

                    showTempStr.append("您可以对我说：");
                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append("【").append(sContentInfos.get(0).getName()).append("】");
                    } else {
                        showTempStr.append("\n问\t\t\t\t\t题：").append("【周边服务】");
                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "qiche": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.qiche_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.qiche_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.qiche_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "shouji":
                case "jiaju": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.jiaju_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.jiaju_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.qiche_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "canting": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.canting_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.canting_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.qiche_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }

                case "yaodian": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.yaodian_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.yaodian_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.qiche_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }

                case "yanjingdian": {
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);
                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.yanjingdian_say1));
                    speakTempStr.append(robotName).append(BaseApplication.getAppContext().getString(R.string.yanjingdian_say1));
                    String productText = Constants.Product.getSpeakText();
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n" + BaseApplication.getAppContext().getString(R.string.qiche_say2));
                        showTempStr.append(productText);
                    } else {

                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "kejiguan": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供展馆导航、信息咨询等服务。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为你提供展馆导航、信息咨询等服务。");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "yiyuan": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供预诊分诊、专家介绍等服务。\n");
                    speakTempStr.append(robotName).append("正在为您服务，我可以为您提供预诊分诊、专家介绍等服务。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }

                    showTempStr.append("\n期待与您互动，希望您对我的服务满意！");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "shebao": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();

                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append(BaseApplication.getAppContext().getString(R.string.shebao_say1));
                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    showTempStr.append("\n问\t\t\t\t\t题：【单位职工生育津贴报销流程】【城乡居民医疗保险缴费方式】");
                    showTempStr.append("\n如果需要问我其他问题，请对我说：").append(wakeupWords);
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供信息咨询、服务导航等服务。");
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "dianli": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    showTempStr.append(greetWords)
                            .append(robotName)
                            .append("正在为您服务，我可以为您提供信息咨询、服务导航等服务。\n您可以对我说：");
                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    showTempStr.append("\n问\t\t\t\t\t题：【用什么可以交电费？】");
                    String speakTempStr = greetWords + robotName + "正在为您服务，我可以为您提供信息咨询、服务导航等服务。";
                    return new String[]{showTempStr.toString(), speakTempStr};
                }
                case "yinhang": {
                    StringBuilder showTempStr = new StringBuilder(greetWords);
                    StringBuilder speakTempStr = new StringBuilder(greetWords);
                    String productText = Constants.Product.getSpeakText();
                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供业务咨询、产品介绍等服务。\n")
                            .append("您可以对我说：\n业务咨询：【银行卡如何挂失】");
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供业务咨询、产品介绍等服务。\n");
                    if (!TextUtils.isEmpty(productText)) {
                        showTempStr.append("\n产品介绍：").append(productText);
                    }
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "jichang": {
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();

                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供大厅导航、机场介绍等服务。\n" +
                                    "您可以对我说：");
                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    } else {
                        showTempStr.append("\n问\t\t\t\t\t题：周边服务");
                    }

                    showTempStr.append("\n期待与您互动，希望您对我的服务满意！");
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供大厅导航、机场介绍等服务。");
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "chezhan": { //车站
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();

                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供贵宾导航、信息咨询等服务。\n");
                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }
                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }
                    String wakeup = "";
                    switch (BuildConfig.robotType) {
                        case "snow":
                            wakeup = "\"你好，小雪\"";
                            break;
                        case "alice":
                        case "alice_plus":
                            wakeup = "\"你好，爱丽丝\"";
                            break;
                        case "amy_plus":
                            wakeup = "\"你好，艾米\"";
                            break;
                    }
                    showTempStr.append("\n如果需要问我其他问题，请对我说").append(wakeup);
                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为你提供贵宾导航、信息咨询等服务。");
                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "jiudian": {
                    //酒店
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供贵宾导航、信息咨询等服务。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }

                    showTempStr.append("\n期待与您互动，希望您对我的服务满意！");

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为你提供贵宾导航、信息咨询等服务。");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};

                }
                case "qiantai" : {//前台
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供访客登记、信息咨询等服务。\n");

                    showTempStr.append("您可以对我说：");
                    showTempStr.append("\n1、【受邀访客】");
                    showTempStr.append("\n2、【临时访客】");

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供访客登记、信息咨询等服务。\n");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "zhanguan": {//展馆
                    String naviName = "";
                    String json = SharedPreUtil.getString(SharedKey.NAVI_NAME, SharedKey.NAVI_KEY);
                    if (!TextUtils.isEmpty(json)) {
                        List<NaviBean> naviBeans = GsonUtils.jsonToObject(json,
                                new TypeToken<List<NaviBean>>() {
                                }.getType());
                        if (naviBeans != null && !naviBeans.isEmpty()) {
                            naviName = naviBeans.get(0).getName();
                        }
                    }
                    StringBuilder showTempStr = new StringBuilder();
                    StringBuilder speakTempStr = new StringBuilder();
                    showTempStr.append(greetWords);
                    speakTempStr.append(greetWords);

                    showTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供展馆导航、信息咨询等服务。\n");

                    if (!TextUtils.isEmpty(naviName) || (sContentInfos != null && sContentInfos.size() > 0)) {
                        showTempStr.append("您可以对我说：");
                    }

                    if (!TextUtils.isEmpty(naviName)) {
                        showTempStr.append("\n带\t\t\t\t\t路：带我去{").append(naviName).append("}");
                    }
                    if (sContentInfos != null && sContentInfos.size() > 0) {
                        showTempStr.append("\n问\t\t\t\t\t题：").append(sContentInfos.get(0).getName());
                    }

                    speakTempStr.append(robotName)
                            .append("正在为您服务，我可以为您提供展馆导航、信息咨询等服务。");

                    return new String[]{showTempStr.toString(), speakTempStr.toString()};
                }
                case "gongshang"://工商场景
                case "shuiwu"://税务场景
                case "xingzheng"://行政场景
                case "gongan"://公安场景
                case "fayuan"://法院场景
                case "jianchayuan"://检察院场景
                case "CSJ_BOT"://默认场景
                default: {
                    String showTempStr = greetWords + robotName + BaseApplication.getAppContext().getString(R.string.common_welcome);
                    String speakTempStr = greetWords + robotName + BaseApplication.getAppContext().getString(R.string.common_welcome);
                    return new String[]{showTempStr, speakTempStr};
                }
            }
        }
    }

    /**
     * 是否进入了人工客服
     */
    public static boolean isNeedComment = false;
    public static boolean sIsCustomerService = false;
    public static final int CONNECTING = 1002;    //连接中
    public static final int ISCONNECTED = 1001;   //接通
    public static final int DISCONNCTED = 1003;   //断开连接
    public static final int CONNECT_FAILED = 1004;  //连接失败
    public static final int SERVER_DISCONNCTED = 1005; //服务端主动断开
    public static final int CONNECT_INTERRUPT = 1006; //通信中断
    public static final int ALREADY_CONNECTED = 1007; //已连接


    /**
     * 不可到达点集合
     */
    public static List<UnreachablePointBean> sUnreachablePoints = new ArrayList<>();
    /**
     * 内容管理
     */
    public static List<ContentInfoBean> sContentInfos = new ArrayList<>();

    /**
     * 清除静态数据
     */
    public static void clear() {
        UnknownProblemAnswer.defaultAnswers.clear();
        UnknownProblemAnswer.customerAnswers.clear();
        QrCode.sPaymentQrCodes.clear();
        QrCode.sFloatQrCodes.clear();
        Product.sProductTypeLists.clear();
        Product.sProductDetails.clear();
        if (sensitiveWordsBeans != null) {
            sensitiveWordsBeans.clear();
        }
        greetContentBean = null;
        sUnreachablePoints.clear();
        sContentInfos.clear();
    }

    /**
     * 清除静态数据
     */
    public static void clear2() {
        UnknownProblemAnswer.defaultAnswers.clear();
        UnknownProblemAnswer.customerAnswers.clear();
        QrCode.sPaymentQrCodes.clear();
        QrCode.sFloatQrCodes.clear();
        Product.sProductTypeLists.clear();
        Product.sProductDetails.clear();
        sUnreachablePoints.clear();
    }

    /**
     * 判断是否是大屏机器人
     */
    public static boolean isPlus() {
        return TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS_i18n)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS_i18n);
    }

    /**
     * 判断是否是国外的机器人
     */
    public static boolean isI18N() {
        return TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_SNOW_i18n)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_PLUS_i18n)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_ALICE_i18n)
                || TextUtils.equals(BuildConfig.FLAVOR, BuildConfig.ROBOT_TYPE_DEF_AMY_PLUS_i18n);
    }
}
