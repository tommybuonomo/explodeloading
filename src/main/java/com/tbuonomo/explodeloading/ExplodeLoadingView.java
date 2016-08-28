package com.tbuonomo.explodeloading;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tommy on 02/07/16.
 */
public class ExplodeLoadingView extends RelativeLayout {
    private static final int DEFAULT_DURATION = 1500;
    private static final int DEFAULT_POINT_COLOR = Color.WHITE;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#01579B");
    private static final int DEFULT_NB_CIRCLES = 7;
    private int pointRadius;

    private List<ImageView> mCircles;
    private AnimatorSet mCircleAnimator;
    private int mDuration;
    private int size;

    public ExplodeLoadingView(Context context) {
        super(context);
        init(context, null);
    }

    public ExplodeLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExplodeLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        removeViewsIfNeeded();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExplodeLoadingView);

            int numberPoints = a.getInt(R.styleable.ExplodeLoadingView_pointsNumber, -1);
            setUpPoints(context, numberPoints <= 0 ? DEFULT_NB_CIRCLES : numberPoints);

            int pointColor = a.getColor(R.styleable.ExplodeLoadingView_pointsColor, -1);
            setUpCircleColors(pointColor == -1 ?
                    DEFAULT_POINT_COLOR : pointColor);

            int duration = a.getInt(R.styleable.ExplodeLoadingView_animationDuration, -1);
            mDuration = duration == -1 || duration < 0 ? DEFAULT_DURATION : duration;

            int pointSize = a.getDimensionPixelSize(R.styleable.ExplodeLoadingView_pointsSize, -1);
            if (pointSize != -1) {
                setUpCircleSize(pointSize);
            }
            a.recycle();
        } else {
            setUpCircleColors(DEFAULT_POINT_COLOR);
            mDuration = DEFAULT_DURATION;
        }
    }

    private void setUpPoints(Context context, int nbPoints) {
        mCircles = new ArrayList<>();

        for (int i = 0; i < nbPoints; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.point_circle));
            mCircles.add(imageView);
            addView(imageView);
        }
    }

    private void removeViewsIfNeeded() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = Math.min(w, h);
        setUpAnimators(mDuration);
        mCircleAnimator.start();
    }

    private void setUpAnimators(int duration) {
        final int nbPoints = mCircles.size();
        int pointRadius = mCircles.get(0).getMeasuredWidth();

        final ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0, size / 2 - pointRadius);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                for (int i = 0; i < nbPoints; i++) {
                    double angle = Math.PI * 2 / nbPoints * (i + 1);
                    double transX = Math.cos(angle) * value;
                    double transY = Math.sin(angle) * value;
                    ImageView circle = mCircles.get(i);
                    circle.setTranslationX((float) transX);
                    circle.setTranslationY((float) transY);
                }

                setRotation(valueAnimator.getAnimatedFraction() * 360);
            }
        });

        valueAnimator1.setInterpolator(new FastOutSlowInInterpolator());

        ObjectAnimator middleRotation = ObjectAnimator.ofFloat(this, View.ROTATION, 0, 360);
        middleRotation.setInterpolator(new FastOutLinearInInterpolator());

        ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(size / 2 - pointRadius, 0);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                for (int i = 0; i < nbPoints; i++) {
                    double angle = Math.PI * 2 / nbPoints * (i + 1);
                    double transX = Math.cos(angle) * value;
                    double transY = Math.sin(angle) * value;
                    ImageView circle = mCircles.get(i);
                    circle.setTranslationX((float) transX);
                    circle.setTranslationY((float) transY);
                }

                setRotation(valueAnimator.getAnimatedFraction() * 360);
            }
        });

        valueAnimator2.setInterpolator(new DecelerateInterpolator());

        valueAnimator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCircleAnimator.start();
            }
        });

        mCircleAnimator = new AnimatorSet();

        mCircleAnimator.playSequentially(valueAnimator1, middleRotation, valueAnimator2);

        for (Animator animator : mCircleAnimator.getChildAnimations()) {
            animator.setDuration(duration);
        }
    }

    private void setUpCircleSize(int size) {
        for (ImageView circle : mCircles) {
            LayoutParams params = (LayoutParams) circle.getLayoutParams();
            params.width = size;
            params.height = size;
            circle.setLayoutParams(params);
        }
    }


    private void setUpCircleColors(int color) {
        for (ImageView circle : mCircles) {
            GradientDrawable gradientDrawable = (GradientDrawable) circle.getDrawable();
            gradientDrawable.setColor(color);
        }
    }

    //*********************************************************
    // Users Methods
    //*********************************************************

    public void setPointsColor(int color) {
        setUpCircleColors(color);
    }

    //*********************************************************
    // Lifecycle
    //*********************************************************

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCircleAnimator != null) {
            mCircleAnimator.end();
            mCircleAnimator.removeAllListeners();
            mCircleAnimator = null;
        }
    }
}
