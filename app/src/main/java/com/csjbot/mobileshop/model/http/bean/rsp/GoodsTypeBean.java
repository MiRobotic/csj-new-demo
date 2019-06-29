package com.csjbot.mobileshop.model.http.bean.rsp;

import android.support.annotation.NonNull;

/**
 * 商品种类
 *
 * @author ShenBen
 * @date 2018/12/19 15:00
 * @email 714081644@qq.com
 */

public class GoodsTypeBean implements Comparable<GoodsTypeBean> {


    /**
     * id : 244
     * createById : 226
     * createByDate : 2019-01-07 10:32:34
     * modifyById : null
     * modifyByDate : 2019-01-07 10:43:03
     * deleted : 0
     * level : 1,226,
     * language : zh
     * name : 服装
     * code : /
     * sort : 1
     * floor : 1
     * goodsTypeEntities : null
     */

    private int id;
    private int createById;
    private String createByDate;
    private Object modifyById;
    private String modifyByDate;
    private int deleted;
    private String level;
    private String language;
    private String name;
    private String code;
    private int sort;
    private int floor;
    private Object goodsTypeEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreateById() {
        return createById;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }

    public String getCreateByDate() {
        return createByDate;
    }

    public void setCreateByDate(String createByDate) {
        this.createByDate = createByDate;
    }

    public Object getModifyById() {
        return modifyById;
    }

    public void setModifyById(Object modifyById) {
        this.modifyById = modifyById;
    }

    public String getModifyByDate() {
        return modifyByDate;
    }

    public void setModifyByDate(String modifyByDate) {
        this.modifyByDate = modifyByDate;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Object getGoodsTypeEntities() {
        return goodsTypeEntities;
    }

    public void setGoodsTypeEntities(Object goodsTypeEntities) {
        this.goodsTypeEntities = goodsTypeEntities;
    }

    @Override
    public int compareTo(@NonNull GoodsTypeBean o) {
        return getSort() - o.getSort();
    }
}
