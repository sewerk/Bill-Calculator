package pl.srw.billcalculator.form.pge;

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
@RequiresPresenter(PgeFormPresenter.class)
public class PgeFormView extends DoubleReadingsFormView {

    public PgeFormView(Context context) {
        super(context);
    }

    public PgeFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGE;
    }

    @Override
    protected void attachPreviousReadingAdapter() {
        etDayPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGE_DAY_TO));
        etNightPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGE_NIGHT_TO));
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGE_TO));
    }
}
