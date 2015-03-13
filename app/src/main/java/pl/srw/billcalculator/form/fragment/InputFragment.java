package pl.srw.billcalculator.form.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.event.HistoryChangedEvent;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public abstract class InputFragment extends Fragment {

    protected EventBus eventBus = EventBus.getDefault();

    @InjectView(R.id.button_date_from) Button bFromDate;
    @InjectView(R.id.button_date_to) Button bToDate;
    @InjectView(R.id.editText_date_to_error) TextView etToDateError;

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        eventBus.register(this);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        initDates();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventBus.unregister(this);
    }

    public void onEvent(HistoryChangedEvent event) {
        markHistoryChanged();
    }

    protected abstract void markHistoryChanged();

    private void initDates() {
        bFromDate.setText(Dates.format(Dates.firstDayOfThisMonth()));
        bToDate.setText(Dates.format(Dates.lastDayOfThisMonth()));
    }

    @OnClick({R.id.button_date_from, R.id.button_date_to})
    public void showDatePicker(final Button datePickerButton) {
        final LocalDate date = Dates.parse(datePickerButton.getText().toString());
        final int year = date.getYear();
        final int monthValue = date.getMonthValue() - 1;
        final int dayOfMonth = date.getDayOfMonth();

        new DatePickerDialog(getActivity(), onDatePickedListener(datePickerButton), year, monthValue, dayOfMonth).show();
    }

    private DatePickerDialog.OnDateSetListener onDatePickedListener(final Button button) {
        return new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                button.setText(Dates.format(year, Month.of(month + 1), day));
                etToDateError.setError(null);
            }
        };
    }

    @DebugLog
    @OnClick(R.id.button_calculate)
    public void calculate() {
        if (!validateForm())
            return;

        getActivity().startActivity(getBillActivityIntent());
        getActivity().startService(getBillStorerIntent());
        markHistoryChanged();
    }

    protected abstract Intent getBillActivityIntent();

    protected abstract Intent getBillStorerIntent();

    private boolean validateForm() {
        return validateReadings() && validateDates();
    }

    protected abstract boolean validateReadings();

    protected boolean validateMissingValue(final EditText et) {
        if (et.getText().toString().isEmpty()) {
            shake(et);
            showError(et, R.string.reading_missing);
            return false;
        }
        return true;
    }

    protected boolean validateValueOrder(final EditText prev, final EditText curr) {
        if (!(Integer.parseInt(curr.getText().toString()) > Integer.parseInt(prev.getText().toString()))) {
            shake(curr);
            showError(curr, R.string.reading_order_error);
            return false;
        }
        return true;
    }

    private void showError(EditText et, @StringRes int error_id) {
        et.setError(getString(error_id));
        et.requestFocus();
        showKeyboard(et);
    }

    private boolean validateDates() {
        String fromDate = bFromDate.getText().toString();
        String toDate = bToDate.getText().toString();
        LocalDate from = Dates.parse(fromDate);
        LocalDate to = Dates.parse(toDate);
        if (!from.isBefore(to)) {
            shake(bToDate);
            etToDateError.requestFocus();
            etToDateError.setError(getString(R.string.date_error));
            return false;
        }
        return true;
    }

    private void shake(View target) {
        YoYo.with(Techniques.Shake).playOn(target);
    }

    private void showKeyboard(TextView mTextView) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            // only will trigger it if no physical keyboard is open
            imm.showSoftInput(mTextView, 0);
        }
    }

}
