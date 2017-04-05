package pl.srw.billcalculator.tester;

import android.support.annotation.StringRes;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.widget.DatePicker;
import android.widget.EditText;

import org.hamcrest.Matchers;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

public class FormTester extends Tester {

    private AppTester parent;
    private BillTester billTester = new BillTester(parent);
    private ProviderSettingsTester<FormTester> settingsTester = new ProviderSettingsTester<>(this);

    FormTester(AppTester parent) {
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

    public FormTester pressImeActionButton() {
        onView(allOf(hasFocus(), isAssignableFrom(EditText.class))).perform(ViewActions.pressImeActionButton());
        return this;
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

    public FieldView readingFromView() {
        return new FieldView(R.id.form_entry_reading_from_input);
    }

    public FieldView readingFromErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_from);
    }

    public FieldView readingToView() {
        return new FieldView(R.id.form_entry_reading_to_input);
    }

    public FieldView readingToErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_to);
    }

    public FieldView readingDayFromView() {
        return new FieldView(R.id.form_entry_reading_day_from_input);
    }

    public FieldView readingDayFromErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_day_from);
    }

    public FieldView readingDayToView() {
        return new FieldView(R.id.form_entry_reading_day_to_input);
    }

    public FieldView readingDayToErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_day_to);
    }

    public FieldView readingNightFromView() {
        return new FieldView(R.id.form_entry_reading_night_from_input);
    }

    public FieldView readingNightFromErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_night_from);
    }

    public FieldView readingNightToView() {
        return new FieldView(R.id.form_entry_reading_night_to_input);
    }

    public FieldView readingNightToErrorView() {
        return errorFieldViewForReading(R.id.form_entry_reading_night_to);
    }

    public FieldView readingUnitView() {
        return new FieldView(R.id.form_entry_reading_unit);
    }

    public FieldView tariffView() {
        return new FieldView(R.id.form_entry_tariff);
    }

    public FieldView dateFromView() {
        return new FieldView(R.id.form_entry_dates_from);
    }

    public FieldView dateToErrorView() {
        return new FieldView(R.id.date_to_error);
    }

    public ProviderSettingsTester<FormTester> openProviderSettings() {
        clickView(R.id.form_settings_link);
        return settingsTester;
    }

    public AppTester close() {
        clickView(R.id.close_button);
        return parent;
    }

    private FieldView errorFieldViewForReading(int id) {
        return new FieldView(
                onView(allOf(withParent(withParent(withId(id))), withId(R.id.textinput_error))));
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

    public class FieldView {

        private ViewInteraction onView;

        private FieldView(ViewInteraction onView) {
            this.onView = onView;
        }

        private FieldView(int id) {
            onView = onView(withId(id));
        }

        public FormTester hasText(String text) {
            onView.check(matches(withText(text)));
            return FormTester.this;
        }

        public FormTester hasFocus() {
            onView.check(matches(ViewMatchers.hasFocus()));
            return FormTester.this;
        }

        public FormTester hasText(@StringRes int id) {
            onView.check(matches(withText(id)));
            return FormTester.this;
        }
    }
}
