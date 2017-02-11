package pl.srw.billcalculator.tester;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.PickerActions;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

public class FormTester extends Tester {

    public FormTester openForm(Provider provider) {
        clickView(R.id.fab);
        switch (provider) {
            case PGE:
                clickView(R.id.fab_new_pge);
                break;
            case PGNIG:
                clickView(R.id.fab_new_pgnig);
                break;
            case TAURON:
                clickView(R.id.fab_new_tauron);
                break;
        }
        return this;
    }

    public ViewInteraction autoCompleteContains(String text) {
        return onView(withId(R.id.autocomplete_text))
                .inRoot(not(isDialog()))//withDecorView(not(is(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(withText(text)));
    }

    public void autoCompleteDisplaysInOrder(String... text) {
        for (int i = 0, textLength = text.length; i < textLength; i++) {
            onData(anything()).atPosition(i)
                    .inRoot(not(isDialog()))
                    .check(matches(withText(text[i])));
        }
    }

    public FormTester putIntoReadingFrom(String text) {
        typeInto(R.id.form_entry_reading_from_input, text);
        return this;
    }

    public FormTester putIntoReadingTo(String text) {
        typeInto(R.id.form_entry_reading_to_input, text);
        return this;
    }

    public FormTester skipCheckPricesDialogIfVisible() {
        try {
            clickText(R.string.check_prices_info_cancel);
        } catch (NoMatchingViewException ex) {
            // ignore
        }
        return this;
    }

    public DatePickerTester openDateFromPicker() {
        clickView(R.id.form_entry_dates_from);
        return new DatePickerTester();
    }

    public DatePickerTester openDateToPicker() {
        clickView(R.id.form_entry_dates_to);
        return new DatePickerTester();
    }

    public void calculate() {
        clickView(R.id.calculate_button);
    }

    public FormTester closeForm() {
        clickView(R.id.close_button);
        return this;
    }

    public void hasDateFromText(String date) {
        onView(withId(R.id.form_entry_dates_from))
                .check(matches(withText(date)));
    }

    public void hasDateToError() {
        onView(withId(R.id.date_to_error))
                .check(matches(withText(R.string.date_error)));
    }

    public class DatePickerTester {
        private DatePickerTester() {
        }

        public DatePickerTester pickDate(int day, Month month, int year) {
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(year, month.getValue(), day));
            return this;
        }

        public FormTester acceptDate() {
            clickView(android.R.id.button1);
            return FormTester.this;
        }
    }
}
