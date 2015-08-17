package pl.srw.billcalculator.form.fragment;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.settings.GeneralPreferences;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 29.05.15.
 */
public class PgeFormFragment extends DoubleReadingsFormFragment {

    @Override
    public boolean isTariffG12() {
        return GeneralPreferences.isPgeTariffG12();
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGE;
    }

    @Override
    protected CurrentReadingType[] getCurrentReadingTypes() {
        return new CurrentReadingType[]{CurrentReadingType.PGE_TO, CurrentReadingType.PGE_DAY_TO, CurrentReadingType.PGE_NIGHT_TO};
    }

    public int getTitle() {
        return R.string.new_pge_bill;
    }
}
