package pl.srw.billcalculator.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

    public static AnimatorSet getExpandFabs(final FloatingActionButton baseFab, final View[][] animatedViews) {
        final View[] fabs = animatedViews[0];
        final ValueAnimator fabRotation = rotationAnimator(baseFab, 0f, ROTATION_TO_ANGLE);
        final ValueAnimator translationY = translationYAnimator(animatedViews, true, getShift(baseFab, fabs.length));
        final ValueAnimator alpha = alphaAnimator(0f, 1f, animatedViews);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (View fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
                for (View[] views : animatedViews) {
                    for (View view : views) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (View fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_NONE, null);
                    fab.setClickable(true);
                }
            }
        });
        animatorSet.setDuration(DURATION_SHORT);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    public static AnimatorSet getCollapseFabs(final FloatingActionButton baseFab, final View[][] animatedViews) {
        final View[] fabs = animatedViews[0];
        final ValueAnimator fabRotation = rotationAnimator(baseFab, ROTATION_TO_ANGLE, 0f);
        final ValueAnimator translationY = translationYAnimator(animatedViews, false, getShift(baseFab, fabs.length));
        final ValueAnimator alpha = alphaAnimator(1f, 0f, animatedViews);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(fabRotation, translationY, alpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (View fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    fab.setClickable(false);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                for (View fab : fabs) {
                    fab.setLayerType(View.LAYER_TYPE_NONE, null);
                }
                for (View[] views : animatedViews) {
                    for (View view : views) {
                        view.setVisibility(View.GONE);
                    }
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
    private static ValueAnimator translationYAnimator(final View[][] targets, final boolean topDirection, int shift) {
        final ValueAnimator translationY = topDirection
                ? ValueAnimator.ofInt(0, shift)
                : ValueAnimator.ofInt(shift, 0);
        translationY.addUpdateListener(animation -> {
            final int dY = (int) animation.getAnimatedValue();
            for (int i = 0; i < targets.length; i++)
                for (int j = 0; j < targets[i].length; j++)
                    targets[i][j].setTranslationY((j + 1) * -dY);
        });
        return translationY;
    }

    private static int getShift(FloatingActionButton source, int count) {
        final int bottomMargin = ((ConstraintLayout.LayoutParams) source.getLayoutParams()).bottomMargin;
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
    private static ValueAnimator alphaAnimator(float from, float to, final View[][] targets) {
        final ValueAnimator alpha = ValueAnimator.ofFloat(from, to);
        alpha.addUpdateListener(animation -> {
            final float value = (float) animation.getAnimatedValue();
            if (value >= 0.0f && value <= 1.0)
                for (View[] views : targets)
                    for (View view : views)
                        view.setAlpha(value);
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
