package com.iknow.imageselect.utils;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.iknow.imageselect.ImageSelectContextHolder;

/**
 * Author: Jason.Chou
 * Email: who_know_me@163.com
 * Created: 2016年04月26日 4:13 PM
 * Description:
 */
public class DeviceInforHelper {

    public static int getPixelFromDip(float f) {
        return getPixelFromDip(ImageSelectContextHolder.getInstance().getContext().getResources().getDisplayMetrics(), f);
    }

    public static int getPixelFromDip(DisplayMetrics dm, float dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm) + 0.5f);
    }

    public static int getScreenWidth() {
        return ImageSelectContextHolder.getInstance().getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return ImageSelectContextHolder.getInstance().getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
