package com.iknow.imageselect.display;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * Created by gordon on 8/2/16.
 */
public class DisplayOptions {
    public static DisplayImageOptions getCacheOptions() {
        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).displayer(new SimpleBitmapDisplayer()).showImageOnLoading(getDefaultDrawable()).displayer(new RoundedBitmapDisplayer(10)).build();
    }

    public static Drawable getDefaultDrawable() {
        return new ColorDrawable(getDefaultDrawableColor());
    }

    public static int getDefaultDrawableColor() {
        return 0xFF65C6BB;
    }
}
