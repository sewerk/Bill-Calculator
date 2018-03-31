package pl.srw.billcalculator.tester.rule

import android.app.Activity
import android.os.Build
import android.os.ParcelFileDescriptor
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.rule.IntentsTestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Test rule with permission granting before test execution,
 * activity closing after test execution and intent testing
 * @param <T> tested activity
 */
class PermissionsIntentsTestRule<T : Activity>(clazz: Class<T>,
                                               initialTouchMode: Boolean = false,
                                               launchActivity: Boolean = true,
                                               vararg val permissions: String)
    : IntentsTestRule<T>(clazz, initialTouchMode, launchActivity) {

    private val closingHelper = ClosingActivitiesRuleHelper()

    override fun apply(base: Statement, description: Description): Statement {
        try {
            return super.apply(base, description)
        } finally {
            closingHelper.closeAllActivities()
        }
    }

    override fun beforeActivityLaunched() {
        allowPermissions()
    }

    private fun allowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                var command: ParcelFileDescriptor? = null
                try {
                     command = InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(
                        "pm grant " + InstrumentationRegistry.getTargetContext().packageName + " " + permission
                    )
                } finally {
                    command?.close()
                }

            }
        }
    }
}
