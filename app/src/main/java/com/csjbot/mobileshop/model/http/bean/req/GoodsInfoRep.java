package com.csjbot.mobileshop.model.http.bean.req;

import java.util.Arrays;

/**
 * @author ShenBen
 * @date 2018/12/20 18:07
 * @email 714081644@qq.com
 */

public class GoodsInfoRep {
    private int[] ids;
    private String typeId;
    private String costPricePro;
    private String costPriceNex;
    private String nowPricePro;
    private String nowPriceNex;
    private String usingWay;
    private String weightPro;
    private String weightNex;

    public GoodsInfoRep() {
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getCostPricePro() {
        return costPricePro;
    }

    public void setCostPricePro(String costPricePro) {
        this.costPricePro = costPricePro;
    }

    public String getCostPriceNex() {
        return costPriceNex;
    }

    public void setCostPriceNex(String costPriceNex) {
        this.costPriceNex = costPriceNex;
    }

    public String getNowPricePro() {
        return nowPricePro;
    }

    public void setNowPricePro(String nowPricePro) {
        this.nowPricePro = nowPricePro;
    }

    public String getNowPriceNex() {
        return nowPriceNex;
    }

    public void setNowPriceNex(String nowPriceNex) {
        this.nowPriceNex = nowPriceNex;
    }

    public String getUsingWay() {
        return usingWay;
    }

    public void setUsingWay(String usingWay) {
        this.usingWay = usingWay;
    }

    public String getWeightPro() {
        return weightPro;
    }

    public void setWeightPro(String weightPro) {
        this.weightPro = weightPro;
    }

    public String getWeightNex() {
        return weightNex;
    }

    public void setWeightNex(String weightNex) {
        this.weightNex = weightNex;
    }

    @Override
    public String toString() {
        return "GoodsInfoRep{" +
                "ids=" + Arrays.toString(ids) +
                ", typeId='" + typeId + '\'' +
                ", costPricePro='" + costPricePro + '\'' +
                ", costPriceNex='" + costPriceNex + '\'' +
                ", nowPricePro='" + nowPricePro + '\'' +
                ", nowPriceNex='" + nowPriceNex + '\'' +
                ", usingWay='" + usingWay + '\'' +
                ", weightPro='" + weightPro + '\'' +
                ", weightNex='" + weightNex + '\'' +
                '}';
    }
}
