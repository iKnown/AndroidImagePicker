package com.iknow.imageselect.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by gordon on 8/2/16.
 */
public class SquareImageView extends ImageView {
    private int drawingColor = Color.argb(255, 255, 0, 0);

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawColor(drawingColor);
    }

    private int getDrawingColor(float alpha) {
        return Color.argb((int) (alpha * 255), 255, 0, 0);
    }

    public void disperse(int duration) {
        ValueAnimator animator = ValueAnimator.ofFloat(1F, 0F);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float) valueAnimator.getAnimatedValue();
                drawingColor = getDrawingColor(alpha);
                invalidate();
            }
        });
        animator.setDuration(duration);
        animator.start();
    }
}
