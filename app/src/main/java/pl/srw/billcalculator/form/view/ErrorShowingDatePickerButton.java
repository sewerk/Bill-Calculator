package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.Bind;
import pl.srw.billcalculator.R;

/**
 * Created by kseweryn on 30.04.15.
 */
public class ErrorShowingDatePickerButton extends RelativeLayout {

    @Bind(R.id.button_date) DatePickingButton button;
    @Bind(R.id.editText_date_error) EditText errorField;

    public ErrorShowingDatePickerButton(Context context) {
        super(context);
        init();
    }

    public ErrorShowingDatePickerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.date_button_with_error, this);
        ButterKnife.bind(this);
        button.setOnDatePickedListener(new DatePickingButton.OnDatePickedListener() {
            @Override
            public void onDatePicked(DatePickingButton view) {
                clearError();
            }
        });
    }

    public void clearError() {
        errorField.setError(null);
    }

    public void showError(@StringRes int errorRes) {
        errorField.requestFocus();
        errorField.setError(getContext().getString(errorRes));
    }

    public void setText(String text) {
        button.setText(text);
    }

    public CharSequence getText() {
        return button.getText();
    }
}
