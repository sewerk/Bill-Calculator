package pl.srw.billcalculator.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Condition;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.srw.billcalculator.HistoryActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.preference.GeneralPreferences;
import pl.srw.billcalculator.testutils.TestHistoryGenerator;
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
        Database.getSession().deleteAll(PgeG11Bill.class);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Test
    public void shouldNotifyRemoveToUpdateList() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        // when: list contain 5 entries
        new TestHistoryGenerator().generatePgeG11Bills(5);
        redrawActivity();
        assertThat(SoloHelper.isVisible(solo, R.drawable.selected), is(false));
        // given: select second entry and delete
        solo.clickLongOnText("4 - 14");
        solo.clickOnView(solo.getView(R.id.action_delete));
        // and select second and third entry and delete
        solo.clickLongOnText("3 - 13");
        solo.clickOnText("2 - 12");
        solo.clickOnView(solo.getView(R.id.action_delete));

        // then: none item is selected
        assertThat(SoloHelper.isVisible(solo, R.drawable.selected), is(false));
    }

    private void redrawActivity() {
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
    }

}
