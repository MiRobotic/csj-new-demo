package com.csjbot.mobileshop.feature.product.bean;


/**
 * @author ShenBen
 * @date 2018/11/13 10:06
 * @email 714081644@qq.com
 */

public class ProductTypeBean {
    private int id;
    private String goodsName;
    private String imageUrl;
    private double originalPrice;
    private double presentPrice;

    public ProductTypeBean() {
    }

    public ProductTypeBean(String goodsName, String imageUrl, double originalPrice, double presentPrice) {
        this.goodsName = goodsName;
        this.imageUrl = imageUrl;
        this.originalPrice = originalPrice;
        this.presentPrice = presentPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getPresentPrice() {
        return presentPrice;
    }

    public void setPresentPrice(double presentPrice) {
        this.presentPrice = presentPrice;
    }

    @Override
    public String toString() {
        return "ProductTypeBean{" +
                "id=" + id +
                ", goodsName='" + goodsName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", originalPrice=" + originalPrice +
                ", presentPrice=" + presentPrice +
                '}';
    }
}
