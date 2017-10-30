package com.example.administrator.testboxdesign;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/10/11.
 */

/**
 * 获取屏幕尺寸
 */
public class Utils {
    public static float getScreenWidth(Context context){
        WindowManager wm = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
