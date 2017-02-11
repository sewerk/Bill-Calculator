package pl.srw.billcalculator.form.view;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.tester.FormTester;
import pl.srw.billcalculator.type.Provider;

@RunWith(AndroidJUnit4.class)
public class DatePickingViewAndroidTest {

    @Rule
    public ActivityTestRule<DrawerActivity> mActivityTestRule = new ActivityTestRule<>(DrawerActivity.class);

    private FormTester tester = new FormTester();

    @Test
    public void clickingOnDatePickingViewOpensDialogWithPresetDate() throws Exception {
        LocalDate now = LocalDate.now();
        String monthPrefix = now.getMonthValue() < 10 ? "0" : "";

        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG)
                .openDateFromPicker()
                .acceptDate();

        tester.hasDateFromText("01/" + monthPrefix + now.getMonthValue() + "/" + now.getYear());
    }

    @Test
    public void pickingDateSetsTextOnDatePickingView() throws Exception {
        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG)
                .openDateFromPicker()
                .pickDate(15, Month.JANUARY, 2017)
                .acceptDate();

        tester.hasDateFromText("15/01/2017");
    }

    @Test
    public void pickingOlderDateInToDateViewShowsError() throws Exception {
        tester
                .skipCheckPricesDialogIfVisible()
                .openForm(Provider.PGNIG)
                .putIntoReadingFrom("1")
                .putIntoReadingTo("2")
                .openDateFromPicker()
                .pickDate(15, Month.JANUARY, 2017)
                .acceptDate()
                .openDateToPicker()
                .pickDate(1, Month.JANUARY, 2017)
                .acceptDate()
                .calculate();

        tester.hasDateToError();
    }
}