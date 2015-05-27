package pl.srw.billcalculator.form.pgnig;

import android.content.Context;
import android.util.AttributeSet;

import nucleus.view.RequiresPresenter;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.SingleReadingsFormPresenter;
import pl.srw.billcalculator.form.SingleReadingsFormView;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 30.04.15.
 */
@RequiresPresenter(SingleReadingsFormPresenter.class)
public class PgnigFormView extends SingleReadingsFormView {

    public PgnigFormView(Context context) {
        super(context);
    }

    public PgnigFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setReadingsHint();
    }

    private void setReadingsHint() {
        etCurrentReading.setHint(R.string.reading_hint_m3);
        etPreviousReading.setHint(R.string.reading_hint_m3);
    }

    @Override
    protected void attachPreviousReadingAdapter() {
        etPreviousReading.setAdapter(PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGNIG_TO));
    }

    @Override
    protected Provider getProvider() {
        return Provider.PGNIG;
    }
}
