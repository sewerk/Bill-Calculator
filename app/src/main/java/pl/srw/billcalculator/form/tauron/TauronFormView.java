package pl.srw.billcalculator.form.tauron;

import android.content.Context;
import android.util.AttributeSet;

import nucleus.view.RequiresPresenter;
import pl.srw.billcalculator.form.DoubleReadingsFormView;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 27.05.15.
 */
@RequiresPresenter(TauronFormPresenter.class)
public class TauronFormView extends DoubleReadingsFormView {

    public TauronFormView(Context context) {
        super(context);
    }

    public TauronFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Provider getProvider() {
        return Provider.TAURON;
    }

    @Override
    protected void attachPreviousReadingAdapter() {
        etDayPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.TAURON_DAY_TO));
        etNightPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.TAURON_NIGHT_TO));
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.TAURON_TO));
    }
}
