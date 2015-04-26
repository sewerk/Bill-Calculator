package pl.srw.billcalculator.form;

import pl.srw.billcalculator.form.view.PagerAdapter;
import pl.srw.billcalculator.settings.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class ProviderTabsProvider implements PagerAdapter {

    @Override
    public int getCount() {
        return Provider.values().length;
    }

    @Override
    public String getPageTitle(int i) { //TODO: add logo, add alfa 0.8 to unselected
        return Provider.values()[i].name();
    }
}
