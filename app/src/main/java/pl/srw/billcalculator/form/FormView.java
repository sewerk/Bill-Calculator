package pl.srw.billcalculator.form;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import nucleus.view.NucleusLayout;
import nucleus.view.RequiresPresenter;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapter;
import pl.srw.billcalculator.form.adapter.PreviousReadingsAdapterFactory;
import pl.srw.billcalculator.form.view.DatePickingButton;
import pl.srw.billcalculator.form.view.ErrorShowingDatePickerButton;
import pl.srw.billcalculator.persistence.type.CurrentReadingType;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 30.04.15.
 */
@RequiresPresenter(FormPresenter.class)
public abstract class FormView extends NucleusLayout<FormPresenter> {

    private Provider provider;

    @InjectView(R.id.iv_logo) ImageView logo;
    @InjectView(R.id.et_reading_from) AutoCompleteTextView previousReading;
    @InjectView(R.id.et_reading_to) EditText currentReading;
    @InjectView(R.id.button_date_from) DatePickingButton fromDate;
    @InjectView(R.id.button_date_to) ErrorShowingDatePickerButton toDate;
    @InjectView(R.id.button_calculate) Button calculate;
    private PreviousReadingsAdapter adapter;

    public FormView(Context context) {
        super(context);
        init();
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.FormView, 0, 0);
        try {
            provider = Provider.values()[typedArray.getInteger(R.styleable.FormView_provider, -1)];
        } finally {
            typedArray.recycle();
        }
        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.form_g11, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        logo.setImageResource(provider.logoRes);
        previousReading.setAdapter(getPreviousReadingAdapter());
    }

    private PreviousReadingsAdapter getPreviousReadingAdapter() {
        switch (provider) {
            case PGE:
                //TODO: if (getPresenter().getTariff())
                return PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGE_TO);
            case PGNIG:
                return PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.PGNIG_TO);
            case TAURON:
                return PreviousReadingsAdapterFactory.build(getContext(), CurrentReadingType.TAURON_TO);
            default:
                throw new EnumVariantNotHandledException(provider);
        }
    }

    public void initDates(String firsDayOfMonth, String lastDayOfMonth) {
        fromDate.setText(firsDayOfMonth);
        toDate.setText(lastDayOfMonth);
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        getPresenter().processForm(previousReading.getText().toString(), currentReading.getText().toString(),
                fromDate.getText().toString(), toDate.getText().toString());
    }

    public void onValidationSuccess() {
        getContext().startActivity(getBillActivityIntent());
        getContext().startService(getBillStorerIntent());
    }

    public void onReadingError(int readingIdx, @StringRes int errorMsg) {
        onError(getView(readingIdx), errorMsg);
    }

    public void onDatesError(@StringRes int errorMsg) {
        shake(toDate);
        toDate.showError(errorMsg);
    }

    protected abstract Intent getBillStorerIntent();

    protected abstract Intent getBillActivityIntent();

    private EditText getView(final int readingIdx) {
        //TODO: G12
        switch (readingIdx) {
            case 0: return previousReading;
            case 1: return currentReading;
        }
        return null;
    }

    private void onError(EditText et, @StringRes int errorMsgRes) {
        shake(et);
        et.setError(getContext().getString(errorMsgRes));
        et.requestFocus();
        showKeyboard(et);
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
