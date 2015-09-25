package pl.srw.billcalculator.form.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import pl.srw.billcalculator.util.Dates;

/**
 * Created by kseweryn on 30.04.15.
 */
public class DatePickingView extends AppCompatButton {

    private OnDatePickedListener onDatePickedListener;
    private ErrorViewHandler errorHandler;

    public DatePickingView(Context context) {
        super(context);
    }

    public DatePickingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        errorHandler = new ErrorViewHandler(this, attrs, 0);
    }

    public DatePickingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        errorHandler = new ErrorViewHandler(this, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    @Override
    public void setError(CharSequence error) {
        errorHandler.setError(error);
    }

    private void showDatePicker() {
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
