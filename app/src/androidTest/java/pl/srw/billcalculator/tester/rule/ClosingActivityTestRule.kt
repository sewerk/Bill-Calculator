package pl.srw.billcalculator.tester.rule

import android.app.Activity
import android.support.test.rule.ActivityTestRule

import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Custom test runner to make sure previous activities are completed (meaning they go through
 * the entire lifecycle) before starting next test.
 *
 * @param <T>   activity type
 */
class ClosingActivityTestRule<T : Activity> : ActivityTestRule<T> {

    private val closingHelper = ClosingActivitiesRuleHelper()

    constructor(activityClass: Class<T>) : super(activityClass)

    constructor(activityClass: Class<T>, initialTouchMode: Boolean, launchActivity: Boolean) : super(activityClass, initialTouchMode, launchActivity)

    override fun apply(base: Statement, description: Description): Statement {
        try {
            return super.apply(base, description)
        } finally {
            closingHelper.closeAllActivities()
        }
    }
}