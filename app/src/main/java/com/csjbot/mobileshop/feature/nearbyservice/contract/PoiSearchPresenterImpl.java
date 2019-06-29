package com.csjbot.mobileshop.feature.nearbyservice.contract;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.csjbot.mobileshop.R;
import com.csjbot.mobileshop.feature.nearbyservice.adapter.PoiSearchAdapter;
import com.csjbot.mobileshop.feature.nearbyservice.bean.PoiSearchBean;
import com.csjbot.mobileshop.global.Constants;
import com.csjbot.mobileshop.util.CSJToast;
import com.csjbot.mobileshop.util.SharedKey;
import com.csjbot.mobileshop.util.SharedPreUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenBen
 * @date 2019/1/23 17:20
 * @email 714081644@qq.com
 */
public class PoiSearchPresenterImpl implements PoiSearchContract.Presenter, OnGetPoiSearchResultListener, OnGetSuggestionResultListener, OnGetRoutePlanResultListener {
    private static final String TAG = "PoiSearchPresenterImpl-->";
    private Context mContext;
    private PoiSearchContract.View mView;

    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;
    private BaiduMap mBaiduMap;
    private LatLng mDefualtLng = new LatLng(31.391546, 120.987261);//昆山市中心
    private LatLng mCenterLng;//当前定位的中心
    private List<PoiSearchBean> mAddressList;
    private PoiSearchAdapter mAdapter;
    private int mRadius = 2000;//搜索半径
    /**
     * 是否使用默认的搜索地点
     * false：百度定位到的位置
     * true：默认的 昆山市中心
     */
    private boolean isDefualtLatLng = false;
    /**
     * 搜索的关键词
     */
    private String mKey;

    public PoiSearchPresenterImpl(Context mContext, PoiSearchContract.View mView) {
        this.mContext = mContext;
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void init() {
        mAddressList = new ArrayList<>();
        mAdapter = new PoiSearchAdapter();
        mView.getRecycler().setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.bindToRecyclerView(mView.getRecycler());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mAdapter.getLastPosition() == position) {
                return;
            }
            mAdapter.checkAddress(position);
            reset();
            searchRoute(PlanNode.withLocation(mAddressList.get(position).getLocation()));
        });
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mBaiduMap = mView.getMapView().getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(19).build()));

        String longitude = SharedPreUtil.getString(SharedKey.LOCATION, SharedKey.LONGITUDE);
        String latitude = SharedPreUtil.getString(SharedKey.LOCATION, SharedKey.LATITUDE);
        if (!TextUtils.isEmpty(longitude) && !TextUtils.isEmpty(latitude)) {
            mCenterLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        } else {
            mCenterLng = new LatLng(Constants.LocationInfo.latitude, Constants.LocationInfo.longitude);
        }
    }

    @Override
    public void searchPlace(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mKey = key;
        if (TextUtils.equals("快捷酒店", key) || TextUtils.equals("出租车", key)
                || TextUtils.equals("景点", key)) {
            mRadius = mRadius * 10;
        }
        isDefualtLatLng = false;
        searchNearBy(key, mCenterLng);
    }

    @Override
    public void reset() {
        if (isDefualtLatLng) {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mDefualtLng));
        } else {
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mCenterLng));
        }
    }

    @Override
    public void destroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
    }

    /****************************************PoiSearch相关*****************************************************/
    @SuppressLint("DefaultLocale")
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetPoiResult: error: " + poiResult.error);
        if (poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {//搜索没有结果时，用昆山市政府搜索
            if (isDefualtLatLng) {
                mBaiduMap.clear();
                return;
            }
            isDefualtLatLng = true;
            searchNearBy(mKey, mDefualtLng);
            return;
        }
        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new PoiOverlay(mBaiduMap) {
                @Override
                public boolean onPoiClick(int i) {
                    PoiInfo poi = getPoiResult().getAllPoi().get(i);
                    mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
                    PlanNode node = PlanNode.withLocation(poi.location);
                    searchRoute(node);
                    return true;
                }
            };
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(poiResult);
            overlay.addToMap();
            overlay.zoomToSpan();
            if (isDefualtLatLng) {
                showNearbyArea(mDefualtLng, mRadius);
            } else {
                showNearbyArea(mCenterLng, mRadius);
            }
            mAddressList.clear();
            if (poiResult.getAllPoi() != null && !poiResult.getAllPoi().isEmpty()) {
                int index = 0;
                PoiSearchBean bean;
                for (PoiInfo info : poiResult.getAllPoi()) {
                    if (index >= PoiOverlay.MAX_POI_SIZE) {
                        break;
                    }
                    if (info.location == null) {
                        continue;
                    }
                    ++index;
                    bean = new PoiSearchBean();
                    bean.setUid(info.uid);
                    bean.setName(info.name);
                    bean.setAddress(info.address);
                    bean.setLocation(info.location);
                    bean.setChecked(false);
                    bean.setDistance(String.format("%.2f",
                            DistanceUtil.getDistance(isDefualtLatLng ?
                                    mDefualtLng : mCenterLng, info.location) / 1000) + "km");//手动计算距离
                    mAddressList.add(bean);
                }
            }
            mAdapter.setNewData(mAddressList);
        } else if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            StringBuilder strInfo = new StringBuilder("在");
            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo.append(cityInfo.city);
                strInfo.append(",");
            }
            strInfo.append("找到结果");
            CSJToast.showToast(mContext, strInfo.toString(), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetPoiDetailResult: error: " + poiDetailResult.error);
        if (poiDetailResult.error == SearchResult.ERRORNO.NO_ERROR) {
            CsjlogProxy.getInstance().info(TAG + "onGetPoiDetailResult: 搜索详情: " + poiDetailResult.name);
            //更新选中状态
            int size = mAddressList.size();
            PoiSearchBean bean;
            for (int j = 0; j < size; j++) {
                bean = mAddressList.get(j);
                if (TextUtils.equals(bean.getUid(), poiDetailResult.uid)) {
                    mAdapter.checkAddress(j);
                    break;
                }
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetPoiDetailResult: error: " + poiDetailSearchResult.error);

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetPoiIndoorResult: error: " + poiIndoorResult.error);

    }


    /****************************************SuggestionSearch相关*****************************************************/
    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetSuggestionResult: error: " + suggestionResult.error);

    }

    /****************************************创建路线相关*****************************************************/
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetWalkingRouteResult: error: " + walkingRouteResult.error);
        if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            if (walkingRouteResult.getRouteLines() != null && !walkingRouteResult.getRouteLines().isEmpty()) {//路线不为空
                //创建WalkingRouteOverlay实例
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                overlay.setData(walkingRouteResult.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetTransitRouteResult: error: " + transitRouteResult.error);
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetMassTransitRouteResult: error: " + massTransitRouteResult.error);
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetDrivingRouteResult: error: " + drivingRouteResult.error);
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetIndoorRouteResult: error: " + indoorRouteResult.error);
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        CsjlogProxy.getInstance().info(TAG + "onGetBikingRouteResult: error: " + bikingRouteResult.error);
    }

    /**
     * poi搜索
     *
     * @param key
     * @param latLng
     */
    private void searchNearBy(String key, LatLng latLng) {
        PoiNearbySearchOption option = new PoiNearbySearchOption()
                .location(latLng)//中心
                .radius(mRadius)//搜索半径
                .keyword(key)//关键词
                .pageNum(0)//分页编号，默认返回第0页结果
                .pageCapacity(20)//设置每页容量，默认为10条结果
                .sortType(PoiSortType.distance_from_near_to_far)
                .scope(1);//值为1 或 空，返回基本信息,值为2，返回POI详细信息
        mPoiSearch.searchNearby(option);
    }

    /**
     * 绘制中心区域
     *
     * @param center
     * @param radius
     */
    private void showNearbyArea(LatLng center, int radius) {
        BitmapDescriptor centerBitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_openmap_focuse_mark);
        MarkerOptions ooMarker = new MarkerOptions().position(center).icon(centerBitmap);
        mBaiduMap.addOverlay(ooMarker);
        //添加定位中心点颜色
        OverlayOptions ooDot = new DotOptions()
                .center(center)
                .radius(5)
                .color(0xFF346BDA);
        mBaiduMap.addOverlay(ooDot);
        //添加圆圈颜色
        OverlayOptions ooCircle = new CircleOptions()
                .fillColor(0x55346BDA)
                .center(center)
                .stroke(new Stroke(3, 0x55346BDA))
                .radius(radius);
        mBaiduMap.addOverlay(ooCircle);
    }

    /**
     * 规划路线
     *
     * @param node
     */
    private void searchRoute(PlanNode node) {
        mBaiduMap.clear();
        PlanNode stNode;
        if (isDefualtLatLng) {
            stNode = PlanNode.withLocation(mDefualtLng);
        } else {
            stNode = PlanNode.withLocation(mCenterLng);
        }
        // 注册 路径规划 服务
        RoutePlanSearch search = RoutePlanSearch.newInstance();
        search.setOnGetRoutePlanResultListener(this);
        search.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(node));
    }
}
