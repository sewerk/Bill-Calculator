package pl.srw.billcalculator.test.bill.service;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by kseweryn on 29.04.15.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class TauronBillStoringServiceTest {

    @Rule public final ServiceTestRule serviceTestRule = new ServiceTestRule();

    @Test
    public void shouldStoreBillInHistory() {
        //FIXME: implement
        //serviceTestRule.startService(new Intent(InstrumentationRegistry.getTargetContext(), TauronBillStoringService.class));
    }
}
