package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kseweryn on 23.09.15.
 */
public class ReadingsEntryView extends TextInputLayout implements ReadingsEntryHandling {

    private ErrorViewHandler errorHandler;

    public ReadingsEntryView(Context context) {
        super(context);
    }

    public ReadingsEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        errorHandler = new ErrorViewHandler(this, attrs, 0);

    }

    public ReadingsEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        errorHandler = new ErrorViewHandler(this, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //noinspection ConstantConditions
        getEditText().addTextChangedListener(hideErrorOnTextChangedWatcher());
    }

    @Override
    public String getText() {
        return ((EditText) getChildAt(0)).getText().toString();
    }

    @Override
    public void setErrorView(TextView errorView) {
        errorHandler.setView(errorView);
    }

    @Override
    public void setError(CharSequence errorMsg) {
        errorHandler.setError(errorMsg);
    }

    @Override
    public void setError(@StringRes int errorMsgRes) {
        errorHandler.setError(errorMsgRes);
    }

    @NonNull
    private TextWatcher hideErrorOnTextChangedWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                errorHandler.setError(null);
            }
        };
    }
}
