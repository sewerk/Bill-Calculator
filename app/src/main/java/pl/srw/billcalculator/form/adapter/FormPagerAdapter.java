package pl.srw.billcalculator.form.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import pl.srw.billcalculator.form.fragment.FormFragment;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 28.05.15.
 */
public class FormPagerAdapter extends FragmentPagerAdapter {

    public FormPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Provider.values()[position].toString();
    }

    @Override
    public Fragment getItem(int position) {
        return FormFragment.of(Provider.values()[position]);
    }

    @Override
    public int getCount() {
        return Provider.values().length;
    }
}
