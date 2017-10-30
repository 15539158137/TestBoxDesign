package com.example.administrator.testboxdesign.beans;

import android.widget.ImageView;

/**
 * Created by Administrator on 2017/10/16.
 */

public   class OneBoxItem {
    //这个盒子左上角的坐标
    double x;
    double y;
    //这个小格子是否被使用了
    boolean hadUse;
    //这个方格的id,从上横向开始一次为0s0;0s1,,用来对于超过一个表的表箱位置的判断.这个只是在添加的时候进行判断.右侧拖拽的时候使用下面的当前时间
    String ID;
    //当前时间毫秒数,如果这个数值相同,呢么表示他们是同一个表箱的不同部分
    double nowTime;
    //这个格子放置的view,用来做删除操作
    ImageView imageView;
    //给这个格子一个表箱类型,用来标识这个位置放的是哪种类型的表箱
    String boxType;
    //这个格子,在所属表箱的位置,0-8,从上往下  从左往右 依次递增
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getNowTime() {
        return nowTime;
    }

    public void setNowTime(double nowTime) {
        this.nowTime = nowTime;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public OneBoxItem() {
        this.hadUse = false;
        this.index=-1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isHadUse() {
        return hadUse;
    }

    public void setHadUse(boolean hadUse) {
        this.hadUse = hadUse;
    }
}
