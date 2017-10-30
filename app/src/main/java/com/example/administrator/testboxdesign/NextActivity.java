package com.example.administrator.testboxdesign;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.testboxdesign.beans.OneBoxItem;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/16.
 */

public class NextActivity extends AppCompatActivity{
    //这个是分出来的表格,不是实际显示的
    List<OneBoxItem> allBox;
    @BindView(R.id.example_onebox)
    ImageView example;
    @BindView(R.id.box_one)
    ImageView touch1;
    //右侧的画图区域
    @BindView(R.id.drawview)
    PercentRelativeLayout drawView;
    @BindView(R.id.main)
    PercentRelativeLayout mainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        ButterKnife.bind(NextActivity.this);
    }
    //左侧实例的宽高
    int ExampleWidth;
    int ExampleHeight;

    int BoxWidth;
    int BoxHeight;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    boolean hadGet;//是否已经获取到尺寸了,获取到了之后就不需要

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hadGet == false) {
            //实际需要添加的大小
            BoxWidth = touch1.getRight() - touch1.getLeft();
            BoxHeight = touch1.getBottom() - touch1.getTop();
            //左侧例子的大小
            ExampleWidth = example.getRight() - example.getLeft();
            ExampleHeight = example.getBottom() - example.getTop();
            //对画布区域进行分格子处理
            //以区域的左上角的坐标原点
            //先获取右侧区域的坐标,
            float x = drawView.getLeft();
            float y = drawView.getTop();
            //再获取右侧区域的大小
            float width = drawView.getRight() - drawView.getLeft();
            float height = drawView.getBottom() - drawView.getTop();
            //对于这个区域,需要一个边沿的空白,这个区域的大小为左侧实例box的1/4宽,及examplewidth/4.实例的宽度为百分之五宽度,右侧绘图区域的大小是百分之8
            //算出在X,Y方向上所能容纳的个数
            double temp = ((width - ExampleWidth / 2) / BoxWidth);
            double temp1 = (height - ExampleHeight / 2) / BoxHeight;
            Log.e("横向上可以容纳的数量",temp+"");

            int X_canuse = splitDouble(temp);
            Log.e("横向上可以容纳的数量-实际",X_canuse+"");
            int Y_canuse = splitDouble(temp1);
//从左往右,横向的加
            //因为存在留白区域,所以对于实际显示区域的起始坐标,需要计算下
            double startX = x + (ExampleWidth / 4);
            double startY = y + (ExampleHeight / 4);
            for (int i = 0; i < Y_canuse; i++) {
                for (int j = 0; j < X_canuse; j++) {
                    //从第一行的第一个开始,向右走,然后第二行...
                    OneBoxItem oneBoxItem = new OneBoxItem();
                    //这里的j表示从左往右的第几行
                    oneBoxItem.setX(startX + BoxWidth * j);
                    //这里的i表示从上网下的第几列
                    oneBoxItem.setY(startY + BoxHeight * i);
                    oneBoxItem.setID(i + "s" + j);
                    if (allBox == null || allBox.size() == 0) {
                        allBox = new ArrayList<OneBoxItem>();
                    }

                    ImageView uu = new ImageView(NextActivity.this);
                    PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.width = BoxWidth ;
                    layoutParams.height = BoxHeight;
                    uu.setAlpha(0.1f);
                    uu.setLayoutParams(layoutParams);
                    uu.setX((float) oneBoxItem.getX());
                    uu.setY((float) oneBoxItem.getY());
                    uu.setBackground(getResources().getDrawable(R.drawable.i1x1));
                    mainView.addView(uu);

                    allBox.add(oneBoxItem);
                }
            }
            //设置完毕后置空
            hadGet = true;
            initView();
        } else {

        }
    }
    //开始初始化view
    //需要添加的view
    ImageView needAddImageview;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(){
//建立本页面可以绘制的表格的ID和下标的对应,用于根据已绘制的数据的ID获取当前页面的坐标,
        Map<String,Integer> map=new HashMap();
        for(int i=0;i<allBox.size();i++){
            OneBoxItem oneBoxItem1=allBox.get(i);
            map.put(oneBoxItem1.getID(),i);
        }
        if(SaveUtilS.allBox==null){
            Log.e("存储的数据为空","====");
        }
for(OneBoxItem oneBoxItem:SaveUtilS.allBox){
if(oneBoxItem.isHadUse()==true&&oneBoxItem.getIndex()==0){
    needAddImageview = new ImageView(NextActivity.this);
    needAddImageview.setAlpha(1.0f);
    PercentRelativeLayout.LayoutParams layoutParamsShow = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    //对不同的表箱类型进行判断
    if(oneBoxItem.getBoxType()==null){
        Log.e("这个表格的类型为空","===");
        return;
    }
    if (oneBoxItem.getBoxType().equals(BoxTypes.Old_1_X)) {
        layoutParamsShow.width = BoxWidth;
        layoutParamsShow.height = BoxHeight;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i1x1);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_2_X)) {
        //横向双表
        layoutParamsShow.width = BoxWidth * 2;
        layoutParamsShow.height = BoxHeight;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i1x2);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_2_Y)) {
        //纵向双表
        layoutParamsShow.width = BoxWidth;
        layoutParamsShow.height = BoxHeight * 2;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i2x1);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    } else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_3_X)) {
        //横向的3表
        layoutParamsShow.width = BoxWidth * 3;
        layoutParamsShow.height = BoxHeight;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i1x3);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if (oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_3_Y)) {
        //纵向的3表
        layoutParamsShow.width = BoxWidth ;
        layoutParamsShow.height = BoxHeight*3;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i3x1);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if(oneBoxItem.getBoxType().equals(BoxTypes.New_4_Y)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth ;
        layoutParamsShow.height = BoxHeight*4;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i4x1);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if(oneBoxItem.getBoxType().equals(BoxTypes.New_4_X)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth*4 ;
        layoutParamsShow.height = BoxHeight;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i1x4);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    } else if(oneBoxItem.getBoxType().equals(BoxTypes.New_4_XY)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth*2 ;
        layoutParamsShow.height = BoxHeight*2;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i2x2);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if(oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_6_Y)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth*2 ;
        layoutParamsShow.height = BoxHeight*3;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i3x2);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if(oneBoxItem.getBoxType().equals(BoxTypes.Old_6_X)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth*3 ;
        layoutParamsShow.height = BoxHeight*2;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i2x3);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }else if(oneBoxItem.getBoxType().equals(BoxTypes.Old_A_New_9_XY)){
        //纵行的四表
        layoutParamsShow.width = BoxWidth*3 ;
        layoutParamsShow.height = BoxHeight*3;
        needAddImageview.setLayoutParams(layoutParamsShow);
        needAddImageview.setImageResource(R.drawable.i3x3);
        needAddImageview.setBackground(getResources().getDrawable(R.drawable.shape_rect));
    }
    needAddImageview.setX((float) allBox.get(map.get(oneBoxItem.getID())).getX());
    needAddImageview.setY((float) allBox.get(map.get(oneBoxItem.getID())).getY());
    mainView.addView(needAddImageview);
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

}
