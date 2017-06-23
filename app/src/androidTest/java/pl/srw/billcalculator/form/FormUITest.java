package pl.srw.billcalculator.form;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.DrawerActivity;
import pl.srw.billcalculator.tester.AppTester;
import pl.srw.billcalculator.tester.FormTester;

import static pl.srw.billcalculator.type.Provider.PGE;
import static pl.srw.billcalculator.type.Provider.PGNIG;
import static pl.srw.billcalculator.type.Provider.TAURON;

public class FormUITest {

    @Rule
    public ActivityTestRule<DrawerActivity> testRule = new ActivityTestRule<>(DrawerActivity.class);

    private AppTester tester = new AppTester();

    @Before
    public void setUp() throws Exception {
        tester.skipCheckPricesDialogIfVisible();
    }

    @Test
    public void shouldShowReadingUnitAccordingToBillType() {
        tester
                .openForm(PGE)
                .readingUnitView().hasText("[kWh]")
                .close()

                .openForm(PGNIG)
                .readingUnitView().hasText("[m³]")
                .close()

                .openForm(TAURON)
                .readingUnitView().hasText("[kWh]");
    }

    @Test
    public void shouldRestoreStateOnOrientationChange() throws Throwable {
        FormTester formTester = tester.openForm(PGNIG);

        formTester
                .putIntoReadingFrom("45")
                .putIntoReadingTo("123")
                .changeOrientation(testRule);

        formTester
                .readingFromView().hasText("45")
                .readingToView().hasText("123");
    }

    @Test
    public void shouldShowTariffLabelAccordingToPreferences() {
        // given
        given_PgeTariffG12();

                // then
        tester.openForm(PGE)
                .tariffView().hasText("G12")

                // when: switch to G11 in preferences
                .openProviderSettings()
                .getPreferenceAtLine(0)
                .pickOption("Taryfa całodobowa (G11)")
                .close()

                // then
                .tariffView().hasText("G11");
    }

    @Test
    public void shouldChangeFocusOnSoftNextKeyForG11() throws Throwable {
        tester
                .openForm(PGNIG)
                .readingFromView().hasFocus()
                .pressImeActionButton()
                .readingToView().hasFocus();
    }

    @Test
    public void shouldChangeFocusOnSoftNextKeyForG12() throws Throwable {
        // given:
        given_PgeTariffG12();

        tester.openForm(PGE)
                .readingDayFromView().hasFocus()

                .pressImeActionButton()
                .readingDayToView().hasFocus()

                .pressImeActionButton()
                .readingNightFromView().hasFocus()

                .pressImeActionButton()
                .readingNightToView().hasFocus();
    }

    @Test
    public void shouldShowErrorOnEmptyReadingsG11() {
        // given: empty readings
        FormTester formTester = tester.openForm(PGNIG);

        // when
        formTester.calculate();

        // then
        formTester.readingFromErrorView().hasText(R.string.reading_missing);

        // when
        formTester.putIntoReadingFrom("123")
                .calculate();

        // then
        formTester.readingToErrorView().hasText(R.string.reading_missing);
    }

    @Test
    public void shouldShowErrorOnEmptyReadingsG12() {
        // given
        given_PgeTariffG12();
        FormTester formTester = tester.openForm(PGE);

        // when
        formTester.calculate();

        // then
        formTester.readingDayFromErrorView().hasText(R.string.reading_missing);

        // when
        formTester
                .putIntoReadingDayFrom("123")
                .calculate();

        // then
        formTester.readingDayToErrorView().hasText(R.string.reading_missing);

        // when
        formTester
                .putIntoReadingDayTo("124")
                .calculate();

        // then
        formTester.readingNightFromErrorView().hasText(R.string.reading_missing);

        // when
        formTester
                .putIntoReadingNightFrom("123")
                .calculate();

        // then
        formTester.readingNightToErrorView().hasText(R.string.reading_missing);
    }

    @Test
    public void shouldShowErrorOnReadingG11WrongOrder() {
        FormTester formTester = tester.openForm(PGNIG);
        formTester
                .putIntoReadingFrom("234")
                .putIntoReadingTo("123")
                .calculate();

        formTester.readingToErrorView().hasText(R.string.reading_order_error);
    }

    @Test
    public void shouldShowErrorOnReadingG12WrongOrder() {
        given_PgeTariffG12();
        FormTester formTester = tester.openForm(PGE);

        formTester
                .putIntoReadingDayFrom("12")
                .putIntoReadingDayTo("11")
                .putIntoReadingNightFrom("22")
                .putIntoReadingNightTo("21")
                .calculate();

        formTester.readingDayToErrorView().hasText(R.string.reading_order_error);

        formTester
                .putIntoReadingDayTo("13")
                .calculate();

        formTester.readingNightToErrorView().hasText(R.string.reading_order_error);
    }

    private void given_PgeTariffG12() {
        tester
                .openSettings()
                .pickProvider(PGE)
                .getPreferenceAtLine(0)
                .pickOption("Taryfa dwustrefowa (G12)")
                .close()
                .close();
    }
}
