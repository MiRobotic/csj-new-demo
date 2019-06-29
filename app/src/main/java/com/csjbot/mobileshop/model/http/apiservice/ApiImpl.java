package com.csjbot.mobileshop.model.http.apiservice;


import com.csjbot.mobileshop.global.CsjLanguage;
import com.csjbot.mobileshop.model.http.base.BaseImpl;
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

public class ApiImpl extends BaseImpl implements IApi {

    @Override
    public void saveVip(String body, Observer<ResponseBean<String>> observer) {
        scheduler(getRetrofit().saveVip(getBody(body))).subscribe(observer);
    }

    @Override
    public void getLogo(String sn, Observer<ResponseBean<LogoBean>> observer) {
        scheduler(getRetrofit().getLogo(sn)).subscribe(observer);
    }

    @Override
    public void getScene(String sn, Observer<ResponseBean<SceneBean>> observer) {
        scheduler(getRetrofit().getScene(sn)).subscribe(observer);
    }

    @Override
    public void downloadSceneResource(String url, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().downloadSceneResource(url)).subscribe(observer);
    }

    @Override
    public void getOSSSignature(Observer<ResponseBean<OSSSignatureBean>> observer) {
        scheduler(getRetrofit().getOSSSignature()).subscribe(observer);
    }

    @Override
    public void getAwsOSSSignature(Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getAwsOSSSignature()).subscribe(observer);
    }

    @Override
    public void uploadOSS(String url, Map<String, RequestBody> map, MultipartBody.Part part, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().uploadOSS(url, map, part)).subscribe(observer);
    }

    @Override
    public void uploadAwsOSS(String url, Map<String, RequestBody> map, MultipartBody.Part part, Observer<Response<Void>> observer) {
        scheduler(getRetrofit().uploadAwsOSS(url, map, part)).subscribe(observer);
    }

    @Override
    public void getVipGreet(String sn, String faceId, Observer<ResponseBean<VipGreetBean>> observer) {
        scheduler(getRetrofit().getVipGreet(sn, faceId)).subscribe(observer);
    }

    @Override
    public void uploadVisitor(String body, Observer<ResponseBean<String>> observer) {
        scheduler(getRetrofit().uploadVisitor(getBody(body))).subscribe(observer);
    }

    @Override
    public void getModule(String sn, Observer<ResponseBean<List<ModuleBean>>> observer) {
        scheduler(getRetrofit().getModule(sn)).subscribe(observer);
    }

    @Override
    public void getSensitiveWords(String sn, Observer<ResponseBean<List<SensitiveWordsBean>>> observer) {
        scheduler(getRetrofit().getSensitiveWords(sn)).subscribe(observer);
    }

    @Override
    public void getGreetContent(String sn, Observer<ResponseBean<GreetContentBean>> observer) {
        scheduler(getRetrofit().getGreetContent(sn)).subscribe(observer);
    }

    @Override
    public void getNaviResourceList(String sn, Observer<ResponseBean<List<NaviResourceBean>>> observer) {
        scheduler(getRetrofit().getNaviResourceList(sn)).subscribe(observer);
    }

    @Override
    public void getContentInfo(String sn, Observer<ResponseBean<List<ContentInfoBean>>> observer) {
        scheduler(getRetrofit().getContentInfo(sn)).subscribe(observer);
    }

    @Override
    public void getSales(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getSales(sn)).subscribe(observer);
    }

    @Override
    public void getChatAndSegmentInfo(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getChatAndSegmentInfo(sn)).subscribe(observer);
    }

    @Override
    public void updateChatAndSegment(String body, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().updateChatAndSegment(getBody(body))).subscribe(observer);
    }

    @Override
    public void getAnswer(String body, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getAnswer(getBody(body))).subscribe(observer);
    }

    @Override
    public void getAnswerV3(String sn, String body, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getAnswerV3(sn, getBody(body))).subscribe(observer);
    }

    @Override
    public void getInternationalStatus(String sn, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getInternationalStatus(sn)).subscribe(observer);
    }

    @Override
    public void getGoodsType(String sn, String id, Observer<ResponseBean<List<GoodsTypeBean>>> observer) {
        scheduler(getRetrofit().getGoodsType(sn, id, CsjLanguage.getISOLanguage(CsjLanguage.CURRENT_LANGUAGE))).subscribe(observer);
    }

    @Override
    public void getAllGoodsInfo(String sn, Observer<ResponseBean<List<AllGoodsTypeBean>>> observer) {
        scheduler(getRetrofit().getAllGoodsInfo(sn, CsjLanguage.getISOLanguage(CsjLanguage.CURRENT_LANGUAGE))).subscribe(observer);
    }

    @Override
    public void getAdvertisementInfo(String sn, Observer<ResponseBean<AdvertisementInfoBean>> observer) {
        scheduler(getRetrofit().getAdvertisementInfo(sn, CsjLanguage.getISOLanguage(CsjLanguage.CURRENT_LANGUAGE))).subscribe(observer);
    }

    @Override
    public void getGoodsDetail(String sn, String body, Observer<ResponseBean<List<GoodsDetailBean>>> observer) {
        scheduler(getRetrofit().getGoodsDetail(sn, CsjLanguage.getISOLanguage(CsjLanguage.CURRENT_LANGUAGE), getBody(body))).subscribe(observer);
    }

    @Override
    public void getShoppingQrCode(String sn, Observer<ResponseBean<ShoppingQrCodeBean>> observer) {
        scheduler(getRetrofit().getShoppingQrCode(sn, CsjLanguage.getISOLanguage(CsjLanguage.CURRENT_LANGUAGE))).subscribe(observer);
    }

    @Override
    public void postOrder(String body, Observer<ResponseBean> observer) {
        scheduler(getRetrofit().postOrder(getBody(body))).subscribe(observer);
    }

    @Override
    public void getAppVersion(String sn, String category, Observer<ResponseBean<UpdateAPKBean>> observer) {
        scheduler(getRetrofit().getAppVersion(sn, 1, category, CsjLanguage.getISOLanguage())).subscribe(observer);
    }

    @Override
    public void downloadFile(String url, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().downloadFile(url)).subscribe(observer);
    }

    @Override
    public void getQrCode(String sn, Observer<ResponseBean<List<QrCodeBean>>> observer) {
        scheduler(getRetrofit().getQrCode(sn, CsjLanguage.getISOLanguage())).subscribe(observer);
    }

    @Override
    public void getP2PCount(String sn, Observer<P2PBean> observer) {
        scheduler(getRetrofit().getP2PCount(sn, CsjLanguage.getISOLanguage())).subscribe(observer);
    }

    @Override
    public void getUnreachablePoint(String sn, Observer<ResponseBean<List<UnreachablePointBean>>> observer) {
        scheduler(getRetrofit().getUnreachablePoint(sn, CsjLanguage.getISOLanguage())).subscribe(observer);
    }

    @Override
    public void uploadLocation(String sn, String location, Observer<ResponseBean> observer) {
        scheduler(getRetrofit().uploadLocation(sn, CsjLanguage.getISOLanguage(), getBody(location))).subscribe(observer);
    }

    @Override
    public void getVisitInfo(String sn, String invitationCode, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().getVisitInfo(sn, CsjLanguage.getISOLanguage(), invitationCode)).subscribe(observer);
    }

    @Override
    public void findVisitByFaceid(String sn, String faceid, Observer<ResponseBody> observer) {
        scheduler(getRetrofit().findVisitByFaceid(sn, CsjLanguage.getISOLanguage(), faceid)).subscribe(observer);
    }

    @Override
    public void addVisitInfo(String sn, String location, Observer<ResponseBean> observer) {
        scheduler(getRetrofit().addVisitInfo(sn, CsjLanguage.getISOLanguage(), getBody(location))).subscribe(observer);
    }

    @Override
    public ApiService getRetrofit() {
        return getRetrofit(ApiService.class);
    }
}
