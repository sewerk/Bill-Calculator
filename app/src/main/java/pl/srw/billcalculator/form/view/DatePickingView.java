package pl.srw.billcalculator.form.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.DatePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.util.Dates;

public class DatePickingView extends AppCompatButton {

    private OnDatePickedListener onDatePickedListener;
    private ErrorViewHandler errorHandler;

    public DatePickingView(Context context) {
        super(context);
    }

    public DatePickingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DatePickingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        errorHandler = new ErrorViewHandler(this, attrs, defStyleAttr);
        ButterKnife.bind(this);
    }

    @Override
    public void setError(CharSequence error) {
        errorHandler.setError(error);
    }

    public void setError(@StringRes int errorId) {
        errorHandler.setError(errorId);
    }

    @OnClick
    void showDatePicker() {
        final LocalDate date = Dates.parse(this.getText().toString());
        final int year = date.getYear();
        final int monthValue = date.getMonthValue() - 1;
        final int dayOfMonth = date.getDayOfMonth();

        new DatePickerDialog(getContext(), onDatePickedListener(), year, monthValue, dayOfMonth).show();
    }

    private DatePickerDialog.OnDateSetListener onDatePickedListener() {
        return new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                DatePickingView.this.setText(Dates.format(year, Month.of(month + 1), day));
                setError(null);
                if (onDatePickedListener != null) {
                    onDatePickedListener.onDatePicked(DatePickingView.this);
                }
            }
        };
    }

    public void setOnDatePickedListener(OnDatePickedListener listener) {
        onDatePickedListener = listener;
    }

    public interface OnDatePickedListener {
        void onDatePicked(DatePickingView view);
    }
}
