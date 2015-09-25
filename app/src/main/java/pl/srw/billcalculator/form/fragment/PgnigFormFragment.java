package pl.srw.billcalculator.form.fragment;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 29.05.15.
 */
public class PgnigFormFragment extends FormFragment {

    @Override
    protected boolean isTariffG12() {
        return false;
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    @Override
    protected CurrentReadingType[] getCurrentReadingTypes() {
        return new CurrentReadingType[]{CurrentReadingType.PGNIG_TO};
    }

    @Override
    public int getTitle() {
        return R.string.new_pgnig_bill;
    }
}
