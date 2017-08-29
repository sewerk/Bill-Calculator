package pl.srw.billcalculator.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public final class Animations {

    private static final long DURATION_LONG = 1000;
    private static final long DURATION_SHORT = 200;
    private static final float ROTATION_TO_ANGLE = 135f;

    private Animations() {
    }

    public static void shake(View target) {
        ObjectAnimator shakeAnimation = ObjectAnimator
                .ofFloat(null, "translationX", 0, 25, -25, 25, -25, 15, -15, 6, -6, 0)
                .setDuration(DURATION_LONG);
        shakeAnimation.setTarget(target);
        shakeAnimation.start();
    }

    public static AnimatorSet getExpandFabs(final FloatingActionButton baseFab, final FloatingActionButton... fabs) {
        final ValueAnimator fabRotation = rotationAnimator(baseFab, 0f, ROTATION_TO_ANGLE);
        final ValueAnimator translationY = translationYAnimator(fabs, true, getShift(baseFab, fabs.length));
        final ValueAnimator alpha = alphaAnimator(0f, 1f, fabs);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (FloatingActionButton fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (FloatingActionButton fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_NONE, null);
                    fab.setClickable(true);
                }
            }
        });
        animatorSet.setDuration(DURATION_SHORT);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    public static AnimatorSet getCollapseFabs(final FloatingActionButton baseFab, final FloatingActionButton... fabs) {
        final ValueAnimator fabRotation = rotationAnimator(baseFab, ROTATION_TO_ANGLE, 0f);
        final ValueAnimator translationY = translationYAnimator(fabs, false, getShift(baseFab, fabs.length));
        final ValueAnimator alpha = alphaAnimator(1f, 0f, fabs);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (FloatingActionButton fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    fab.setClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (int i = 0, fabsLength = fabs.length; i < fabsLength; i++) {
                    FloatingActionButton fab = fabs[i];
                    fab.setVisibility(View.GONE);
                    fab.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            }
        });
        animatorSet.setDuration(DURATION_SHORT);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    @NonNull
    private static ValueAnimator rotationAnimator(final FloatingActionButton target, float from, float to) {
        final ValueAnimator fabRotation = ValueAnimator.ofFloat(from, to);
        fabRotation.addUpdateListener(animation -> {
            final float animatedValue = (float) animation.getAnimatedValue();
            target.setRotation(animatedValue);
        });
        return fabRotation;
    }

    @NonNull
    private static ValueAnimator translationYAnimator(final FloatingActionButton[] targets, final boolean topDirection, int shift) {
        final ValueAnimator translationY = topDirection
                ? ValueAnimator.ofInt(0, shift)
                : ValueAnimator.ofInt(shift, 0);
        translationY.addUpdateListener(animation -> {
            final int dY = (int) animation.getAnimatedValue();
            for (int i = 0; i < targets.length; i++)
                targets[i].setTranslationY((i + 1) * -dY);
        });
        return translationY;
    }

    private static int getShift(FloatingActionButton source, int count) {
        final int bottomMargin = ((CoordinatorLayout.LayoutParams) source.getLayoutParams()).bottomMargin;
        int shift = source.getHeight() + bottomMargin;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            shift -= source.getPaddingBottom() / 2;

        // check if targets are not off screen
        final int targetMaxTop = source.getTop() - (count * shift);
        if (targetMaxTop < 0) {
            int maxShiftForAllTargetFullyVisible = shift - Math.abs(targetMaxTop / count);
            int minShiftForAllTargetFullyVisible = source.getHeight() - source.getPaddingBottom();
            shift = Math.max(maxShiftForAllTargetFullyVisible, minShiftForAllTargetFullyVisible);
        }
        return shift;
    }

    @NonNull
    private static ValueAnimator alphaAnimator(float from, float to, final FloatingActionButton[] targets) {
        final ValueAnimator alpha = ValueAnimator.ofFloat(from, to);
        alpha.addUpdateListener(animation -> {
            final float value = (float) animation.getAnimatedValue();
            if (value >= 0.0f && value <= 1.0)
                for (FloatingActionButton target : targets)
                    target.setAlpha(value);
        });
        return alpha;
    }

    // FIXME: is needed?
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
