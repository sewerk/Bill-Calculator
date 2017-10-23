package pl.srw.billcalculator.tester

import android.content.Intent
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import pl.srw.billcalculator.R

internal class AboutTester : Tester() {

    fun clickMailLink(): AboutTester {
        clickText(R.string.emailme)
        return this
    }

    fun clickGPlusLink(): AboutTester {
        clickText(R.string.link_gplus)
        return this
    }

    fun checkGPlusIntentSend(url: String) {
        intended(allOf(
                hasAction(Intent.ACTION_VIEW),
                hasData(url)
        ))
    }

    fun checkMailIntentSend(address: String, title: String) {
        intended(chooser(allOf(
                hasAction(Intent.ACTION_SENDTO),
                hasExtra(Intent.EXTRA_SUBJECT, title),
                hasData("mailto:$address")
                )))
    }

    private fun chooser(matcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(`is`(Intent.EXTRA_INTENT), matcher))
    }
}
