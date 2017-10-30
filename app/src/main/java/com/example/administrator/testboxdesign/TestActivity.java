package com.example.administrator.testboxdesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.example.administrator.testboxdesign.beans.OneBoxItem;
import com.example.administrator.testboxdesign.designbox_utils.SaveUtil;
import com.example.administrator.testboxdesign.designbox_view.BoxChooseView;
import com.example.administrator.testboxdesign.designbox_view.BoxDesignView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/27.
 */

/**
 * 存储数据的类，无效了
 */
public class TestActivity extends Activity{
    @OnClick(R.id.next)
    void next(){
        SaveUtilS.allBox=new ArrayList<OneBoxItem>();
        SaveUtilS.allBox.addAll(SaveUtil.AllCanUseBox);
        SaveUtilS.Temp="123";
        startActivity(new Intent(TestActivity.this,NextActivity.class));
    }

    @BindView(R.id.example_onebox)
    ImageView example;
    @BindView(R.id.box_one)
    ImageView touch1;
    //右侧的绘制区域
    @BindView(R.id.drawview)
    BoxDesignView drawView;
    //左侧的选取区域
    @BindView(R.id.boxchooseview)
    BoxChooseView boxChooseView;
    @BindView(R.id.mainview)
            PercentRelativeLayout mainView;
    //左侧实例的宽高
    int ExampleWidth;
    int ExampleHeight;
//实际拖拽过去时候的格子的宽高
    int BoxWidth;
    int BoxHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_layout);
        ButterKnife.bind(TestActivity.this);
    }
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
            //设置完毕后置空
            hadGet = true;
            //给右侧画格子
            drawView.spiltBox(ExampleWidth,ExampleHeight,BoxWidth,BoxHeight,mainView);

            //给左侧设置数据
            boxChooseView.setValues(ExampleWidth,ExampleHeight,BoxWidth,BoxHeight,mainView,drawView);
        } else {

        }
    }

}
