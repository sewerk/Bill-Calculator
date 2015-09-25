package pl.srw.billcalculator.form.fragment;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 29.05.15.
 */
public class TauronFormFragment extends FormFragment {
    
    @Override
    public boolean isTariffG12() {
        return GeneralPreferences.isTauronTariffG12();
    }

    @Override
    protected Provider getProvider() {
        return Provider.TAURON;
    }

    @Override
    protected CurrentReadingType[] getCurrentReadingTypes() {
        return new CurrentReadingType[]{CurrentReadingType.TAURON_TO, CurrentReadingType.TAURON_DAY_TO, CurrentReadingType.TAURON_NIGHT_TO};
    }

    @Override
    public int getTitle() {
        return R.string.new_tauron_bill;
    }
}
