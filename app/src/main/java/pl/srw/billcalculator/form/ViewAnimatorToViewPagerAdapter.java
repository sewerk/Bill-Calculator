package pl.srw.billcalculator.form;

import android.widget.ViewAnimator;

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
    public void setCurrentItem(final int toIdx) {
        int from = getCurrentItem();
        if (toIdx == from)
            return;
        mOnPageChangeListener.onPageSelected(toIdx);
        viewAnimator.setDisplayedChild(toIdx);
    }
}
