package pl.srw.billcalculator.form.view;

/**
 * Created by Kamil Seweryn.
 */
public interface ViewPager {
    int SCROLL_STATE_IDLE = 0;

    PagerAdapter getAdapter();
    int getCurrentItem();
    void setOnPageChangeListener(OnPageChangeListener listener);
    void setCurrentItem(int i);

    interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageScrollStateChanged(int state);
        void onPageSelected(int position);
    }
}
