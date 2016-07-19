package com.iknow.imageselect.utils;

import android.graphics.drawable.Animatable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import com.iknow.imageselect.photodraweeview.PhotoDraweeView;

/**
 * Created by gordon on 5/9/16.
 */
public class PhotoDraweeViewUtil {
    public static void display(final PhotoDraweeView draweeView, Uri uri) {
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(uri);
        controller.setOldController(draweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || draweeView == null) {
                    return;
                }
                draweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        draweeView.setController(controller.build());
    }
}
