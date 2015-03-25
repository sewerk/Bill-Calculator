package pl.srw.billcalculator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.HistoryActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.testutils.HistoryGenerator;
import pl.srw.billcalculator.testutils.SoloHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
public class HistoryUITest extends ActivityInstrumentationTestCase2<HistoryActivity> {

    public HistoryUITest() {
        super(HistoryActivity.class);
    }

    private Solo solo;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        GeneralPreferences.markFirstLaunch();
        HistoryGenerator.clear();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Test
    public void shouldRemoveBillWithPricesFromDb() throws InterruptedException {
        // given: one bill in history
        HistoryGenerator.generatePgeG11Bills(1);
        SoloHelper.redrawActivity(solo);

        // when: deleting one bill
        solo.clickLongOnText("1 - 11");
        solo.clickOnView(solo.getView(R.id.action_delete));

        // then: no bill is history is available
        assertTrue(solo.searchText(SoloHelper.getString(solo, R.string.empty_history)));
        // and no bill and prices in database is available
        assertTrue(Database.getSession().getPgeG11BillDao().loadAll().isEmpty());
        assertTrue(Database.getSession().getPgePricesDao().loadAll().isEmpty());
    }

    @Test
    public void shouldUnselectAfterDeletion() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        // given: list contain 5 entries
        HistoryGenerator.generatePgeG11Bills(5);
        SoloHelper.redrawActivity(solo);

        // when: select second entry and delete
        solo.clickLongOnText("4 - 14");
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.selected));
        solo.clickOnView(solo.getView(R.id.action_delete));
        // and select second and third entry and delete
        solo.clickLongOnText("3 - 13");
        solo.clickOnText("2 - 12");
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.selected));
        solo.clickOnView(solo.getView(R.id.action_delete));

        // then: none item is selected
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.pge));
    }

    @Test
    public void shouldUselectAfterDone() throws InterruptedException {
        // given: list contain 3 entries
        HistoryGenerator.generatePgeG11Bills(3);
        SoloHelper.redrawActivity(solo);

        // when: selecting 1st and 3rd entry
        solo.clickLongOnText("1 - 11");
        solo.clickOnText("3 - 13");

        // and clicking done
        solo.clickOnImage(0);

        // then: none entry is selected
        assertThat(SoloHelper.getDrawableFromRow(solo, 0), is(R.drawable.pge));
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.pge));
        assertThat(SoloHelper.getDrawableFromRow(solo, 2), is(R.drawable.pge));
    }

    @Test
    public void shouldRestoreSelectionOnScreenRotation() throws InterruptedException {
        // given: one item is selected
        HistoryGenerator.generatePgeG11Bills(3);
        SoloHelper.redrawActivity(solo);
        solo.clickLongOnText("2 - 12");
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.selected));

        // when: screen orientation change
        solo.setActivityOrientation(Solo.LANDSCAPE);

        // then: select mode is active and item is selected
        assertNotNull(solo.getView(R.id.action_delete));
        assertThat(SoloHelper.getDrawableFromRow(solo, 1), is(R.drawable.selected));
    }
}
