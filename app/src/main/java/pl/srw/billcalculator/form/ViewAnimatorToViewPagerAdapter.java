package pl.srw.billcalculator.form;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.widget.ViewAnimator;

import java.util.LinkedList;
import java.util.List;

import pl.srw.billcalculator.form.view.PagerAdapter;
import pl.srw.billcalculator.form.view.ViewPager;

/**
 * Created by Kamil Seweryn.
 */
public class ViewAnimatorToViewPagerAdapter implements ViewPager {

    private OnPageChangeListener mOnPageChangeListener;
    private final ViewAnimator viewAnimator;
    private final PagerAdapter pagerAdapter;

    public ViewAnimatorToViewPagerAdapter(final ViewAnimator viewAnimator, final PagerAdapter pagerAdapter) {
        this.viewAnimator = viewAnimator;
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    public PagerAdapter getAdapter() {
        return pagerAdapter;
    }

    @Override
    public int getCurrentItem() {
        return viewAnimator.getDisplayedChild();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListener = listener;
        mOnPageChangeListener.onPageScrollStateChanged(ViewPager.SCROLL_STATE_IDLE);
    }

    @Override
    public void setCurrentItem(final int to) {
        int from = getCurrentItem();
        if (to == from)
            return;
        else if (to > from)
            animateRight(from, to);
        else
            animateLeft(from, to);
        viewAnimator.setDisplayedChild(to);
    }

    private void animateLeft(final int fromIdx, final int toIdx) {//TODO: refactor
        final int len = fromIdx - toIdx;
        ValueAnimator animatorLeft = ValueAnimator.ofFloat(1f*len, 0f);
        animatorLeft.setDuration(1000);//TODO change to 100
        animatorLeft.addListener(new AnimatorListenerAdapter() {

            private boolean canceled;

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled) // TODO: does not work
                    mOnPageChangeListener.onPageSelected(toIdx);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                canceled = true;
            }
        });
        animatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (float) animation.getAnimatedValue();
                mOnPageChangeListener.onPageScrolled(toIdx, offset, 0);
            }
        });
        animatorLeft.start();
    }

    private void animateRight(final int fromIdx, final int toIdx) {
        final int len = toIdx - fromIdx;
        ValueAnimator animatorRight = ValueAnimator.ofFloat(0f, 1f*len);
        animatorRight.setDuration(1000);
        animatorRight.addListener(new AnimatorListenerAdapter() {

            private boolean canceled;

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!canceled)
                    mOnPageChangeListener.onPageSelected(toIdx);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                canceled = true;
            }
        });
        animatorRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (float) animation.getAnimatedValue();
                mOnPageChangeListener.onPageScrolled(fromIdx, offset, 0);
            }
        });
        animatorRight.start();
    }
}
