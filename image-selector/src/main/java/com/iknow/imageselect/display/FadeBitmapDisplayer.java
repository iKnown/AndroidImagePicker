package com.iknow.imageselect.display;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.iknow.imageselect.widget.SquareImageView;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

/**
 * Created by gordon on 8/2/16.
 */
public class FadeBitmapDisplayer implements BitmapDisplayer {

    private final int durationMillis;

    private final boolean animateFromNetwork;
    private final boolean animateFromDisk;
    private final boolean animateFromMemory;

    /**
     * @param durationMillis Duration of "fade-in" animation (in milliseconds)
     */
    public FadeBitmapDisplayer(int durationMillis) {
        this(durationMillis, true, true, true);
    }

    /**
     * @param durationMillis     Duration of "fade-in" animation (in milliseconds)
     * @param animateFromNetwork Whether animation should be played if image is loaded from network
     * @param animateFromDisk    Whether animation should be played if image is loaded from disk cache
     * @param animateFromMemory  Whether animation should be played if image is loaded from memory cache
     */
    public FadeBitmapDisplayer(int durationMillis, boolean animateFromNetwork, boolean animateFromDisk,
                               boolean animateFromMemory) {
        this.durationMillis = durationMillis;
        this.animateFromNetwork = animateFromNetwork;
        this.animateFromDisk = animateFromDisk;
        this.animateFromMemory = animateFromMemory;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        imageAware.setImageBitmap(bitmap);

        if ((animateFromNetwork && loadedFrom == LoadedFrom.NETWORK) ||
                (animateFromDisk && loadedFrom == LoadedFrom.DISC_CACHE) ||
                (animateFromMemory && loadedFrom == LoadedFrom.MEMORY_CACHE)) {
            animate(imageAware.getWrappedView(), durationMillis);
        }
    }

    /**
     * Animates {@link ImageView} with "fade-in" effect
     *
     * @param imageView      {@link ImageView} which display image in
     * @param durationMillis The length of the animation in milliseconds
     */
    public static void animate(View imageView, int durationMillis) {
        if (imageView != null && imageView instanceof SquareImageView) {
            SquareImageView squareImageView = (SquareImageView) imageView;
            squareImageView.disperse(durationMillis);
        }
    }
}
