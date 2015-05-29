package pl.srw.billcalculator.form.fragment;

import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
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
    protected void attachPreviousReadingAdapter() {
        etDayPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_DAY_TO));
        etNightPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_NIGHT_TO));
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGE_TO));
    }
}
