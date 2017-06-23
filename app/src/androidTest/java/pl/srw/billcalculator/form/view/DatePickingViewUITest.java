package pl.srw.billcalculator.form.view;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.type.Provider;

public class DatePickingViewUITest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity.class);

    private AppTester tester = new AppTester();

    @Test
    public void clickingOnDatePickingViewOpensDialogWithPresetDate() throws Exception {
        LocalDate now = LocalDate.now();
        String monthPrefix = now.getMonthValue() < 10 ? "0" : "";

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG)
                .openDateFromPicker()
                .acceptDate()

                .dateFromView().hasText("01." + monthPrefix + now.getMonthValue() + "." + now.getYear() % 100);
    }

    @Test
    public void pickingDateSetsTextOnDatePickingView() throws Exception {
        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG)
                .openDateFromPicker()
                .pickDate(15, Month.JANUARY, 2017)
                .acceptDate()

                .dateFromView().hasText("15.01.17");
    }

    @Test
    public void pickingOlderDateInToDateViewShowsError() throws Exception {
        FormTester formTester = tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG);

        formTester
                .putIntoReadingFrom("1")
                .putIntoReadingTo("2")
                .openDateFromPicker()
                .pickDate(15, Month.JANUARY, 2017)
                .acceptDate()
                .openDateToPicker()
                .pickDate(1, Month.JANUARY, 2017)
                .acceptDate()
                .calculate();

        formTester.dateToErrorView().hasText(R.string.date_error);
    }
}