package com.csjbot.mobileshop.model.http.apiservice;

import com.csjbot.mobileshop.model.http.bean.rsp.AdvertisementInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.AllGoodsTypeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ContentInfoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsDetailBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GoodsTypeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.GreetContentBean;
import com.csjbot.mobileshop.model.http.bean.rsp.LogoBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ModuleBean;
import com.csjbot.mobileshop.model.http.bean.rsp.NaviResourceBean;
import com.csjbot.mobileshop.model.http.bean.rsp.OSSSignatureBean;
import com.csjbot.mobileshop.model.http.bean.rsp.P2PBean;
import com.csjbot.mobileshop.model.http.bean.rsp.QrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ResponseBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SceneBean;
import com.csjbot.mobileshop.model.http.bean.rsp.SensitiveWordsBean;
import com.csjbot.mobileshop.model.http.bean.rsp.ShoppingQrCodeBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UnreachablePointBean;
import com.csjbot.mobileshop.model.http.bean.rsp.UpdateAPKBean;
import com.csjbot.mobileshop.model.http.bean.rsp.VipGreetBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by jingwc on 2018/3/29.
 */

public interface IApi {
    void saveVip(String body, Observer<ResponseBean<String>> observer);

    void getLogo(String sn, Observer<ResponseBean<LogoBean>> observer);

    void getScene(String sn, Observer<ResponseBean<SceneBean>> observer);

    void downloadSceneResource(String url, Observer<ResponseBody> observer);

    void getOSSSignature(Observer<ResponseBean<OSSSignatureBean>> observer);

    void getAwsOSSSignature(Observer<ResponseBody> observer);

    void uploadOSS(String url, Map<String, RequestBody> map, MultipartBody.Part part, Observer<ResponseBody> observer);

    void uploadAwsOSS(String url, Map<String, RequestBody> map, MultipartBody.Part part, Observer<Response<Void>> observer);

    void getVipGreet(String sn, String faceId, Observer<ResponseBean<VipGreetBean>> observer);

    void uploadVisitor(String body, Observer<ResponseBean<String>> observer);

    void getModule(String sn, Observer<ResponseBean<List<ModuleBean>>> observer);

    void getSensitiveWords(String sn, Observer<ResponseBean<List<SensitiveWordsBean>>> observer);

    void getGreetContent(String sn, Observer<ResponseBean<GreetContentBean>> observer);


    void getNaviResourceList(String sn, Observer<ResponseBean<List<NaviResourceBean>>> observer);

    void getContentInfo(String sn, Observer<ResponseBean<List<ContentInfoBean>>> observer);

    void getSales(String sn, Observer<ResponseBody> observer);

    void getChatAndSegmentInfo(String sn, Observer<ResponseBody> observer);

    void updateChatAndSegment(String body, Observer<ResponseBody> observer);

    void getAnswer(String body, Observer<ResponseBody> observer);


    void getAnswerV3(String sn, String body, Observer<ResponseBody> observer);

    void getInternationalStatus(String sn, Observer<ResponseBody> observer);

    void getGoodsType(String sn, String id, Observer<ResponseBean<List<GoodsTypeBean>>> observer);

    void getAllGoodsInfo(String sn, Observer<ResponseBean<List<AllGoodsTypeBean>>> observer);

    void getAdvertisementInfo(String sn, Observer<ResponseBean<AdvertisementInfoBean>> observer);

    void getGoodsDetail(String sn, String body, Observer<ResponseBean<List<GoodsDetailBean>>> observer);

    void getShoppingQrCode(String sn, Observer<ResponseBean<ShoppingQrCodeBean>> observer);

    void postOrder(String body, Observer<ResponseBean> observer);

    void getAppVersion(String sn, String category, Observer<ResponseBean<UpdateAPKBean>> observer);

    void downloadFile(String url, Observer<ResponseBody> observer);

    void getQrCode(String sn, Observer<ResponseBean<List<QrCodeBean>>> observer);

    void getP2PCount(String sn, Observer<P2PBean> observer);

    void getUnreachablePoint(String sn, Observer<ResponseBean<List<UnreachablePointBean>>> observer);

    void uploadLocation(String sn, String location, Observer<ResponseBean> observer);

    void getVisitInfo(String sn, String invitationCode, Observer<ResponseBody> observer);

    void findVisitByFaceid(String sn, String faceid, Observer<ResponseBody> observer);

    void addVisitInfo(String sn, String location, Observer<ResponseBean> observer);
}
