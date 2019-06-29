package com.csjbot.mobileshop.feature.nearbyservice.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * @author ShenBen
 * @date 2019/1/24 11:49
 * @email 714081644@qq.com
 */
public class PoiSearchBean {
    private String uid;
    private String name;
    private String address;
    private String distance;
    private LatLng location;
    private boolean isChecked;

    public PoiSearchBean() {
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "PoiSearchBean{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", distance='" + distance + '\'' +
                ", location=" + location +
                ", isChecked=" + isChecked +
                '}';
    }
}
