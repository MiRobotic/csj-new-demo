package com.csjbot.mobileshop.guide_patrol.patrol.bean;

import com.csjbot.mobileshop.model.tcp.bean.Position;

import java.io.Serializable;

/**
 * Created by 孙秀艳 on 2019/1/19.
 */

public class PatrolBean implements Serializable {
    public String id;
    protected Position pos;
    protected String name;//巡视点名称
    protected float translationX;
    protected float translationY;
    public int left, right, bottom, top;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTranslationX() {
        return translationX;
    }

    public void setTranslationX(float translationX) {
        this.translationX = translationX;
    }

    public float getTranslationY() {
        return translationY;
    }

    public void setTranslationY(float translationY) {
        this.translationY = translationY;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
