package pl.srw.billcalculator.form.fragment;

import android.os.Bundle;
import android.view.View;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 29.05.15.
 */
public class PgnigFormFragment extends SingleReadingFormFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setReadingsHint();
    }

    private void setReadingsHint() {
        etCurrentReading.setHint(R.string.reading_hint_m3);
        etPreviousReading.setHint(R.string.reading_hint_m3);
    }

    @Override
    protected void attachPreviousReadingAdapter() {
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getActivity(), CurrentReadingType.PGNIG_TO));
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }
}
