package com.iknow.imageselect.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatDrawableManager;

/**
 * Created by gordon on 5/9/16.
 */
public class DrawableUtil {
    public static Drawable decodeFromVector(Context context, int resId) {
        return AppCompatDrawableManager.get().getDrawable(context, resId);
    }
}
