package com.example.administrator.testboxdesign;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.testboxdesign.beans.OneBoxItem;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //当前右侧绘图区域的所有box
    List<OneBoxItem> allBox;
    @BindView(R.id.i1)
    ImageView i1;
    @BindView(R.id.i2)
    ImageView i2;
    @BindView(R.id.i3)
    ImageView i3;
    @BindView(R.id.i4)
    ImageView i4;
    @BindView(R.id.i5)
    ImageView i5;
    @BindView(R.id.i6)
    ImageView i6;
    @BindView(R.id.i7)
    ImageView i7;
    @BindView(R.id.i8)
    ImageView i8;
    @BindView(R.id.i9)
    ImageView i9;
    @BindView(R.id.i10)
    ImageView i10;
    @BindView(R.id.i11)
    ImageView i11;

    @BindView(R.id.example_onebox)
    ImageView example;
    @BindView(R.id.box_one)
    ImageView touch1;

    //右侧的画图区域
    @BindView(R.id.drawview)
    PercentRelativeLayout drawView;
    @BindView(R.id.main)
    PercentRelativeLayout mainView;
    @OnClick(R.id.next)
    void next(){
        SaveUtilS.allBox=new ArrayList<OneBoxItem>();
        SaveUtilS.allBox.addAll(allBox);
SaveUtilS.Temp="123";
        startActivity(new Intent(MainActivity.this,NextActivity.class));
    }

    /*不管是左侧实例的拖拽还是右侧实际表箱的拖拽,这个拖拽所操作的虚拟的view 都是新增的;down的时候添加,up的时候删除这个虚拟的
    *
    *对于跳转到下一页,需要保存本页的设置:每个表箱 给予他左上角在坐标系中的坐标;单个表箱的每个格子,给予他所在这个表箱的编号,这个编号统一是自左往右,自上往下数..对于同一个表箱 的不同格子,他的nowTime数值是相同的.
    * 所以在遍历时候拿到type,再找出time相同的,再找出是第一个的,就可以了.
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        initView();

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

                        ImageView uu = new ImageView(MainActivity.this);
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
        } else {

        }
    }

    /**
     * 这个是左侧区域的拖拽处理===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧===左侧
     */
    //这个imageview是在拖拽的时候显示的图片,设置完毕这个就删除了
    ImageView showImage_Left;

    private void setTouchEvent_Left(View imageview_left, final String boxType) {
        imageview_left.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showImage_Left = new ImageView(MainActivity.this);
                        showImage_Left.setAlpha(0.3f);
                        PercentRelativeLayout.LayoutParams layoutParamsShow = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        //对不同的表箱类型进行判断
                        if (boxType.equals(BoxTypes.Old_1_X)) {
                            layoutParamsShow.width = ExampleWidth;
                            layoutParamsShow.height = ExampleHeight;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i1x1));
                        } else if (boxType.equals(BoxTypes.Old_A_New_2_X)) {
                            //横向双表
                            layoutParamsShow.width = ExampleWidth * 2;
                            layoutParamsShow.height = ExampleHeight;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i1x2));
                        } else if (boxType.equals(BoxTypes.Old_2_Y)) {
                            //纵向双表
                            layoutParamsShow.width = ExampleWidth;
                            layoutParamsShow.height = ExampleHeight * 2;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i2x1));
                        } else if (boxType.equals(BoxTypes.Old_A_New_3_X)) {
                            //横向的3表
                            layoutParamsShow.width = ExampleWidth * 3;
                            layoutParamsShow.height = ExampleHeight;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i1x3));
                        }else if (boxType.equals(BoxTypes.Old_A_New_3_Y)) {
                            //纵向的3表
                            layoutParamsShow.width = ExampleWidth ;
                            layoutParamsShow.height = ExampleHeight*3;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i3x1));
                        }else if(boxType.equals(BoxTypes.New_4_Y)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth ;
                            layoutParamsShow.height = ExampleHeight*4;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i4x1));
                        }else if(boxType.equals(BoxTypes.New_4_X)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth*4 ;
                            layoutParamsShow.height = ExampleHeight;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i1x4));
                        } else if(boxType.equals(BoxTypes.New_4_XY)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth*2 ;
                            layoutParamsShow.height = ExampleHeight*2;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i2x2));
                        }else if(boxType.equals(BoxTypes.Old_A_New_6_Y)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth*2 ;
                            layoutParamsShow.height = ExampleHeight*3;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i3x2));
                        }else if(boxType.equals(BoxTypes.Old_6_X)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth*3 ;
                            layoutParamsShow.height = ExampleHeight*2;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i2x3));
                        }else if(boxType.equals(BoxTypes.Old_A_New_9_XY)){
                            //纵行的四表
                            layoutParamsShow.width = ExampleWidth*3 ;
                            layoutParamsShow.height = ExampleHeight*3;
                            showImage_Left.setLayoutParams(layoutParamsShow);
                            showImage_Left.setBackground(getResources().getDrawable(R.drawable.i3x3));
                        }
                        showImage_Left.setX(event.getRawX() - layoutParamsShow.width / 2);
                        showImage_Left.setY(event.getRawY() - layoutParamsShow.height / 2);
                        mainView.addView(showImage_Left);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //让这个屏幕上左侧例子的另一个副本进行位置变换
                        //对不同的表箱类型进行判断
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) showImage_Left.getLayoutParams();
                        showImage_Left.setX((float) (event.getRawX() - layoutParams.width / 2));
                        showImage_Left.setY((float) (event.getRawY() - layoutParams.height / 2));

                        break;
                    case MotionEvent.ACTION_UP:
                        //手指抬起后,对这个进行添加表箱的处理
                        addBoxToDrawview(event.getRawX(), event.getRawY(), showImage_Left, boxType);
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        //单表箱类型的表的实例--左侧
        example.setVisibility(View.INVISIBLE);
        //单表箱类型的表的实例--右侧
        touch1.setVisibility(View.INVISIBLE);
        setTouchEvent_Left(i1, BoxTypes.Old_1_X);
        setTouchEvent_Left(i2, BoxTypes.Old_A_New_2_X);
        setTouchEvent_Left(i3, BoxTypes.Old_2_Y);
        setTouchEvent_Left(i4, BoxTypes.Old_A_New_3_X);
        setTouchEvent_Left(i5, BoxTypes.Old_A_New_3_Y);
        setTouchEvent_Left(i6, BoxTypes.New_4_Y);
        setTouchEvent_Left(i7, BoxTypes.New_4_X);
        setTouchEvent_Left(i8, BoxTypes.New_4_XY);
        setTouchEvent_Left(i9, BoxTypes.Old_A_New_6_Y);
        setTouchEvent_Left(i10, BoxTypes.Old_6_X);
        setTouchEvent_Left(i11, BoxTypes.Old_A_New_9_XY);
        setTouchEvent_Right();
    }

    static int OLE_ONE = 1;


//根据touch事件中传递的X,Y,来判断这个图是应该放在哪,所有类型表公用这一个方法

    /**
     * @param x          点击事件X轴的坐标
     * @param y          点击事件Y的坐标
     * @param touchImage 点击这个表箱所触发的图片
     */
    int anInt;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addBoxToDrawview(double x, double y, ImageView touchImage, String boxType) {
        //可以放置表箱的区域
        double startX = drawView.getLeft() + ExampleWidth / 2;
        double startY = drawView.getTop() + ExampleHeight / 2;
        double endX = drawView.getRight() - ExampleWidth / 2;
        double endY = drawView.getBottom() - ExampleHeight / 2;
        //先判断这个有没有到画表箱的区域
        if (x < startX || x > endX || y < startY || y > endY) {
            mainView.removeView(touchImage);
            showImage_Left = null;
            return;
        }
        //对于这个表箱类型需要做判断
        //表示右侧是否有可以容纳的
        int userCount = 0;
        if (boxType.equals(BoxTypes.Old_1_X)) {
            for (OneBoxItem oneBoxItem : allBox) {
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    userCount++;
                    //实例出来一个新的表箱,用来添加到右侧页面上
                    ImageView addImage = new ImageView(MainActivity.this);
                    PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layoutParams.width = BoxWidth;
                    layoutParams.height = BoxHeight;
                    addImage.setLayoutParams(layoutParams);
                    addImage.setX((float) oneBoxItem.getX());
                    addImage.setY((float) oneBoxItem.getY());
                    addImage.setImageResource(R.drawable.i1x1);
                    addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                    Log.e("新增了一个view", "===");
                    mainView.addView(addImage);
                    //由于nowtime字段是为了区分几个type相同的格子是否为同一个类型的表箱拖出去,这个是单表箱 就不存在
                    oneBoxItem.setIndex(0);
                    oneBoxItem.setHadUse(true);
                    oneBoxItem.setImageView(addImage);
                    oneBoxItem.setBoxType(BoxTypes.Old_1_X);
                    Log.e("设置的类型",oneBoxItem.getBoxType()+"="+allBox.get(0).getBoxType());
                    //删除这个复制的图片
                    mainView.removeView(touchImage);
                    ;
                    showImage_Left = null;
                }
            }
        } else if (boxType.equals(BoxTypes.Old_A_New_2_X)) {
            //先拿到所有表格的下标和ID之间的对应关系
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }

            //把这个点当做是两个表箱左侧的这个,如果这个格子右边还有空余的,就放置,没有就取消
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //横向两个表箱的判断,假设这个是两个表箱左侧的这个,呢么另一个的ID应该是X+1sY
                    //获得这个表格右侧的ID
                    Log.e("两横表箱 左侧的id", oneBoxItem.getID());
                    //1s1
                    String ID_Right = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) + 1);
                    Log.e("两横表箱 右侧的id", ID_Right);
                    try {
                        int index = (int) map.get(ID_Right);
                        anInt = index;

                        if (allBox.get(index).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth * 2;
                            layoutParams.height = BoxHeight;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) oneBoxItem.getX());
                            addImage.setY((float) oneBoxItem.getY());
                            addImage.setImageResource(R.drawable.i1x2);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            Log.e("新增了一个二表箱", "===");
                            mainView.addView(addImage);
                            //左侧这个的数据设置
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setIndex(0);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_A_New_2_X);
                            //右侧这个数据的设置
                            allBox.get(index).setHadUse(true);
                            allBox.get(index).setIndex(1);
                            allBox.get(index).setNowTime(time);
                            allBox.get(index).setImageView(addImage);
                            allBox.get(index).setBoxType(BoxTypes.Old_A_New_2_X);

                            Log.e("右侧这个表箱现在的状态是什么", allBox.get(index).isHadUse() + "==" + allBox.get(index).getBoxType() + "==" + allBox.get(index).getX() + "=" + BoxWidth);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }
        } else if (boxType.equals(BoxTypes.Old_2_Y)) {
//拖动过来的是纵向二表的表箱,还是遍历所有表格,遇到一个空的,就去查询他下面的那个表格是否被使用.默认这个拖拽点是上面的那个表箱
            //先拿到所有表格的下标和ID之间的对应关系
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //纵向两个表箱的判断,假设这个是两个表箱上部的这个,呢么另一个的ID应该是XsY+1,对应ID中的数据AsB为 A+1,B

                    //1s1
                    String ID_Right = String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))) + 1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())));
                    try {
                        int index = (int) map.get(ID_Right);
                        anInt = index;

                        if (allBox.get(index).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth;
                            layoutParams.height = BoxHeight * 2;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) oneBoxItem.getX());
                            addImage.setY((float) oneBoxItem.getY());
                            addImage.setImageResource(R.drawable.i2x1);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //上部这个的数据设置
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setIndex(0);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_2_Y);
                            //下部这个数据的设置
                            allBox.get(index).setHadUse(true);
                            allBox.get(index).setIndex(1);
                            allBox.get(index).setNowTime(time);
                            allBox.get(index).setImageView(addImage);
                            allBox.get(index).setBoxType(BoxTypes.Old_2_Y);

                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }

        } else if (boxType.equals(BoxTypes.Old_A_New_3_X)) {
            //如果是横向的3,这里和2表箱的区别是,假设点击点的坐标是中间那个的坐标
            //先拿到所有表格的下标和ID之间的对应关系
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //纵向两个表箱的判断,假设这个是两个表箱上部的这个,呢么另一个的ID应该是XsY+1,对应ID中的数据AsB为 A+1,B

                    //右侧的这个
                    String ID_Right = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) + 1);
                    //左侧的这个
                    String ID_Left = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) - 1);
                    try {
                        int indexRight = (int) map.get(ID_Right);
                        int indexLeft = (int) map.get(ID_Left);
                        //判断左右两个是不是为空
                        if (allBox.get(indexRight).isHadUse() == false && allBox.get(indexLeft).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth * 3;
                            layoutParams.height = BoxHeight;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexLeft).getX());
                            addImage.setY((float) allBox.get(indexLeft).getY());
                            addImage.setImageResource(R.drawable.i1x3);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //中间这个的数据设置
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setIndex(1);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_A_New_3_X);
                            //右侧这个数据的设置
                            allBox.get(indexRight).setIndex(2);
                            allBox.get(indexRight).setHadUse(true);
                            allBox.get(indexRight).setNowTime(time);
                            allBox.get(indexRight).setImageView(addImage);
                            allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_3_X);
                            //左侧这个
                            allBox.get(indexLeft).setIndex(0);
                            allBox.get(indexLeft).setHadUse(true);
                            allBox.get(indexLeft).setNowTime(time);
                            allBox.get(indexLeft).setImageView(addImage);
                            allBox.get(indexLeft).setBoxType(BoxTypes.Old_A_New_3_X);

                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }

        }else if(boxType.equals(BoxTypes.Old_A_New_3_Y)){
            //纵3的添加,以触摸点为中间那个
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //点击点为中间
                    //上侧的这个
                    String ID_Top =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                    //下侧的这个
                    String ID_Bottom =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                    try {
                        int indexTop = (int) map.get(ID_Top);
                        int indexBottom = (int) map.get(ID_Bottom);
                        //判断左右两个是不是为空
                        if (allBox.get(indexTop).isHadUse() == false && allBox.get(indexBottom).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth ;
                            layoutParams.height = BoxHeight* 3;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexTop).getX());
                            addImage.setY((float) allBox.get(indexTop).getY());
                            addImage.setImageResource(R.drawable.i3x1);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //左侧这个的数据设置
                            oneBoxItem.setIndex(1);
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_A_New_3_Y);
                            //上侧这个数据的设置
                            allBox.get(indexTop).setIndex(0);
                            allBox.get(indexTop).setHadUse(true);
                            allBox.get(indexTop).setNowTime(time);
                            allBox.get(indexTop).setImageView(addImage);
                            allBox.get(indexTop).setBoxType(BoxTypes.Old_A_New_3_Y);
                            //左侧这个
                            allBox.get(indexBottom).setIndex(2);
                            allBox.get(indexBottom).setHadUse(true);
                            allBox.get(indexBottom).setNowTime(time);
                            allBox.get(indexBottom).setImageView(addImage);
                            allBox.get(indexBottom).setBoxType(BoxTypes.Old_A_New_3_Y);

                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }

        }else if(boxType.equals(BoxTypes.New_4_Y)){
            //纵4,以从上开始数的第二个作为这个点击的
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //纵向两个表箱的判断,假设这个是两个表箱上部的这个,呢么另一个的ID应该是XsY+1,对应ID中的数据AsB为 A+1,B
                    //上部的这个
                    String ID_Top =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))-1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                    //下部第一个
                    String ID_Bottom =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+1)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );
                   //下部第二个
                    String ID_Bottom_2 =String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")))+2)   + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) );

                    try {
                        int indexTop = (int) map.get(ID_Top);
                        int indexBottom = (int) map.get(ID_Bottom);
                        int indexBottom_2 = (int) map.get(ID_Bottom_2);
                        //判断左右两个是不是为空
                        if (allBox.get(indexTop).isHadUse() == false && allBox.get(indexBottom).isHadUse() == false&& allBox.get(indexBottom_2).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth ;
                            layoutParams.height = BoxHeight* 4;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexTop).getX());
                            addImage.setY((float) allBox.get(indexTop).getY());
                            addImage.setImageResource(R.drawable.i4x1);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setIndex(1);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.New_4_Y);
                            //
                            allBox.get(indexTop).setIndex(0);
                            allBox.get(indexTop).setHadUse(true);
                            allBox.get(indexTop).setNowTime(time);
                            allBox.get(indexTop).setImageView(addImage);
                            allBox.get(indexTop).setBoxType(BoxTypes.New_4_Y);
                            //
                            allBox.get(indexBottom).setIndex(2);
                            allBox.get(indexBottom).setHadUse(true);
                            allBox.get(indexBottom).setNowTime(time);
                            allBox.get(indexBottom).setImageView(addImage);
                            allBox.get(indexBottom).setBoxType(BoxTypes.New_4_Y);

                            allBox.get(indexBottom_2).setIndex(3);
                            allBox.get(indexBottom_2).setHadUse(true);
                            allBox.get(indexBottom_2).setNowTime(time);
                            allBox.get(indexBottom_2).setImageView(addImage);
                            allBox.get(indexBottom_2).setBoxType(BoxTypes.New_4_Y);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }

        }else if(boxType.equals(BoxTypes.New_4_X)){
            //横4,以从上开始数的第二个作为这个点击的
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //点击点作为第一行第二个
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
                        if (allBox.get(index_Left).isHadUse() == false && allBox.get(index_Right).isHadUse() == false&& allBox.get(index_Right_2).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth* 4 ;
                            layoutParams.height = BoxHeight;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(index_Left).getX());
                            addImage.setY((float) allBox.get(index_Left).getY());
                            addImage.setImageResource(R.drawable.i1x4);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setIndex(1);
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.New_4_X);
                            //
                            allBox.get(index_Left).setIndex(0);
                            allBox.get(index_Left).setHadUse(true);
                            allBox.get(index_Left).setNowTime(time);
                            allBox.get(index_Left).setImageView(addImage);
                            allBox.get(index_Left).setBoxType(BoxTypes.New_4_X);
                            //
                            allBox.get(index_Right).setIndex(2);
                            allBox.get(index_Right).setHadUse(true);
                            allBox.get(index_Right).setNowTime(time);
                            allBox.get(index_Right).setImageView(addImage);
                            allBox.get(index_Right).setBoxType(BoxTypes.New_4_X);

                            allBox.get(index_Right_2).setIndex(3);
                            allBox.get(index_Right_2).setHadUse(true);
                            allBox.get(index_Right_2).setNowTime(time);
                            allBox.get(index_Right_2).setImageView(addImage);
                            allBox.get(index_Right_2).setBoxType(BoxTypes.New_4_X);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }

        }else if(boxType.equals(BoxTypes.New_4_XY)){
            //田子4,以从上开始数的第一个作为这个点击的,及左上角
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //点击点为左上角
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
                        if (allBox.get(indexTop_Right).isHadUse() == false && allBox.get(indexBottom_Left).isHadUse() == false&& allBox.get(indexBottom_Right).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth*2 ;
                            layoutParams.height = BoxHeight* 2;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) oneBoxItem.getX());
                            addImage.setY((float) oneBoxItem.getY());
                            addImage.setImageResource(R.drawable.i2x2);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setIndex(0);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.New_4_XY);
                            //
                            allBox.get(indexTop_Right).setIndex(1);
                            allBox.get(indexTop_Right).setHadUse(true);
                            allBox.get(indexTop_Right).setNowTime(time);
                            allBox.get(indexTop_Right).setImageView(addImage);
                            allBox.get(indexTop_Right).setBoxType(BoxTypes.New_4_XY);
                            //
                            allBox.get(indexBottom_Left).setIndex(2);
                            allBox.get(indexBottom_Left).setHadUse(true);
                            allBox.get(indexBottom_Left).setNowTime(time);
                            allBox.get(indexBottom_Left).setImageView(addImage);
                            allBox.get(indexBottom_Left).setBoxType(BoxTypes.New_4_XY);

                            allBox.get(indexBottom_Right).setIndex(3);
                            allBox.get(indexBottom_Right).setHadUse(true);
                            allBox.get(indexBottom_Right).setNowTime(time);
                            allBox.get(indexBottom_Right).setImageView(addImage);
                            allBox.get(indexBottom_Right).setBoxType(BoxTypes.New_4_XY);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }
        }else if(boxType.equals(BoxTypes.Old_A_New_6_Y)){
            //田子6,纵向的,这个触摸点为左侧第二排的格子
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
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
                        if (allBox.get(indexTop_Left).isHadUse() == false && allBox.get(indexTop_Right).isHadUse() == false&& allBox.get(indexRight).isHadUse() == false&&allBox.get(indexBottom_Left).isHadUse() == false&&allBox.get(indexBottom_Right).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth*2 ;
                            layoutParams.height = BoxHeight* 3;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexTop_Left).getX());
                            addImage.setY((float)  allBox.get(indexTop_Left).getY());
                            addImage.setImageResource(R.drawable.i3x2);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setIndex(2);
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_A_New_6_Y);
                            //
                            allBox.get(indexTop_Left).setIndex(0);
                            allBox.get(indexTop_Left).setHadUse(true);
                            allBox.get(indexTop_Left).setNowTime(time);
                            allBox.get(indexTop_Left).setImageView(addImage);
                            allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_6_Y);
                            //
                            allBox.get(indexTop_Right).setIndex(1);
                            allBox.get(indexTop_Right).setHadUse(true);
                            allBox.get(indexTop_Right).setNowTime(time);
                            allBox.get(indexTop_Right).setImageView(addImage);
                            allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_6_Y);

                            allBox.get(indexRight).setIndex(3);
                            allBox.get(indexRight).setHadUse(true);
                            allBox.get(indexRight).setNowTime(time);
                            allBox.get(indexRight).setImageView(addImage);
                            allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_6_Y);

                            allBox.get(indexBottom_Left).setIndex(4);
                            allBox.get(indexBottom_Left).setHadUse(true);
                            allBox.get(indexBottom_Left).setNowTime(time);
                            allBox.get(indexBottom_Left).setImageView(addImage);
                            allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_6_Y);

                            allBox.get(indexBottom_Right).setIndex(5);
                            allBox.get(indexBottom_Right).setHadUse(true);
                            allBox.get(indexBottom_Right).setNowTime(time);
                            allBox.get(indexBottom_Right).setImageView(addImage);
                            allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_6_Y);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }
        }else if(boxType.equals(BoxTypes.Old_6_X)){
            //田子6,横向的,这个触摸点为第一排第二个的格子
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
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
                        if (allBox.get(indexTop_Left).isHadUse() == false && allBox.get(indexTop_Right).isHadUse() == false&& allBox.get(indexRight).isHadUse() == false&&allBox.get(indexBottom_Left).isHadUse() == false&&allBox.get(indexBottom_Right).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth*3 ;
                            layoutParams.height = BoxHeight* 2;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexTop_Left).getX());
                            addImage.setY((float)  allBox.get(indexTop_Left).getY());
                            addImage.setImageResource(R.drawable.i2x3);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setIndex(1);
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_6_X);
                            //
                            allBox.get(indexTop_Left).setIndex(0);
                            allBox.get(indexTop_Left).setHadUse(true);
                            allBox.get(indexTop_Left).setNowTime(time);
                            allBox.get(indexTop_Left).setImageView(addImage);
                            allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_6_X);
                            //
                            allBox.get(indexTop_Right).setIndex(2);
                            allBox.get(indexTop_Right).setHadUse(true);
                            allBox.get(indexTop_Right).setNowTime(time);
                            allBox.get(indexTop_Right).setImageView(addImage);
                            allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_6_X);

                            allBox.get(indexRight).setIndex(3);
                            allBox.get(indexRight).setHadUse(true);
                            allBox.get(indexRight).setNowTime(time);
                            allBox.get(indexRight).setImageView(addImage);
                            allBox.get(indexRight).setBoxType(BoxTypes.Old_6_X);

                            allBox.get(indexBottom_Left).setIndex(4);
                            allBox.get(indexBottom_Left).setHadUse(true);
                            allBox.get(indexBottom_Left).setNowTime(time);
                            allBox.get(indexBottom_Left).setImageView(addImage);
                            allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_6_X);

                            allBox.get(indexBottom_Right).setIndex(5);
                            allBox.get(indexBottom_Right).setHadUse(true);
                            allBox.get(indexBottom_Right).setNowTime(time);
                            allBox.get(indexBottom_Right).setImageView(addImage);
                            allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_6_X);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }
        }else if(boxType.equals(BoxTypes.Old_A_New_9_XY)){
            //田子9,这个触摸点为最中间
            Map map = new HashMap();
            for (int i = 0; i < allBox.size(); i++) {
                map.put(allBox.get(i).getID(), i);
            }
            for (int i = 0; i < allBox.size(); i++) {
                OneBoxItem oneBoxItem = allBox.get(i);
                if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                        && oneBoxItem.isHadUse() == false) {
                    //当前为第二排第二个
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
                        if (allBox.get(indexTop_Left).isHadUse() == false &&
                                allBox.get(indexTop_Right).isHadUse() == false&&
                                allBox.get(indexRight).isHadUse() == false&&
                                allBox.get(indexBottom_Left).isHadUse() == false&&
                                allBox.get(indexBottom_Right).isHadUse() == false&&
                                allBox.get(indexTop_Zhong).isHadUse() == false&&
                                allBox.get(indexZhong_Left).isHadUse() == false&&
                                allBox.get(indexZhong_Right).isHadUse() == false) {
                            double time = System.currentTimeMillis();
                            userCount++;
                            //实例出来一个新的表箱,用来添加到右侧页面上
                            ImageView addImage = new ImageView(MainActivity.this);
                            PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.width = BoxWidth*3 ;
                            layoutParams.height = BoxHeight*3;
                            addImage.setLayoutParams(layoutParams);
                            addImage.setX((float) allBox.get(indexTop_Left).getX());
                            addImage.setY((float)  allBox.get(indexTop_Left).getY());
                            addImage.setImageResource(R.drawable.i3x3);
                            addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                            mainView.addView(addImage);
                            //设置新增的数据
                            oneBoxItem.setIndex(4);
                            oneBoxItem.setHadUse(true);
                            oneBoxItem.setNowTime(time);
                            oneBoxItem.setImageView(addImage);
                            oneBoxItem.setBoxType(BoxTypes.Old_A_New_9_XY);
                            //
                            allBox.get(indexTop_Zhong).setIndex(1);
                            allBox.get(indexTop_Zhong).setHadUse(true);
                            allBox.get(indexTop_Zhong).setNowTime(time);
                            allBox.get(indexTop_Zhong).setImageView(addImage);
                            allBox.get(indexTop_Zhong).setBoxType(BoxTypes.Old_A_New_9_XY);

                            //
                            allBox.get(indexZhong_Left).setIndex(3);
                            allBox.get(indexZhong_Left).setHadUse(true);
                            allBox.get(indexZhong_Left).setNowTime(time);
                            allBox.get(indexZhong_Left).setImageView(addImage);
                            allBox.get(indexZhong_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                            //
                            allBox.get(indexZhong_Right).setIndex(5);
                            allBox.get(indexZhong_Right).setHadUse(true);
                            allBox.get(indexZhong_Right).setNowTime(time);
                            allBox.get(indexZhong_Right).setImageView(addImage);
                            allBox.get(indexZhong_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                            //
                            allBox.get(indexTop_Left).setIndex(0);
                            allBox.get(indexTop_Left).setHadUse(true);
                            allBox.get(indexTop_Left).setNowTime(time);
                            allBox.get(indexTop_Left).setImageView(addImage);
                            allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_9_XY);
                            //
                            allBox.get(indexTop_Right).setIndex(2);
                            allBox.get(indexTop_Right).setHadUse(true);
                            allBox.get(indexTop_Right).setNowTime(time);
                            allBox.get(indexTop_Right).setImageView(addImage);
                            allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_9_XY);
//底部正中
                            allBox.get(indexRight).setIndex(7);
                            allBox.get(indexRight).setHadUse(true);
                            allBox.get(indexRight).setNowTime(time);
                            allBox.get(indexRight).setImageView(addImage);
                            allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_9_XY);

                            allBox.get(indexZhong_Right).setIndex(6);
                            allBox.get(indexBottom_Left).setHadUse(true);
                            allBox.get(indexBottom_Left).setNowTime(time);
                            allBox.get(indexBottom_Left).setImageView(addImage);
                            allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                            allBox.get(indexBottom_Right).setIndex(8);
                            allBox.get(indexBottom_Right).setHadUse(true);
                            allBox.get(indexBottom_Right).setNowTime(time);
                            allBox.get(indexBottom_Right).setImageView(addImage);
                            allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_9_XY);
                            //删除这个复制的图片
                            mainView.removeView(touchImage);
                            ;
                            showImage_Left = null;
                        }
                    } catch (Exception e) {
                        Log.e("这个不合适二横表箱", "===");
                    }
                }
            }
        }
        //类型判断完毕
        if (userCount > 0) {

        } else {
            Toast.makeText(MainActivity.this, "位置错误", Toast.LENGTH_LONG).show();
            mainView.removeView(touchImage);
            ;
            showImage_Left = null;
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

    /*
    这个是右侧绘图区域的点击和拖拽处理===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧===右侧
     */
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
                        Log.e("右侧点击", event.getRawX() + "第一个的坐标" + allBox.get(0).getX() + "==" + allBox.get(1).getX());
                        //注意,这里也需要对不同的表箱类型进行相应的判断
                        Log.e("点击的位置", event.getRawX() + "==" + allBox.get(anInt).getBoxType() + "==" + allBox.get(anInt).getX());
                        float x = event.getRawX();
                        float y = event.getRawY();

                        for (int i = 0; i < allBox.size(); i++) {
                            OneBoxItem oneBoxItem = allBox.get(i);
                            if (x > oneBoxItem.getX() && x < oneBoxItem.getX() + BoxWidth && y > oneBoxItem.getY() && y < oneBoxItem.getY() + BoxHeight
                                    && oneBoxItem.isHadUse() == true) {
                                Log.e("这个地方有表箱", "====");
                                //表示这个位置有表箱,呢就显示新增个虚拟的表箱吧
                                showImage_Right = new ImageView(MainActivity.this);
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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                needDelte.add(i);
                                            }
                                        }
                                        if (needDelte.size() < 1) {
                                            Toast.makeText(MainActivity.this, "删除失败,请重试", Toast.LENGTH_SHORT).show();
                                        } else {
                                            for (int del : needDelte) {
                                                allBox.get(del).setHadUse(false);
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
                                        for (OneBoxItem oneBoxItem : allBox) {
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight
                                                    && oneBoxItem.isHadUse() == false) {
                                                //表示放置的位置是个空白
                                                //实例出来一个新的表箱,用来添加到右侧页面上
                                                ImageView addImage = new ImageView(MainActivity.this);
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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                otherTableIndex = i;
                                            }
                                        }
                                        //把这个点当做是两个表箱左侧的这个,如果这个格子右边还有空余的,就放置,没有就取消
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //如果他在这个区域内部
                                                //算出需要的两个格子的位置.一个就是现在的onBoxItem,另一个就需要计算了
                                                String ID_Right = oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s")) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())) + 1);
                                                try {
                                                    int index = (int) map.get(ID_Right);
                                                    if ((oneBoxItem.getNowTime() == TheBoxT.getNowTime() || oneBoxItem.isHadUse() == false) && (allBox.get(index).getNowTime() == TheBoxT.getNowTime() || allBox.get(index).isHadUse() == false)) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
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
                                                        allBox.get(otherTableIndex).setHadUse(false);
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
                                                        allBox.get(index).setIndex(1);
                                                        allBox.get(index).setHadUse(true);
                                                        allBox.get(index).setNowTime(time);
                                                        allBox.get(index).setImageView(addImage);
                                                        allBox.get(index).setBoxType(BoxTypes.Old_A_New_2_X);
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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                otherTableIndex = i;
                                            }
                                        }
                                        //把这个点当做是两个表箱上侧的这个,如果这个格子右边还有空余的,就放置,没有就取消
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
                                            //只需要判断相关联的box是false或者为相同的就可以了
                                            if (x1 > oneBoxItem.getX() && x1 < oneBoxItem.getX() + BoxWidth && y1 > oneBoxItem.getY() && y1 < oneBoxItem.getY() + BoxHeight) {
                                                //这个是纵向2的底部
                                                String ID_Right = String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(0, oneBoxItem.getID().indexOf("s"))) + 1) + "s" + String.valueOf(Integer.parseInt(oneBoxItem.getID().substring(oneBoxItem.getID().indexOf("s") + 1, oneBoxItem.getID().length())));
                                                try {
                                                    int index = (int) map.get(ID_Right);
                                                    if ((oneBoxItem.getNowTime() == TheBoxT.getNowTime() || oneBoxItem.isHadUse() == false) && (allBox.get(index).getNowTime() == TheBoxT.getNowTime() || allBox.get(index).isHadUse() == false)) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
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
                                                        allBox.get(otherTableIndex).setHadUse(false);
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
                                                        allBox.get(index).setHadUse(true);
                                                        allBox.get(index).setNowTime(time);
                                                        allBox.get(index).setIndex(1);
                                                        allBox.get(index).setImageView(addImage);
                                                        allBox.get(index).setBoxType(BoxTypes.Old_2_Y);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexRight).isHadUse() == false||allBox.get(indexRight).getNowTime()==TheBoxT.getNowTime()) && (allBox.get(indexLeft).isHadUse() == false||allBox.get(indexLeft).getNowTime()==TheBoxT.getNowTime())
                                                    &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth * 3;
                                                        layoutParams.height = BoxHeight;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexLeft).getX());
                                                        addImage.setY((float) allBox.get(indexLeft).getY());
                                                        addImage.setImageResource(R.drawable.i1x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexRight).setIndex(2);
                                                        allBox.get(indexRight).setHadUse(true);
                                                        allBox.get(indexRight).setNowTime(time);
                                                        allBox.get(indexRight).setImageView(addImage);
                                                        allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_3_X);
                                                        //左侧这个
                                                        allBox.get(indexLeft).setIndex(0);
                                                        allBox.get(indexLeft).setHadUse(true);
                                                        allBox.get(indexLeft).setNowTime(time);
                                                        allBox.get(indexLeft).setImageView(addImage);
                                                        allBox.get(indexLeft).setBoxType(BoxTypes.Old_A_New_3_X);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexTop).isHadUse() == false||allBox.get(indexTop).getNowTime()==TheBoxT.getNowTime()) && (allBox.get(indexBottom).isHadUse() == false||allBox.get(indexBottom).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth ;
                                                        layoutParams.height = BoxHeight* 3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexTop).getX());
                                                        addImage.setY((float) allBox.get(indexTop).getY());
                                                        addImage.setImageResource(R.drawable.i3x1);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexTop).setIndex(0);
                                                        allBox.get(indexTop).setHadUse(true);
                                                        allBox.get(indexTop).setNowTime(time);
                                                        allBox.get(indexTop).setImageView(addImage);
                                                        allBox.get(indexTop).setBoxType(BoxTypes.Old_A_New_3_Y);
                                                        //左侧这个
                                                        allBox.get(indexBottom).setIndex(2);
                                                        allBox.get(indexBottom).setHadUse(true);
                                                        allBox.get(indexBottom).setNowTime(time);
                                                        allBox.get(indexBottom).setImageView(addImage);
                                                        allBox.get(indexBottom).setBoxType(BoxTypes.Old_A_New_3_Y);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexTop).isHadUse() == false||allBox.get(indexTop).getNowTime()==TheBoxT.getNowTime()) && (allBox.get(indexBottom).isHadUse() == false||allBox.get(indexBottom).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(allBox.get(indexBottom_2).isHadUse() == false||allBox.get(indexBottom_2).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth ;
                                                        layoutParams.height = BoxHeight* 4;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexTop).getX());
                                                        addImage.setY((float) allBox.get(indexTop).getY());
                                                        addImage.setImageResource(R.drawable.i4x1);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先清除 在新增
                                                        //把之前的盛放的view给置空
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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

                                                        allBox.get(indexTop).setIndex(0);
                                                        allBox.get(indexTop).setHadUse(true);
                                                        allBox.get(indexTop).setNowTime(time);
                                                        allBox.get(indexTop).setImageView(addImage);
                                                        allBox.get(indexTop).setBoxType(BoxTypes.New_4_Y);
                                                        //纵3
                                                        allBox.get(indexBottom).setIndex(2);
                                                        allBox.get(indexBottom).setHadUse(true);
                                                        allBox.get(indexBottom).setNowTime(time);
                                                        allBox.get(indexBottom).setImageView(addImage);
                                                        allBox.get(indexBottom).setBoxType(BoxTypes.New_4_Y);

                                                        allBox.get(indexBottom_2).setIndex(3);
                                                        allBox.get(indexBottom_2).setHadUse(true);
                                                        allBox.get(indexBottom_2).setNowTime(time);
                                                        allBox.get(indexBottom_2).setImageView(addImage);
                                                        allBox.get(indexBottom_2).setBoxType(BoxTypes.New_4_Y);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(index_Left).isHadUse() == false||allBox.get(index_Left).getNowTime()==TheBoxT.getNowTime()) && (allBox.get(index_Right).isHadUse() == false||allBox.get(index_Right).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(allBox.get(index_Right_2).isHadUse() == false||allBox.get(index_Right_2).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth* 4 ;
                                                        layoutParams.height = BoxHeight;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(index_Left).getX());
                                                        addImage.setY((float) allBox.get(index_Left).getY());
                                                        addImage.setImageResource(R.drawable.i1x4);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(index_Left).setIndex(0);
                                                        allBox.get(index_Left).setHadUse(true);
                                                        allBox.get(index_Left).setNowTime(time);
                                                        allBox.get(index_Left).setImageView(addImage);
                                                        allBox.get(index_Left).setBoxType(BoxTypes.New_4_X);
                                                        //
                                                        allBox.get(index_Right).setIndex(2);
                                                        allBox.get(index_Right).setHadUse(true);
                                                        allBox.get(index_Right).setNowTime(time);
                                                        allBox.get(index_Right).setImageView(addImage);
                                                        allBox.get(index_Right).setBoxType(BoxTypes.New_4_X);

                                                        allBox.get(index_Right_2).setIndex(3);
                                                        allBox.get(index_Right_2).setHadUse(true);
                                                        allBox.get(index_Right_2).setNowTime(time);
                                                        allBox.get(index_Right_2).setImageView(addImage);
                                                        allBox.get(index_Right_2).setBoxType(BoxTypes.New_4_X);
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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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

                                                    if ((allBox.get(indexTop_Right).isHadUse() == false||allBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime()) && (allBox.get(indexBottom_Left).isHadUse() == false||allBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime())
                                                            &&(oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&(allBox.get(indexBottom_Right).isHadUse() == false||allBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
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
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexTop_Right).setIndex(1);
                                                        allBox.get(indexTop_Right).setHadUse(true);
                                                        allBox.get(indexTop_Right).setNowTime(time);
                                                        allBox.get(indexTop_Right).setImageView(addImage);
                                                        allBox.get(indexTop_Right).setBoxType(BoxTypes.New_4_XY);
                                                        //
                                                        allBox.get(indexBottom_Left).setIndex(2);
                                                        allBox.get(indexBottom_Left).setHadUse(true);
                                                        allBox.get(indexBottom_Left).setNowTime(time);
                                                        allBox.get(indexBottom_Left).setImageView(addImage);
                                                        allBox.get(indexBottom_Left).setBoxType(BoxTypes.New_4_XY);

                                                        allBox.get(indexBottom_Right).setIndex(3);
                                                        allBox.get(indexBottom_Right).setHadUse(true);
                                                        allBox.get(indexBottom_Right).setNowTime(time);
                                                        allBox.get(indexBottom_Right).setImageView(addImage);
                                                        allBox.get(indexBottom_Right).setBoxType(BoxTypes.New_4_XY);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexTop_Left).isHadUse() == false||allBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexTop_Right).isHadUse() == false||allBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexRight).isHadUse() == false||allBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                    (allBox.get(indexBottom_Left).isHadUse() == false||allBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexBottom_Right).isHadUse() == false||allBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*2 ;
                                                        layoutParams.height = BoxHeight* 3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  allBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i3x2);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空操作
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexTop_Left).setIndex(0);
                                                        allBox.get(indexTop_Left).setHadUse(true);
                                                        allBox.get(indexTop_Left).setNowTime(time);
                                                        allBox.get(indexTop_Left).setImageView(addImage);
                                                        allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_6_Y);
                                                        //
                                                        allBox.get(indexTop_Right).setIndex(1);
                                                        allBox.get(indexTop_Right).setHadUse(true);
                                                        allBox.get(indexTop_Right).setNowTime(time);
                                                        allBox.get(indexTop_Right).setImageView(addImage);
                                                        allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        allBox.get(indexRight).setIndex(3);
                                                        allBox.get(indexRight).setHadUse(true);
                                                        allBox.get(indexRight).setNowTime(time);
                                                        allBox.get(indexRight).setImageView(addImage);
                                                        allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        allBox.get(indexBottom_Left).setIndex(4);
                                                        allBox.get(indexBottom_Left).setHadUse(true);
                                                        allBox.get(indexBottom_Left).setNowTime(time);
                                                        allBox.get(indexBottom_Left).setImageView(addImage);
                                                        allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_6_Y);

                                                        allBox.get(indexBottom_Right).setIndex(5);
                                                        allBox.get(indexBottom_Right).setHadUse(true);
                                                        allBox.get(indexBottom_Right).setNowTime(time);
                                                        allBox.get(indexBottom_Right).setImageView(addImage);
                                                        allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_6_Y);
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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexTop_Left).isHadUse() == false||allBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexTop_Right).isHadUse() == false||allBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexRight).isHadUse() == false||allBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexBottom_Left).isHadUse() == false||allBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexBottom_Right).isHadUse() == false||allBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*3 ;
                                                        layoutParams.height = BoxHeight* 2;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  allBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i2x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空操作
                                                        //先删除老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexTop_Left).setIndex(0);
                                                        allBox.get(indexTop_Left).setHadUse(true);
                                                        allBox.get(indexTop_Left).setNowTime(time);
                                                        allBox.get(indexTop_Left).setImageView(addImage);
                                                        allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_6_X);
                                                        //
                                                        allBox.get(indexTop_Right).setIndex(2);
                                                        allBox.get(indexTop_Right).setHadUse(true);
                                                        allBox.get(indexTop_Right).setNowTime(time);
                                                        allBox.get(indexTop_Right).setImageView(addImage);
                                                        allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_6_X);

                                                        allBox.get(indexRight).setIndex(4);
                                                        allBox.get(indexRight).setHadUse(true);
                                                        allBox.get(indexRight).setNowTime(time);
                                                        allBox.get(indexRight).setImageView(addImage);
                                                        allBox.get(indexRight).setBoxType(BoxTypes.Old_6_X);

                                                        allBox.get(indexBottom_Left).setIndex(3);
                                                        allBox.get(indexBottom_Left).setHadUse(true);
                                                        allBox.get(indexBottom_Left).setNowTime(time);
                                                        allBox.get(indexBottom_Left).setImageView(addImage);
                                                        allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_6_X);

                                                        allBox.get(indexBottom_Right).setIndex(5);
                                                        allBox.get(indexBottom_Right).setHadUse(true);
                                                        allBox.get(indexBottom_Right).setNowTime(time);
                                                        allBox.get(indexBottom_Right).setImageView(addImage);
                                                        allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_6_X);

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
                                        for (int i = 0; i < allBox.size(); i++) {
                                            map.put(allBox.get(i).getID(), i);
                                            if (allBox.get(i).getNowTime() == TheBoxT.getNowTime() && !allBox.get(i).getID().equals(TheBoxT.getID())) {
                                                list.add(i);
                                            }
                                        }
                                        for (int i = 0; i < allBox.size(); i++) {
                                            OneBoxItem oneBoxItem = allBox.get(i);
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
                                                    if ((allBox.get(indexTop_Left).isHadUse() == false||allBox.get(indexTop_Left).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexTop_Right).isHadUse() == false||allBox.get(indexTop_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (oneBoxItem.isHadUse() == false||oneBoxItem.getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexRight).isHadUse() == false||allBox.get(indexRight).getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexTop_Zhong).isHadUse() == false||allBox.get(indexTop_Zhong).getNowTime()==TheBoxT.getNowTime()) &&
                                                            (allBox.get(indexZhong_Left).isHadUse() == false||allBox.get(indexZhong_Left).getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexZhong_Right).isHadUse() == false||allBox.get(indexZhong_Right).getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexBottom_Left).isHadUse() == false||allBox.get(indexBottom_Left).getNowTime()==TheBoxT.getNowTime())&&
                                                            (allBox.get(indexBottom_Right).isHadUse() == false||allBox.get(indexBottom_Right).getNowTime()==TheBoxT.getNowTime())
                                                            ) {
                                                        double time = System.currentTimeMillis();
                                                        canUse++;
                                                        //实例出来一个新的表箱,用来添加到右侧页面上
                                                        ImageView addImage = new ImageView(MainActivity.this);
                                                        PercentRelativeLayout.LayoutParams layoutParams = new PercentRelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                        layoutParams.width = BoxWidth*3 ;
                                                        layoutParams.height = BoxHeight*3;
                                                        addImage.setLayoutParams(layoutParams);
                                                        addImage.setX((float) allBox.get(indexTop_Left).getX());
                                                        addImage.setY((float)  allBox.get(indexTop_Left).getY());
                                                        addImage.setImageResource(R.drawable.i3x3);
                                                        addImage.setBackground(getResources().getDrawable(R.drawable.shape_rect));
                                                        //清空老数据
                                                        TheBoxT.setHadUse(false);
                                                        for (int index_delete : list) {
                                                            allBox.get(index_delete).setHadUse(false);
                                                            allBox.get(index_delete).setHadUse(false);
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
                                                        allBox.get(indexTop_Zhong).setIndex(1);
                                                        allBox.get(indexTop_Zhong).setHadUse(true);
                                                        allBox.get(indexTop_Zhong).setNowTime(time);
                                                        allBox.get(indexTop_Zhong).setImageView(addImage);
                                                        allBox.get(indexTop_Zhong).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        allBox.get(indexZhong_Left).setIndex(3);
                                                        allBox.get(indexZhong_Left).setHadUse(true);
                                                        allBox.get(indexZhong_Left).setNowTime(time);
                                                        allBox.get(indexZhong_Left).setImageView(addImage);
                                                        allBox.get(indexZhong_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        allBox.get(indexZhong_Left).setIndex(5);
                                                        allBox.get(indexZhong_Right).setHadUse(true);
                                                        allBox.get(indexZhong_Right).setNowTime(time);
                                                        allBox.get(indexZhong_Right).setImageView(addImage);
                                                        allBox.get(indexZhong_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        //
                                                        allBox.get(indexTop_Left).setIndex(0);
                                                        allBox.get(indexTop_Left).setHadUse(true);
                                                        allBox.get(indexTop_Left).setNowTime(time);
                                                        allBox.get(indexTop_Left).setImageView(addImage);
                                                        allBox.get(indexTop_Left).setBoxType(BoxTypes.Old_A_New_9_XY);
                                                        //
                                                        allBox.get(indexTop_Right).setIndex(2);
                                                        allBox.get(indexTop_Right).setHadUse(true);
                                                        allBox.get(indexTop_Right).setNowTime(time);
                                                        allBox.get(indexTop_Right).setImageView(addImage);
                                                        allBox.get(indexTop_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        allBox.get(indexRight).setIndex(7);
                                                        allBox.get(indexRight).setHadUse(true);
                                                        allBox.get(indexRight).setNowTime(time);
                                                        allBox.get(indexRight).setImageView(addImage);
                                                        allBox.get(indexRight).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        allBox.get(indexBottom_Left).setIndex(6);
                                                        allBox.get(indexBottom_Left).setHadUse(true);
                                                        allBox.get(indexBottom_Left).setNowTime(time);
                                                        allBox.get(indexBottom_Left).setImageView(addImage);
                                                        allBox.get(indexBottom_Left).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                        allBox.get(indexBottom_Right).setIndex(8);
                                                        allBox.get(indexBottom_Right).setHadUse(true);
                                                        allBox.get(indexBottom_Right).setNowTime(time);
                                                        allBox.get(indexBottom_Right).setImageView(addImage);
                                                        allBox.get(indexBottom_Right).setBoxType(BoxTypes.Old_A_New_9_XY);

                                                    }
                                                } catch (Exception e) {
                                                    Log.e("这个不合适二横表箱", "===");
                                                }
                                            }
                                        }
                                    }//这里写下一个
                                    if (canUse == 0) {
                                        mainView.removeView(showImage_Right);
                                        Toast.makeText(MainActivity.this, "该位置无效,请重试", Toast.LENGTH_SHORT).show();
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
