package pl.srw.billcalculator.form.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.form.fragment.PgeFormFragment;
import pl.srw.billcalculator.form.fragment.PgnigFormFragment;
import pl.srw.billcalculator.form.fragment.TauronFormFragment;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
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

    @DebugLog
    @Override
    public Fragment getItem(int position) {
        final Provider provider = Provider.values()[position];
        switch (provider) {
            case PGE: return new PgeFormFragment();
            case PGNIG: return new PgnigFormFragment();
            case TAURON: return new TauronFormFragment();
        }
        throw new EnumVariantNotHandledException(provider);
    }

    @Override
    public int getCount() {
        return Provider.values().length;
    }
}
