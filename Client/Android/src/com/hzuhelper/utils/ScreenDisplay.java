package com.hzuhelper.utils;

import com.hzuhelper.AppContext;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class ScreenDisplay{

    private int screenWidth;
    private int screenHeight;

    private ScreenDisplay(){
        WindowManager wm = (WindowManager)AppContext.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private static ScreenDisplay screenDisplay;

    public static ScreenDisplay getInstances(){
        if (screenDisplay==null) {
            synchronized (ScreenDisplay.class) {
                if (screenDisplay==null) {
                    screenDisplay = new ScreenDisplay();
                }
            }
        }
        return screenDisplay;
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

}
