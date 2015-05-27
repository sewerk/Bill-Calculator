package pl.srw.billcalculator.form;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import nucleus.view.NucleusLayout;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.view.DatePickingButton;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.intent.BillStoringServiceIntentFactory;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 30.04.15.
 */
public abstract class SingleReadingsFormView<T extends SingleReadingsFormPresenter> extends NucleusLayout<T> {

    protected @InjectView(R.id.iv_logo) ImageView ivLogo;

    protected @InjectView(R.id.ll_readings_single) LinearLayout llReadingG11;
    protected @InjectView(R.id.et_reading_from) AutoCompleteTextView etPreviousReading;
    protected @InjectView(R.id.et_reading_to) EditText etCurrentReading;

    protected @InjectView(R.id.button_date_from) DatePickingButton bFromDate;
    protected @InjectView(R.id.button_date_to) DatePickingButton bToDate;

    public SingleReadingsFormView(Context context) {
        super(context);
        init();
    }

    public SingleReadingsFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.form_g11, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        ivLogo.setImageResource(getProvider().logoRes);
        attachPreviousReadingAdapter();
    }

    public void initDates(String firsDayOfMonth, String lastDayOfMonth) {
        bFromDate.setText(firsDayOfMonth);
        bToDate.setText(lastDayOfMonth);
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        getPresenter().processForm(etPreviousReading.getText().toString(), etCurrentReading.getText().toString(),
                bFromDate.getText().toString(), bToDate.getText().toString());
    }

    protected abstract Provider getProvider();

    protected abstract void attachPreviousReadingAdapter();

    protected Intent getBillActivityIntent() {
        IntentCreator intentCreator = BillActivityIntentFactory.of(getContext(), getProvider());
        return provideExtra(intentCreator);
    }

    protected Intent getBillStorerIntent() {
        IntentCreator intentCreator = BillStoringServiceIntentFactory.of(getContext(), getProvider());
        return provideExtra(intentCreator);
    }

    protected Intent provideExtra(final IntentCreator intentCreator) {
        return intentCreator.from(etPreviousReading, etCurrentReading, bFromDate, bToDate);
    }

    public void onValidationSuccess() {
        getContext().startActivity(getBillActivityIntent());
        getContext().startService(getBillStorerIntent());
    }

    public void onDatesError(@StringRes int errorMsg) {
        shake(bToDate);
        //TODO bToDate.showError(errorMsg);
    }

    public void onReadingError(int readingIdx, @StringRes int errorMsg) {
        onError(getView(readingIdx), errorMsg);
    }

    private void onError(EditText et, @StringRes int errorMsgRes) {
        shake(et);
        et.setError(getContext().getString(errorMsgRes));
        et.requestFocus();
        showKeyboard(et);
    }

    protected EditText getView(final int readingIdx) {
        switch (readingIdx) {
            case 0: return etPreviousReading;
            case 1: return etCurrentReading;
        }
        throw new IllegalArgumentException("Reading view with idx=" + readingIdx + " unknown.");
    }

    private void shake(View target) {
        YoYo.with(Techniques.Shake).playOn(target);
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }
}
