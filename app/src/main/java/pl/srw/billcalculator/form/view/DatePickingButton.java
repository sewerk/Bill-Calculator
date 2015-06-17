package pl.srw.billcalculator.form.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import pl.srw.billcalculator.util.Dates;

/**
 * Created by kseweryn on 30.04.15.
 */
public class DatePickingButton extends Button {

    private OnDatePickedListener onDatePickedListener;

    public DatePickingButton(Context context) {
        super(context);
    }

    public DatePickingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
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
                DatePickingButton.this.setText(Dates.format(year, Month.of(month + 1), day));
                if (onDatePickedListener != null) {
                    onDatePickedListener.onDatePicked(DatePickingButton.this);
                }
            }
        };
    }

    public void setOnDatePickedListener(OnDatePickedListener listener) {
        onDatePickedListener = listener;
    }

    public interface OnDatePickedListener {
        void onDatePicked(DatePickingButton view);
    }
}
