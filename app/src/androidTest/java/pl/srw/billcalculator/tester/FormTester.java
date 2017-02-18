package pl.srw.billcalculator.tester;

import android.support.test.espresso.contrib.PickerActions;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;

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

    private AppTester parent;
    private BillTester billTester = new BillTester(parent);
    private ProviderSettingsTester<FormTester> settingsTester = new ProviderSettingsTester<>(this);

    public FormTester(AppTester parent) {
        this.parent = parent;
    }

    public FormTester putIntoReadingFrom(String text) {
        typeInto(R.id.form_entry_reading_from_input, text);
        return this;
    }

    public FormTester putIntoReadingTo(String text) {
        typeInto(R.id.form_entry_reading_to_input, text);
        return this;
    }

    public FormTester putIntoReadingDayFrom(String value) {
        typeInto(R.id.form_entry_reading_day_from_input, value);
        return this;
    }

    public FormTester putIntoReadingDayTo(String value) {
        typeInto(R.id.form_entry_reading_day_to_input, value);
        return this;
    }

    public FormTester putIntoReadingNightFrom(String value) {
        typeInto(R.id.form_entry_reading_night_from_input, value);
        return this;
    }

    public FormTester putIntoReadingNightTo(String value) {
        typeInto(R.id.form_entry_reading_night_to_input, value);
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

    public BillTester calculate() {
        clickView(R.id.calculate_button);
        return billTester;
    }

    public void autoCompleteContains(String text) {
        onView(withId(R.id.autocomplete_text))
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

    public void hasDateFromText(String date) {
        onView(withId(R.id.form_entry_dates_from))
                .check(matches(withText(date)));
    }

    public void hasDateToError() {
        onView(withId(R.id.date_to_error))
                .check(matches(withText(R.string.date_error)));
    }

    public ProviderSettingsTester<FormTester> openProviderSettings() {
        clickView(R.id.form_settings_link);
        return settingsTester;
    }

    public AppTester close() {
        clickView(R.id.close_button);
        return parent;
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
