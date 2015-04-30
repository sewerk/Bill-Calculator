package pl.srw.billcalculator.form;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 30.04.15.
 */
public class PgnigFormView extends FormView {

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
        currentReading.setHint(R.string.reading_hint_m3);
        previousReading.setHint(R.string.reading_hint_m3);
    }

    @Override
    protected Intent getBillActivityIntent() {
        IntentCreator intentCreator = BillActivityIntentFactory.of(getContext(), Provider.PGNIG);
        return provideExtra(intentCreator);
    }

    @Override
    protected Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getContext(), Provider.PGNIG);
        return provideExtra(intentCreator);
    }

    private Intent provideExtra(final IntentCreator intentCreator) {
        return intentCreator.from(previousReading, currentReading, fromDate, toDate);
    }
}
