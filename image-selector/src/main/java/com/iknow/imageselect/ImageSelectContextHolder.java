package com.iknow.imageselect;

import android.content.Context;

/**
 * Created by gordon on 8/1/16.
 */
public class ImageSelectContextHolder {
    private Context context;

    private ImageSelectContextHolder() {
    }

    private static class Holder {
        private static ImageSelectContextHolder instance = new ImageSelectContextHolder();
    }

    public static ImageSelectContextHolder getInstance() {
        return Holder.instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
