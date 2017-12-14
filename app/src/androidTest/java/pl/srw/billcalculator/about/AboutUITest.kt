package pl.srw.billcalculator.about

import android.app.Activity
import android.app.Instrumentation
import android.support.test.espresso.intent.Intents.intending
import android.support.test.espresso.intent.matcher.IntentMatchers.isInternal
import android.support.test.espresso.intent.rule.IntentsTestRule
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.srw.billcalculator.R
import pl.srw.billcalculator.history.DrawerActivity
import pl.srw.billcalculator.tester.AppTester

class AboutUITest {

    @Rule @JvmField
    val testRule = IntentsTestRule(DrawerActivity::class.java)

    val tester = AppTester()

    @Before
    fun setUp() {
        intending(not(isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun shouldOpenMailAppWhenLinkClicked() {
        tester.skipCheckPricesDialogIfVisible()
                .openAbout()
                .clickMailLink()
                .checkMailIntentSend("kalkulator.rachunkow@gmail.com",
                        testRule.activity.getString(R.string.app_name))
    }

    @Test
    fun shouldOpenGPlusWhenLinkClicked() {
        tester.skipCheckPricesDialogIfVisible()
                .openAbout()
                .clickGPlusLink()
                .checkGPlusIntentSend("https://plus.google.com/communities/113263640175495853700")
    }
}
