package com.csjbot.mobileshop.feature.product.bean;

/**
 * @author ShenBen
 * @date 2018/12/7 15:54
 * @email 714081644@qq.com
 */

public class SelectClothColor {
    private String color;
    private boolean isChecked;

    public SelectClothColor(String color, boolean isChecked) {
        this.color = color;
        this.isChecked = isChecked;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
