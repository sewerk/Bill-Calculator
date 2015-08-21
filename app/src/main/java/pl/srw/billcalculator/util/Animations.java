package pl.srw.billcalculator.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by kseweryn on 18.06.15.
 */
public final class Animations {

    private static final long DURATION_LONG = 1000;
    private static final long DURATION_SHORT = 200;
    private static final ObjectAnimator SHAKE = ObjectAnimator.ofFloat(null, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0).setDuration(DURATION_LONG);

    private Animations() {
    }

    public static void shake(View target) {
        SHAKE.setTarget(target);
        SHAKE.start();
    }

    public static AnimatorSet getExpandFabs(final FloatingActionButton baseFab, final FloatingActionButton... fabs) {
        final float margin = ((RelativeLayout.LayoutParams) baseFab.getLayoutParams()).bottomMargin;

        final ValueAnimator fabRotation = rotationAnimator(baseFab, 0f, 135f);
        final ValueAnimator translationY = translationYAnimator(baseFab.getTop(), margin, 0, baseFab.getHeight(), fabs);
        final ValueAnimator alpha = alphaAnimator(0f, 1f, fabs);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (int i = 0; i < fabs.length; i++)
                    fabs[i].setVisibility(View.VISIBLE);
            }
        });
        animatorSet.setDuration(DURATION_SHORT);
        animatorSet.setInterpolator(new OvershootInterpolator());
        return animatorSet;
    }

    public static AnimatorSet getCollapseFabs(final FloatingActionButton baseFab, final FloatingActionButton... fabs) {
        final float margin = ((RelativeLayout.LayoutParams) baseFab.getLayoutParams()).bottomMargin;

        final ValueAnimator fabRotation = rotationAnimator(baseFab, 135f, 0f);
        final ValueAnimator translationY = translationYAnimator(baseFab.getTop(), margin, baseFab.getHeight(), 0, fabs);
        final ValueAnimator alpha = alphaAnimator(1f, 0f, fabs);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0; i < fabs.length; i++)
                    fabs[i].setVisibility(View.GONE);
            }
        });
        animatorSet.setDuration(DURATION_SHORT);
        animatorSet.setInterpolator(new OvershootInterpolator());
        return animatorSet;
    }

    @NonNull
    private static ValueAnimator rotationAnimator(final FloatingActionButton target, float from, float to) {
        final ValueAnimator fabRotation = ValueAnimator.ofFloat(from, to);
        fabRotation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                target.setRotation((Float) animation.getAnimatedValue());
            }
        });
        return fabRotation;
    }

    @NonNull
    private static ValueAnimator translationYAnimator(final int fromPosition, final float extraShift, int startY, int endY, final FloatingActionButton[] targets) {
        final ValueAnimator translationY = ValueAnimator.ofInt(startY, endY);
        translationY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float dY = (Integer) animation.getAnimatedValue() + extraShift;
                Log.d("ANIMATION", "dY="+ dY + ", shift=" + extraShift);
                for (int i = 0; i < targets.length; i++)
                    targets[i].setTranslationY(fromPosition - (i + 1) * dY);
            }
        });
        return translationY;
    }

    @NonNull
    private static ValueAnimator alphaAnimator(float from, float to, final FloatingActionButton[] targets) {
        final ValueAnimator alpha = ValueAnimator.ofFloat(from, to);
        alpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float) animation.getAnimatedValue();
                if (value >= 0.0f && value <= 1.0)
                    for (int i = 0; i < targets.length; i++)
                        targets[i].setAlpha(value);
            }
        });
        return alpha;
    }

//    public static void reveal() {
//        Animator reveal = ViewAnimationUtils.createCircularReveal(
//                viewToReveal, // The new View to reveal
//                centerX,      // x co-ordinate to start the mask from
//                centerY,      // y co-ordinate to start the mask from
//                startRadius,  // radius of the starting mask
//                endRadius);   // radius of the final mask
//        reveal.start();
//    }

}
