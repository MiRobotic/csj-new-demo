package com.csjbot.mobileshop.router;

import com.csjbot.mobileshop.feature.course.MSTPlayserActivity;
import com.csjbot.mobileshop.feature.media.MediaPublicizeActivity;
import com.csjbot.mobileshop.feature.payment.PaymentGuideActivity;
import com.csjbot.mobileshop.feature.product.ProductDetailsActivity;
import com.csjbot.mobileshop.feature.product.ProductListActivity;
import com.csjbot.mobileshop.feature.product.ProductTypeActivity;

/**
 * Created by xiasuhuei321 on 2017/12/6.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 * <p>
 * desc:保存了所有 Activity 对应的路径，所有路径至少两级
 * 如：/feature/product
 */

public class BRouterPath {
    // 这个分级表示 功能
    private static final String NEW_RETAIL = "/new_retail";
    // 这个分级表示 信息展示
    private static final String INFO = "/info";

    // 产品
    private static final String PRODUCT = "/product";
    // 导航
    private static final String NAVI = "/navi";
    // 巡视
    private static final String PATROL = "/patrol";
    // 娱乐
    private static final String PLAY = "/play";
    // 人脸
    private static final String FACE = "/face";
    // 周边
    private static final String NEARBY = "/nearby";
    // 设置
    private static final String SETTING = "/setting";
    // 课程介绍
    private static final String COURSE = "/course";
    //访客
    private static final String VISIT = "/visit";

    // 首页
    private static final String MAINPAGE = "/mainpage";

    //内容种类（webview[酒店]）
    private static final String JIUDIAN = "/hotel_type";
    /**
     * 所有场景的首页
     */
    public static final String ALL_SCENE_HOME_PATH = NEW_RETAIL + MAINPAGE + "/AllSceneHomeActivity";

    /**
     * 衣服种类
     *
     * @see ProductTypeActivity
     */
    public static final String PRODUCT_CLOTHING_TYPE = NEW_RETAIL + PRODUCT + "/ClothingType";
    /**
     * 衣服列表
     *
     * @see ProductListActivity
     */
    public static final String PRODUCT_CLOTHING_LIST = NEW_RETAIL + PRODUCT + "/ClothingList";
    /**
     * 衣服详情
     *
     * @see ProductDetailsActivity
     */
    public static final String PRODUCT_CLOTHING_DETAIL = NEW_RETAIL + PRODUCT + "/ClothingDetail";
    /**
     * 优惠券
     *
     * @see com.csjbot.mobileshop.feature.coupon.CouponActivity
     */
    public static final String COUPON = NEW_RETAIL + INFO + "/Coupon";

    /**
     * 娱乐 - 跳舞
     *
     * @see com.csjbot.mobileshop.feature.dance.DanceActivity
     */
    public static final String DANCE = NEW_RETAIL + PLAY + "/DANCE";
    /**
     * 娱乐 - 首页
     *
     * @see com.csjbot.mobileshop.feature.entertainment.EntertainmentActivity
     */
    public static final String ENTERTAINMENT = NEW_RETAIL + PLAY + "/Entertainment";
    /**
     * 教育娱乐 - 首页
     *
     * @see com.csjbot.mobileshop.feature.entertainment.EntertainmentActivity
     */
    public static final String JIAO_YU_ENTERTAINMENT = NEW_RETAIL + PLAY + "/JiaoYuEntertainment";
    /**
     * 娱乐 - 播放音乐
     *
     * @see com.csjbot.mobileshop.feature.music.MusicActivity
     */
    public static final String MUSIC = NEW_RETAIL + PLAY + "/Music";
    /**
     * 娱乐 - 讲故事
     *
     * @see com.csjbot.mobileshop.feature.story.StoryActivity
     */
    public static final String STORY = NEW_RETAIL + PLAY + "/Story";

    /**
     * 会员中心
     *
     * @see com.csjbot.mobileshop.feature.vipcenter.VipCenterActivity
     */
    public static final String VIP_CENTER = NEW_RETAIL + FACE + "/VipCenter";

    /**
     * 受邀访客
     *
     * @see com.csjbot.mobileshop.feature.visit.invite.InviteVisitorActivity
     */
    public static final String INVITE_VISIT = NEW_RETAIL + VISIT + "/invite";

    /**
     * 临时访客
     *
     * @see com.csjbot.mobileshop.feature.visit.temp.TempVisitorActivity
     */
    public static final String TEMP_VISIT = NEW_RETAIL + VISIT + "/temp";

    /**
     * 课件播放
     *
     * @see MSTPlayserActivity
     */
    public static final String MST_PLAYER = NEW_RETAIL + COURSE + "/COURSE";


    /**
     * @see com.csjbot.mobileshop.TestActivity
     * 测试Activity
     */
    private static final String TEST = "/test";
    public static final String UPBODY_TEST = TEST + "/UpBodyTest";
    public static final String BAIDU_WIFI_SET = TEST + "/BaiduWifiSet";

    /**
     * 导航首页
     *
     * @see com.csjbot.mobileshop.feature.navigation.NaviActivity
     */
    public static final String NAVI_MAIN = NEW_RETAIL + NAVI + "/Main";
    /**
     * 导航首页新
     *
     * @see com.csjbot.mobileshop.feature.navigation.NaviActivityNew
     */
    public static final String NAVI_MAIN_NEW = NEW_RETAIL + NAVI + "/Main_new";
    /**
     * 导航设置
     *
     * @see com.csjbot.mobileshop.feature.navigation.setting.NaviSettingActivity
     */
    public static final String NAVI_SETTING = NEW_RETAIL + NAVI + "/Setting";

    /**
     * 周边首页
     *
     * @see com.csjbot.mobileshop.feature.nearbyservice.NearByActivity
     */
    public static final String NEAR_BY_MAIN = NEW_RETAIL + NEARBY + "/Main";

    /**
     * 机场周边首页
     *
     * @see com.csjbot.mobileshop.feature.nearbyservice.JichangNearByActivity
     */
    public static final String JICHANG_NEAR_BY_MAIN = NEW_RETAIL + NEARBY + "/JichangMain";

    /**
     * 周边搜索结果展示页
     *
     * @see com.csjbot.mobileshop.feature.nearbyservice.PoiSearchActivity
     */
    public static final String NEAR_BY_SEARCH = NEW_RETAIL + NEARBY + "/Search";

    /**
     * 咨询
     *
     * @see com.csjbot.mobileshop.feature.search.SearchActivity
     */
    public static final String INFO_SEARCH = NEW_RETAIL + INFO + "/search";

    /**
     * 进入设置界面，需要输入密码
     *
     * @see com.csjbot.mobileshop.feature.settings.SettingsActivity
     */
    public static final String SETTING_CONFIRM = NEW_RETAIL + SETTING + "/setting_input_pwd";

    /**
     * 巡视设置
     *
     * @see com.csjbot.mobileshop.guide_patrol.patrol.patrol_setting.PatrolSettingActivity
     */
    public static final String PATROL_SETTING = NEW_RETAIL + PATROL + "/setting";

    /**
     * 巡视密码
     *
     * @see com.csjbot.mobileshop.guide_patrol.patrol.PatrolPWDActivity
     */
    public static final String PATROL_PWD = NEW_RETAIL + PATROL + "/pwd";

    /**
     * 导览评价
     *
     * @see com.csjbot.mobileshop.feature.navigation.NaviGuideCommentActivity
     */
    public static final String NAVI_GUIDE_COMMENT = NEW_RETAIL + NAVI + "/comment";

    /**
     * 语言选择页
     *
     * @see com.csjbot.mobileshop.MainActivity
     */
    public static final String MAIN = NEW_RETAIL + "/main";

    /**
     * 三级
     *
     * @see com.csjbot.mobileshop.feature.content.fragment.ContentLevelThreeFragment
     */
    public static final String THREE_LEVEL = NEW_RETAIL + JIUDIAN + "/3";

    /**
     * 二级
     *
     * @see com.csjbot.mobileshop.feature.content.fragment.ContentLevelTwoFragment
     */
    public static final String TWO_LEVEL = NEW_RETAIL + JIUDIAN + "/2";

    /**
     * 一级
     *
     * @see com.csjbot.mobileshop.feature.content.fragment.ContentLevelOneFragment
     */
    public static final String ONE_LEVEL = NEW_RETAIL + JIUDIAN + "/1";

    public static final String CONTENT_ACTIVITY = NEW_RETAIL + "/ContentActivity";
    /**
     * 媒体宣传
     *
     * @see MediaPublicizeActivity
     */
    public static final String MEDIA_PUBLICIZE = NEW_RETAIL + "/MediaPublicty";
    /**
     * 查缴指南
     *
     * @see PaymentGuideActivity
     */
    public static final String PAYMENT_GUIDE = NEW_RETAIL + "/PaymentGuide";
    /**
     * 学习百科
     *
     * @see com.csjbot.mobileshop.feature.Learning.LearningEducationActivity
     */
    public static final String LEARN_EDUCATION = NEW_RETAIL + "/LearnEducation";
}