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

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by jingwc on 2018/3/29.
 */

public interface ApiService {

    @POST("abcdefg")
    Observable<ResponseBean<String>> saveVip(@Body RequestBody body);

    @GET("abcdefg")
    Observable<ResponseBean<LogoBean>> getLogo(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBean<SceneBean>> getScene(@Query("sn") String sn);

    @GET
    Observable<ResponseBody> downloadSceneResource(@Url String url);

    @GET("abcdefg")
    Observable<ResponseBean<OSSSignatureBean>> getOSSSignature();

    @GET("abcdefg")
    Observable<ResponseBody> getAwsOSSSignature();

    @Multipart
    @POST
    Observable<ResponseBody> uploadOSS(@Url String url, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);

    @Multipart
    @POST
    Observable<Response<Void>> uploadAwsOSS(@Url String url, @PartMap Map<String, RequestBody> map, @Part MultipartBody.Part part);

    @GET("abcdefg")
    Observable<ResponseBean<VipGreetBean>> getVipGreet(@Query("sn") String sn, @Query("faceId") String faceId);

    @POST("abcdefg")
    Observable<ResponseBean<String>> uploadVisitor(@Body RequestBody body);

    @GET("abcdefg")
    Observable<ResponseBean<List<ModuleBean>>> getModule(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBean<List<ContentInfoBean>>> getContentInfo(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBean<List<NaviResourceBean>>> getNaviResourceList(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBean<List<SensitiveWordsBean>>> getSensitiveWords(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBean<GreetContentBean>> getGreetContent(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBody> getSales(@Query("sn") String sn);

    @GET("abcdefg")
    Observable<ResponseBody> getChatAndSegmentInfo(@Query("sn") String sn);

    @POST("abcdefg")
    Observable<ResponseBody> updateChatAndSegment(@Body RequestBody body);


    @POST("abcdefg")
    Observable<ResponseBody> getAnswer(@Body RequestBody body);


    @GET("abcdefg")
    Observable<ResponseBody> getInternationalStatus(@Query("sn") String sn);


    @GET("abcdefg")
    Observable<ResponseBean<List<GoodsTypeBean>>> getGoodsType(@Query("sn") String sn, @Query("id") String id, @Query("language") String language);


    @GET("abcdefg")
    Observable<ResponseBean<List<AllGoodsTypeBean>>> getAllGoodsInfo(@Query("sn") String sn, @Query("language") String language);


    @GET("abcdefg")
    Observable<ResponseBean<AdvertisementInfoBean>> getAdvertisementInfo(@Query("sn") String sn, @Query("language") String language);


    @POST("abcdefg")
    Observable<ResponseBean<List<GoodsDetailBean>>> getGoodsDetail(@Query("sn") String sn, @Query("language") String language, @Body RequestBody body);


    @GET("abcdefg")
    Observable<ResponseBean<ShoppingQrCodeBean>> getShoppingQrCode(@Query("sn") String sn, @Query("language") String language);


    @POST("abcdefg")
    Observable<ResponseBean> postOrder(@Body RequestBody body);

    @GET("abcdefg")
    Observable<ResponseBean<UpdateAPKBean>> getAppVersion(@Query("sn") String sn, @Query("channel") int channel, @Query("category") String category, @Query("language") String language);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);


    @GET("abcdefg")
    Observable<ResponseBean<List<QrCodeBean>>> getQrCode(@Query("sn") String sn, @Query("language") String language);


    @GET("abcdefg")
    Observable<P2PBean> getP2PCount(@Query("sn") String sn, @Query("language") String language);


    @POST("abcdefg")
    Observable<ResponseBody> getAnswerV3(@Query("sn") String sn, @Body RequestBody body);


    @GET("abcdefg")
    Observable<ResponseBean<List<UnreachablePointBean>>> getUnreachablePoint(@Query("sn") String sn, @Query("language") String language);

    @POST("abcdefg")
    Observable<ResponseBean> uploadLocation(@Query("sn") String sn, @Query("language") String language, @Body RequestBody body);

    //受邀访客页面邀请码
    @GET("abcdefg")
    Observable<ResponseBody> getVisitInfo(@Query("sn") String sn, @Query("language") String language, @Query("invitationCode") String invitationCode);

    //受邀访客人脸识别
    @GET("abcdefg")
    Observable<ResponseBody> findVisitByFaceid(@Query("sn") String sn, @Query("language") String language, @Query("faceid") String faceid);

    @POST("abcdefg")
    Observable<ResponseBean> addVisitInfo(@Query("sn") String sn, @Query("language") String language, @Body RequestBody body);
}
