package pl.srw.billcalculator.about

import android.support.test.espresso.intent.rule.IntentsTestRule
import org.junit.Rule
import org.junit.Test
import pl.srw.billcalculator.R
import pl.srw.billcalculator.history.DrawerActivity
import pl.srw.billcalculator.tester.AppTester

class AboutUITest {

    @Rule @JvmField
    val testRule = IntentsTestRule(DrawerActivity::class.java)

    val tester = AppTester()

    @Test
    fun shouldOpenMailAppWhenLinkClicked() {
        tester.openAbout()
                .clickMailLink()
                .checkMailIntentSend("kalkulator.rachunkow@gmail.com",
                        testRule.activity.getString(R.string.app_name))
    }
}