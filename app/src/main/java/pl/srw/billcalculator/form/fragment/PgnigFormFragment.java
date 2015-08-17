package pl.srw.billcalculator.form.fragment;

import android.os.Bundle;
import android.view.View;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 29.05.15.
 */
public class PgnigFormFragment extends SingleReadingsFormFragment {

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
    protected Provider getProvider() {
        return Provider.PGNIG;
    }

    @Override
    protected CurrentReadingType[] getCurrentReadingTypes() {
        return new CurrentReadingType[]{CurrentReadingType.PGNIG_TO};
    }

    public int getTitle() {
        return R.string.new_pgnig_bill;
    }
}
