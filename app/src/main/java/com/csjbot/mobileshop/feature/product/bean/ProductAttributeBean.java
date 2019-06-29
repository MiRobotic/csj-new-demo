package com.csjbot.mobileshop.feature.product.bean;

/**
 * @author ShenBen
 * @date 2019/1/9 16:06
 * @email 714081644@qq.com
 */

public class ProductAttributeBean {
    private String attribute;
    private String label;

    public ProductAttributeBean(String attribute, String label) {
        this.attribute = attribute;
        this.label = label;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ProductAttributeBean{" +
                "attribute='" + attribute + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
