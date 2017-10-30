package com.example.administrator.testboxdesign.designbox_view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.testboxdesign.BoxTypes;
import com.example.administrator.testboxdesign.R;
import com.example.administrator.testboxdesign.beans.OneBoxItem;
import com.example.administrator.testboxdesign.designbox_utils.SaveUtil;
import com.zhy.android.percent.support.PercentFrameLayout;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/27.
 */
//分割区域，在分割的时候就定下来了所有可以使用的表箱，这个表箱自带ID，这个id就可以分割出来他所在的横纵坐标；
    //在添加的时候，根据拖动的位置，定下来这个表箱所在的位置，对于表箱上的单个表的位置，依靠字段index来确定，从0开始；确定一个表箱中的所有表，使用time来确定，这个字段设置是在添加时候设置，如果时间相同，则表示这些小表箱属于一个大表箱、
public class BoxDesignView extends PercentFrameLayout {
    View drawView;
    public BoxDesignView(@NonNull Context context) {
        super(context);
    }

    public BoxDesignView(@NonNull Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);


        View view = LayoutInflater.from(context).inflate(R.layout.right_boxs_design_layout, this, true);
        drawView = view.findViewById(R.id.drawview);
        setTouchEvent_Right();
    }

    public BoxDesignView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    int ExampleWidth;
    int ExampleHeight;
    int BoxWidth;
    int BoxHeight;
    ViewGroup mainView;
    //把布局分割出来

    /**
     * @param ExampleWidth  左侧例子的宽度，因为间隔是拿左侧例子做参考的
     * @param ExampleHeight
     * @param BoxWidth      右侧实际的宽高
     * @param BoxHeight
     * @param mainView      整个页面的view
     */
    public void spiltBox(int ExampleWidth, int ExampleHeight, int BoxWidth, int BoxHeight, ViewGroup mainView) {

        this.mainView = mainView;
        this.ExampleHeight=ExampleHeight;
        this.ExampleWidth=ExampleWidth;
        this.BoxHeight=BoxHeight;
        this.BoxWidth=BoxWidth;
        //对画布区域进行分格子处理
        //以区域的左上角的坐标原点
        //先获取右侧区域的坐标,
        float x = drawView.getLeft();
        float y = drawView.getTop();
        //再获取右侧区域的大小
        float width = drawView.getRight() - drawView.getLeft();
        float height = drawView.getBottom() - drawView.getTop();
//对于这个四周留白区域大小的计算
        double xCan=width/BoxWidth;
        double x_yushou=width%BoxWidth;
        double yCan=height/BoxHeight;
        double y_yushou=height%BoxHeight;


        //对于这个区域,需要一个边沿的空白,这个区域的大小为左侧实例box的1/4宽,及examplewidth/4.实例的宽度为百分之五宽度,右侧绘图区域的大小是百分之8
        //算出在X,Y方向上所能容纳的个数
        double temp = ((width - ExampleWidth / 8) / BoxWidth);
        double temp1 = (height - ExampleHeight / 8) / BoxHeight;
        //因为存在留白区域,所以对于实际显示区域的起始坐标,需要计算下
        double startX = x + (ExampleWidth / 16);
        double startY = y + (ExampleHeight / 16);

        double endX = drawView.getRight() - ExampleWidth / 16;
        double endY = drawView.getBottom() - ExampleHeight / 16;
        SaveUtil.startX=startX;
        SaveUtil.startY=startY;
        SaveUtil.endX=endX;
        SaveUtil.endY=endY;

        int X_canuse = splitDouble(temp);

        int Y_canuse = splitDouble(temp1);
//从左往右,横向的加


        for (int i = 0; i < Y_canuse; i++) {
            for (int j = 0; j < X_canuse; j++) {
                //从第一行的第一个开始,向右走,然后第二行...
                OneBoxItem oneBoxItem = new OneBoxItem();
                //这里的j表示从左往右的第几行
                oneBoxItem.setX(startX + BoxWidth * j);
                //这里的i表示从上网下的第几列
                oneBoxItem.setY(startY + BoxHeight * i);
                oneBoxItem.setID(i + "s" + j);
                if (SaveUtil.AllCanUseBox == null || SaveUtil.AllCanUseBox.size() == 0) {
                    SaveUtil.AllCanUseBox = new ArrayList<OneBoxItem>();
                }

                ImageView uu = new ImageView(getContext());
                PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.width = BoxWidth;
                layoutParams.height = BoxHeight;
                uu.setAlpha(0.1f);
                uu.setLayoutParams(layoutParams);
                uu.setX((float) oneBoxItem.getX());
                uu.setY((float) oneBoxItem.getY());
                uu.setBackground(getResources().getDrawable(R.drawable.i1x1));
                mainView.addView(uu);

                SaveUtil.AllCanUseBox.add(oneBoxItem);
            }
        }
    }

    //把小数转为整数,只取整数部分
    private int splitDouble(double d) {

        String temp = String.valueOf(d);
        int result = 0;
        if (temp.length() > 2) {
            String temp2 = temp.substring(0, temp.indexOf("."));
            result = Integer.parseInt(temp2);
        }
        return result;
    }
    //给右侧的绘图区域设置点击事件
    //这个参数用来表示 在出发down之后 是否还需要出发他的move.当点到的是表箱的时候,才会出发move
    boolean needToMove = false;
    //需要触发move事件的这个表箱
    OneBoxItem TheBoxT = null;
    //这个是右侧区域拖动时候显示的图片,每次都新建,完成后删除
    ImageView showImage_Right = null;

    private void setTouchEvent_Right() {
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e("右侧点击", event.getRawX() + "第一个的坐标" + SaveUtil.AllCanUseBox.get(0).getX() + "==" + SaveUtil.AllCanUseBox.get(1).getX());
                        //注意,这里也需要对不同的表箱类型进行相应的判断

                        float x = event.getRawX();
                        float y = event.getRawY();

                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                            if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                                    && oneBoxItem.isHadUse() == true) {
                                Log.e("这个地方有表箱", "====");
                                //表示这个位置有表箱,呢就显示新增个虚拟的表箱吧
                                showImage_Right = new ImageView(getContext());
                                showImage_Right.setAlpha(0.3f);
                                PercentRelativeLayout.LayoutParams layoutParamsShow = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                //类型判断
                                if (oneBoxItem.getBoxType().equals(BoxTypes.Old_1_X)) {
                                    //对于单表箱的,直接一个判断就可以了
                                    layoutParamsShow.width = BoxWidth;
                                    layoutParamsShow.height = BoxHeight;
                                    showImage_Right.setX(event.getRawX() - BoxWidth / 2);
                                    showImage_Right.setY(event.getRawY() - BoxHeight / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i1x1));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_2_X)) {
                                    //表示是横向二表箱,就需要把他当左或者右来处理
                                    layoutParamsShow.width = BoxWidth * 2;
                                    layoutParamsShow.height = BoxHeight;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i1x2));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_2_Y)) {
                                    //表示是纵向的2表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth;
                                    layoutParamsShow.height = BoxHeight * 2;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i2x1));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_3_X)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 3;
                                    layoutParamsShow.height = BoxHeight;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i1x3));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_3_Y)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth;
                                    layoutParamsShow.height = BoxHeight * 3;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i3x1));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.New_4_Y)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth;
                                    layoutParamsShow.height = BoxHeight * 4;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i4x1));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.New_4_X)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 4;
                                    layoutParamsShow.height = BoxHeight;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i1x4));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.New_4_XY)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 2;
                                    layoutParamsShow.height = BoxHeight* 2;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i2x2));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_6_Y)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 2;
                                    layoutParamsShow.height = BoxHeight* 3;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i3x2));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_6_X)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 3;
                                    layoutParamsShow.height = BoxHeight* 2;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i2x3));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_9_XY)) {
                                    //表示是横向的3表箱,创建一个拖动的虚影子
                                    layoutParamsShow.width = BoxWidth * 3;
                                    layoutParamsShow.height = BoxHeight* 3;
                                    showImage_Right.setX(event.getRawX() - layoutParamsShow.width / 2);
                                    showImage_Right.setY(event.getRawY() - layoutParamsShow.height / 2);
                                    showImage_Right.setBackground(getResources().getDrawable(R.drawable.i3x3));
                                    needToMove = true;
                                    TheBoxT = oneBoxItem;
                                }
                                showImage_Right.setLayoutParams(layoutParamsShow);
                                mainView.addView(showImage_Right);


                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (needToMove) {
                            if (TheBoxT == null || TheBoxT.getBoxType().equals("")) {
                                Log.e("在获取表箱类型时候出现异常", "====");
                                break;
                            }
                            withRightOnTuch(event.getRawX(), event.getRawY(), TheBoxT);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (needToMove) {
                            if (TheBoxT == null || TheBoxT.getBoxType().equals("")) {
                                Log.e("up事件拿表箱类型异常", "====");
                                mainView.removeView(showImage_Right);
                            } else {
                                //如果超出右侧区域,表示是想删除这个表箱.注意,这个虽然存在右侧区域上下左右1/2间隔的问题,但是这个问题会在判断放置的位置是否有空格的时候被解决
                                if (event.getRawX() <= drawView.getLeft() || event.getRawY() <= drawView.getTop() || event.getRawX() >= drawView.getRight() || event.getRawY() >= drawView.getBottom()) {
                                    //超出了区域,表示是需要删除了.注意:还需要判断是哪张类型.这个传过来的是一定要删的
                                    mainView.removeView(TheBoxT.getImageView());
                                    TheBoxT.setHadUse(false);
                                    if (TheBoxT.getBoxType().equals(BoxTypes.Old_1_X)) {
                                    } else {
                                        //超过一个的,就存在多个了
                                        List<Integer> needDelte = new ArrayList();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                needDelte.add(i);
                                            }
                                        }
                                        if (needDelte.size() < 1) {
                                            Toast.makeText(getContext(), "删除失败,请重试", Toast.LENGTH_SHORT).show();
                                        } else {
                                            for (int del : needDelte) {
                                                SaveUtil.AllCanUseBox.get(del).setHadUse(false);
                                            }
                                        }

                                    }
                                    mainView.removeView(showImage_Right);
                                } else {
                                    /**
                                     * 拖动到右侧可绘制的区域内部
                                     */
                                    //还在区域内,就需要判断,拖动位置是否空余,在不空余的时候,如果这个ID和现在的ID相同,就表示还是在老地方,位置不变
                                    float x1 = event.getRawX();
                                    float y1 = event.getRawY();
                                    int canUse = 0;
                                    if (TheBoxT.getBoxType().equals(BoxTypes.Old_1_X)) {
                                        for (OneBoxItem oneBoxItem : SaveUtil.AllCanUseBox) {
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight
                                                    && oneBoxItem.isHadUse() == false) {
                                                //表示放置的位置是个空白
                                                //实例出来一个新的表箱,用来添加到右侧页面上
                                                ImageView addImage = new ImageView(getContext());
                                                PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                layoutParams.width = BoxWidth;
                                                layoutParams.height = BoxHeight;
                                                addImage.setLayoutParams(layoutParams);
                                                addImage.setX((float) oneBoxItem.getX());
                                                addImage.setY((float) oneBoxItem.getY());
                                                addImage.setImageResource(R.drawable.i1x1);
                                                addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));

                                                mainView.addView(addImage);
                                                oneBoxItem.setHadUse(true);
                                                oneBoxItem.setIndex(0);
                                                oneBoxItem.setImageView(addImage);
                                                oneBoxItem.setBoxType(BoxTypes.Old_1_X);
                                                TheBoxT.setHadUse(false);
//让原来的位置的状态变为可用,并删除之前的内容.对于新增的这个半透明的图片,一并删除
                                                mainView.removeView(TheBoxT.getImageView());
                                                mainView.removeView(showImage_Right);
                                                canUse++;
                                            }
                                        }
                                    } else if (TheBoxT.getBoxType().equals(BoxTypes.Old_A_New_2_X)) {
                                        //先拿到所有表格的下标和ID之间的对应关系
                                        Map map = new HashMap();
                                        //再找到另外那个存放的表格位置
                                        int otherTableIndex = 0;
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                otherTableIndex = i;
                                            }
                                        }
                                        //把这个点当做是两个表箱左侧的这个,如果这个格子右边还有空余的,就放置,没有就取消
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //如果他在这个区域内部
                                                //算出需要的两个格子的位置.一个就是现在的onBoxItem,另一个就需要计算了
                                                String ID_Right = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) + 1);
                                                try {
                                                    int index = (int) map.get(ID_Right);
                                                    if ((oneBoxItem.getNowTime() == TheBoxT.getNowTime() || oneBoxItem.isHadUse() == false) && (SaveUtil.AllCanUseBox.get(index).getNowTime() == TheBoxT.getNowTime() || SaveUtil.AllCanUseBox.get(index).isHadUse() == false)) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth * 2;
                                                        layoutParams.height = BoxHeight;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) oneBoxItem.getX());
                                                        addImage.setY((float) oneBoxItem.getY());
                                                        addImage.setImageResource(R.drawable.i1x2);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //因为可能存在覆盖的情况,所以先删除老数据,再写入新数据
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        SaveUtil.AllCanUseBox.get(otherTableIndex).setHadUse(false);
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);

                                                        //加入新数据
                                                        //左侧这个的数据设置
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setIndex(0);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_A_New_2_X);
                                                        //右侧这个数据的设置
                                                        SaveUtil.AllCanUseBox.get(index).setIndex(1);
                                                        SaveUtil.AllCanUseBox.get(index).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(index).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(index).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(index).setBoxType(BoxTypes.Old_A_New_2_X);
                                                        mainView.addView(addImage);
                                                        //把这个置空,
                                                        showImage_Right = null;
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }
                                        }
                                    } else if (TheBoxT.getBoxType().equals(BoxTypes.Old_2_Y)) {
                                        //先拿到所有表格的下标和ID之间的对应关系
                                        Map map = new HashMap();
                                        //再找到另外那个存放的表格位置
                                        int otherTableIndex = 0;
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                otherTableIndex = i;
                                            }
                                        }
                                        //把这个点当做是两个表箱上侧的这个,如果这个格子右边还有空余的,就放置,没有就取消
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            //只需要判断相关联的box是false或者为相同的就可以了
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //这个是纵向2的底部
                                                String ID_Right = String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))) + 1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())));
                                                try {
                                                    int index = (int) map.get(ID_Right);
                                                    if ((oneBoxItem.getNowTime() == TheBoxT.getNowTime() || oneBoxItem.isHadUse() == false) && (SaveUtil.AllCanUseBox.get(index).getNowTime() == TheBoxT.getNowTime() || SaveUtil.AllCanUseBox.get(index).isHadUse() == false)) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth;
                                                        layoutParams.height = BoxHeight * 2;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) oneBoxItem.getX());
                                                        addImage.setY((float) oneBoxItem.getY());
                                                        addImage.setImageResource(R.drawable.i2x1);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
//先置空,再新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        SaveUtil.AllCanUseBox.get(otherTableIndex).setHadUse(false);
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个原图和虚拟的图
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);

                                                        //更新下页面和数据
                                                        mainView.addView(addImage);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setIndex(0);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_2_Y);
                                                        SaveUtil.AllCanUseBox.get(index).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(index).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(index).setIndex(1);
                                                        SaveUtil.AllCanUseBox.get(index).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(index).setBoxType(BoxTypes.Old_2_Y);

                                                        showImage_Right = null;

                                                    }
                                                } catch (Exception e) {
                                                }

                                            }
                                        }
                                    } else if (TheBoxT.getBoxType().equals(BoxTypes.Old_A_New_3_X)) {
                                        //表示拖动的是个横向的3表箱,进行位置判断吧
                                        //先拿到所有表格的下标和ID之间的对应关系
                                        //这里还需要找到需要删除的那两个
                                        List<Integer> list = new ArrayList<Integer>();
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if(x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight){
                                                //表示是个格子
                                                //右侧的这个
                                                String ID_Right = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) + 1);
                                                //左侧的这个
                                                String ID_Left = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) - 1);
                                                try {
                                                    int indexRight = (int) map.get(ID_Right);
                                                    int indexLeft = (int) map.get(ID_Left);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexRight).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexRight).getNowTime()==TheBoxT.getNowTime()) && (SaveUtil.AllCanUseBox.get(indexLeft).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexLeft).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth * 3;
                                                        layoutParams.height = BoxHeight;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexLeft).getX());
                                                        addImage.setY((float) SaveUtil.AllCanUseBox.get(indexLeft).getY());
                                                        addImage.setImageResource(R.drawable.i1x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);

                                                        //新增数据
                                                        mainView.addView(addImage);
                                                        //左侧这个的数据设置
                                                        oneBoxItem.setIndex(1);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_A_New_3_X);
                                                        //右侧这个数据的设置
                                                        SaveUtil.AllCanUseBox.get(indexRight).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_3_X);
                                                        //左侧这个
                                                        SaveUtil.AllCanUseBox.get(indexLeft).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexLeft).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexLeft).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexLeft).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexLeft).setBoxType(BoxTypes.Old_A_New_3_X);

                                                        //删除这个复制的图片
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适三横表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.Old_A_New_3_Y)){
                                        //表示拖动的是个横向的3表箱,进行位置判断吧
                                        //先拿到所有表格的下标和ID之间的对应关系
                                        //这里还需要找到需要删除的那两个
                                        List<Integer> list = new ArrayList<Integer>();
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if(x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight){
                                                //表示是个格子
                                                //右侧的这个
                                                String ID_Top =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //左侧的这个
                                                String ID_Bottom =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                try {
                                                    int indexTop = (int) map.get(ID_Top);
                                                    int indexBottom = (int) map.get(ID_Bottom);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexTop).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop).getNowTime()==TheBoxT.getNowTime()) && (SaveUtil.AllCanUseBox.get(indexBottom).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth ;
                                                        layoutParams.height = BoxHeight* 3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexTop).getX());
                                                        addImage.setY((float) SaveUtil.AllCanUseBox.get(indexTop).getY());
                                                        addImage.setImageResource(R.drawable.i3x1);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);

                                                        //新增数据
                                                        mainView.addView(addImage);
                                                        //左侧这个的数据设置
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setIndex(1);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_A_New_3_Y);
                                                        //右侧这个数据的设置
                                                        SaveUtil.AllCanUseBox.get(indexTop).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setBoxType(BoxTypes.Old_A_New_3_Y);
                                                        //左侧这个
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setBoxType(BoxTypes.Old_A_New_3_Y);

                                                        //删除这个复制的图片
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适三竖表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.New_4_Y)){
                                        //现在点击的位置是纵向4个的第二个,算出其他三个的位置
                                        List<Integer> list = new ArrayList<Integer>();
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if(x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight){
                                                //表示是个格子
                                                //顶部的这个
                                                String ID_Top =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //下一的这个
                                                String ID_Bottom =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //下部第二个
                                                String ID_Bottom_2 =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+2)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );



                                                try {
                                                    int indexTop = (int) map.get(ID_Top);
                                                    int indexBottom = (int) map.get(ID_Bottom);
                                                    int indexBottom_2 = (int) map.get(ID_Bottom_2);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexTop).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop).getNowTime()==TheBoxT.getNowTime()) && (SaveUtil.AllCanUseBox.get(indexBottom).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(SaveUtil.AllCanUseBox.get(indexBottom_2).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_2).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth ;
                                                        layoutParams.height = BoxHeight* 4;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexTop).getX());
                                                        addImage.setY((float) SaveUtil.AllCanUseBox.get(indexTop).getY());
                                                        addImage.setImageResource(R.drawable.i4x1);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);

                                                        //新增数据
                                                        mainView.addView(addImage);
                                                        //左侧这个的数据设置
                                                        oneBoxItem.setIndex(1);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.New_4_Y);

                                                        SaveUtil.AllCanUseBox.get(indexTop).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop).setBoxType(BoxTypes.New_4_Y);
                                                        //纵3
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom).setBoxType(BoxTypes.New_4_Y);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_2).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_2).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_2).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_2).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_2).setBoxType(BoxTypes.New_4_Y);

                                                        //删除这个复制的图片
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适三竖表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.New_4_X)){
                                        //现在点击的位置是横向4个的第二个,算出其他三个的位置
                                        List<Integer> list = new ArrayList<Integer>();
                                        //横4,以从上开始数的第二个作为这个点击的
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //纵向两个表箱的判断,假设这个是两个表箱上部的这个,呢么另一个的ID应该是XsY+1,对应ID中的数据AsB为 A+1,B
                                                //左侧部的这个
                                                String ID_Left =oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );
                                                //第一个右侧
                                                String ID_Right =oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))+1 );
                                                //右侧第二个
                                                String ID_Right_2 =oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))+2 );

                                                try {
                                                    int index_Left = (int) map.get(ID_Left);
                                                    int index_Right = (int) map.get(ID_Right);
                                                    int index_Right_2 = (int) map.get(ID_Right_2);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(index_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(index_Left).getNowTime()==TheBoxT.getNowTime()) && (SaveUtil.AllCanUseBox.get(index_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(index_Right).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(SaveUtil.AllCanUseBox.get(index_Right_2).isHadUse() == false||SaveUtil.AllCanUseBox.get(index_Right_2).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth* 4 ;
                                                        layoutParams.height = BoxHeight;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(index_Left).getX());
                                                        addImage.setY((float) SaveUtil.AllCanUseBox.get(index_Left).getY());
                                                        addImage.setImageResource(R.drawable.i1x4);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);
                                                        //删除完毕


                                                        mainView.addView(addImage);
                                                        //设置新增的数据
                                                        oneBoxItem.setIndex(1);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.New_4_X);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(index_Left).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(index_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(index_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(index_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(index_Left).setBoxType(BoxTypes.New_4_X);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(index_Right).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(index_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(index_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(index_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(index_Right).setBoxType(BoxTypes.New_4_X);

                                                        SaveUtil.AllCanUseBox.get(index_Right_2).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(index_Right_2).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(index_Right_2).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(index_Right_2).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(index_Right_2).setBoxType(BoxTypes.New_4_X);
                                                        //删除这个复制的图片
                                                        mainView.removeView(showImage_Right);
                                                        ;
                                                        showImage_Right = null;
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }

                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.New_4_XY)){
                                        //田子4,以从上开始数的第一个作为这个点击的,及左上角
                                        List<Integer> list = new ArrayList<Integer>();
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //纵向两个表箱的判断,假设这个是两个表箱上部的这个,呢么另一个的ID应该是XsY+1,对应ID中的数据AsB为 A+1,B
                                                //上部的这个
                                                String ID_Top_Right =oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);
                                                //下部第一个
                                                String ID_Bottom_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //下部第二个
                                                String ID_Bottom_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);

                                                try {
                                                    int indexTop_Right = (int) map.get(ID_Top_Right);
                                                    int indexBottom_Left = (int) map.get(ID_Bottom_Left);
                                                    int indexBottom_Right = (int) map.get(ID_Bottom_Right);
                                                    //判断左右两个是不是为空

                                                    if ((SaveUtil.AllCanUseBox.get(indexTop_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime()) && (SaveUtil.AllCanUseBox.get(indexBottom_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(SaveUtil.AllCanUseBox.get(indexBottom_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*2 ;
                                                        layoutParams.height = BoxHeight* 2;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) oneBoxItem.getX());
                                                        addImage.setY((float) oneBoxItem.getY());
                                                        addImage.setImageResource(R.drawable.i2x2);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空操作
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                        //删除完毕

                                                        mainView.addView(addImage);
                                                        //设置新增的数据
                                                        oneBoxItem.setIndex(0);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.New_4_XY);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setIndex(1);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setBoxType(BoxTypes.New_4_XY);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setBoxType(BoxTypes.New_4_XY);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setBoxType(BoxTypes.New_4_XY);

                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.Old_A_New_6_Y)){
                                        List<Integer> list = new ArrayList<Integer>();
                                        //田子6,纵向的,这个触摸点为左侧第二排的格子
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //当前为第二排左侧
                                                //上部左侧
                                                String ID_Top_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //上部的右侧
                                                String ID_Top_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);
                                                //正右侧
                                                String ID_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);
                                                //下部第一个
                                                String ID_Bottom_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //下部第二个
                                                String ID_Bottom_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);

                                                try {
                                                    int indexTop_Left = (int) map.get(ID_Top_Left);
                                                    int indexTop_Right = (int) map.get(ID_Top_Right);
                                                    int indexRight = (int) map.get(ID_Right);
                                                    int indexBottom_Left = (int) map.get(ID_Bottom_Left);
                                                    int indexBottom_Right = (int) map.get(ID_Bottom_Right);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexTop_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexTop_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexRight).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*2 ;
                                                        layoutParams.height = BoxHeight* 3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  SaveUtil.AllCanUseBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i3x2);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空操作
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                        //删除完毕
                                                        //清空完毕
                                                        mainView.addView(addImage);
                                                        //设置新增的数据
                                                        oneBoxItem.setIndex(2);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_A_New_6_Y);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_6_Y);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setIndex(1);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        SaveUtil.AllCanUseBox.get(indexRight).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setIndex(4);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setIndex(5);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_6_Y);
                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.Old_6_X)){
                                        List<Integer> list = new ArrayList<Integer>();
                                        //田子6,纵向的,这个触摸点为第一排第二个的格子
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight
                                                    ) {
                                                //当前为第一排第二个
                                                //上部左侧
                                                String ID_Top_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );
                                                //上部的右侧
                                                String ID_Top_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);
                                                //下部正右
                                                String ID_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //下部第一个
                                                String ID_Bottom_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );
                                                //下部第二个
                                                String ID_Bottom_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);

                                                try {
                                                    int indexTop_Left = (int) map.get(ID_Top_Left);
                                                    int indexTop_Right = (int) map.get(ID_Top_Right);
                                                    int indexRight = (int) map.get(ID_Right);

                                                    int indexBottom_Left = (int) map.get(ID_Bottom_Left);
                                                    int indexBottom_Right = (int) map.get(ID_Bottom_Right);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexTop_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexTop_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexRight).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*3 ;
                                                        layoutParams.height = BoxHeight* 2;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  SaveUtil.AllCanUseBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i2x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空操作
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;
                                                        //删除完毕
                                                        mainView.addView(addImage);
                                                        //设置新增的数据
                                                        oneBoxItem.setIndex(1);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_6_X);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setBoxType(BoxTypes.Old_6_X);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setBoxType(BoxTypes.Old_6_X);

                                                        SaveUtil.AllCanUseBox.get(indexRight).setIndex(4);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setBoxType(BoxTypes.Old_6_X);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_6_X);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setIndex(5);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_6_X);

                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }
                                    }else if(TheBoxT.getBoxType().equals(BoxTypes.Old_A_New_9_XY)){
                                        List<Integer> list = new ArrayList<Integer>();

                                        //田子9,这个触摸点为最中间
                                        Map map = new HashMap();
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            map.put(SaveUtil.AllCanUseBox.get(i).getID(), i);
                                            if (SaveUtil.AllCanUseBox.get(i).getNowTime() == TheBoxT.getNowTime() && !SaveUtil.AllCanUseBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < SaveUtil.AllCanUseBox.size(); i++) {
                                            OneBoxItem oneBoxItem = SaveUtil.AllCanUseBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1> oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight
                                                    ) {
                                                //当前为第一排第二个
                                                //上部左侧
                                                String ID_Top_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );
                                                String ID_Top_Zhong =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                                                //上部的右侧
                                                String ID_Top_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);

//中间的一左一右
                                                String ID_Zhong_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );
                                                String ID_Zhong_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))))  + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))+1 );

                                                //下部第一个
                                                String ID_Bottom_Left =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length()))-1 );

                                                //下部正中
                                                String ID_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );

                                                //下部第二个
                                                String ID_Bottom_Right =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) +1);

                                                try {
                                                    int indexTop_Left = (int) map.get(ID_Top_Left);
                                                    int indexTop_Right = (int) map.get(ID_Top_Right);
                                                    int indexRight = (int) map.get(ID_Right);
                                                    //新增的
                                                    int indexTop_Zhong = (int) map.get(ID_Top_Zhong);
                                                    int indexZhong_Left = (int) map.get(ID_Zhong_Left);
                                                    int indexZhong_Right = (int) map.get(ID_Zhong_Right);

                                                    //
                                                    int indexBottom_Left = (int) map.get(ID_Bottom_Left);
                                                    int indexBottom_Right = (int) map.get(ID_Bottom_Right);
                                                    //判断左右两个是不是为空
                                                    if ((SaveUtil.AllCanUseBox.get(indexTop_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexTop_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexRight).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexTop_Zhong).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexTop_Zhong).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (SaveUtil.AllCanUseBox.get(indexZhong_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexZhong_Left).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexZhong_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexZhong_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Left).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime())&&
                                                            (SaveUtil.AllCanUseBox.get(indexBottom_Right).isHadUse() == false||SaveUtil.AllCanUseBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(getContext());
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*3 ;
                                                        layoutParams.height = BoxHeight*3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) SaveUtil.AllCanUseBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  SaveUtil.AllCanUseBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i3x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                            SaveUtil.AllCanUseBox.get(index_delete).setHadUse(false);
                                                        }
                                                        TheBoxT.setHadUse(false);
                                                        //删除这个复制的图片
                                                        mainView.removeView(TheBoxT.getImageView());
                                                        mainView.removeView(showImage_Right);
                                                        showImage_Right = null;



                                                        //设置新增的数据
                                                        oneBoxItem.setIndex(4);
                                                        mainView.addView(addImage);
                                                        oneBoxItem.setHadUse(true);
                                                        oneBoxItem.setNowTime(time);
                                                        oneBoxItem.setImageView(addImage);
                                                        oneBoxItem.setBoxType(BoxTypes.Old_A_New_9_XY);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Zhong).setIndex(1);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Zhong).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Zhong).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Zhong).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Zhong).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setIndex(3);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Left).setIndex(5);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexZhong_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setIndex(0);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_9_XY);
                                                        //
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setIndex(2);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        SaveUtil.AllCanUseBox.get(indexRight).setIndex(7);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setIndex(6);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setIndex(8);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setHadUse(true);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setNowTime(time);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setImageView(addImage);
                                                        SaveUtil.AllCanUseBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }
                                    }//这里写下一个
                                    if (canUse == 0) {
                                        mainView.removeView(showImage_Right);
                                        Toast.makeText(getContext(), "该位置无效,请重试", Toast.LENGTH_SHORT).show();
                                    }


                                }

                            }
                        }
                        needToMove = false;
                        TheBoxT = null;
                        break;
                }

                return true;
            }
        });
    }
    /**
     * 当从右侧往左侧拖动表箱时候的操作,根据表箱的类型,实现不同类型图片的显示
     *
     * @param x       触摸点的X
     * @param y       触摸点的Y
     * @param boxTYpe 这个表箱实例的类型
     * @return
     */
    private void withRightOnTuch(float x, float y, OneBoxItem boxTYpe) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) showImage_Right.getLayoutParams();
        showImage_Right.setX(x - layoutParams.width / 2);
        showImage_Right.setY(y - layoutParams.height / 2);

    }
}
