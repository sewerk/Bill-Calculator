package pl.srw.billcalculator.tester.rule

import android.app.Activity
import android.app.Instrumentation
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.core.deps.guava.base.Throwables
import android.support.test.espresso.core.deps.guava.collect.Sets
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import timber.log.Timber
import java.util.concurrent.Callable
import java.util.concurrent.atomic.AtomicReference

private val NUMBER_OF_RETRIES = 100

/**
 * Helps test rules to close up all activities (meaning they go through
 * the entire lifecycle) before starting next test.
 *
 * There is a bug in espresso 2.2.2 where previous activity under test does NOT become destroyed
 * until the next activity test is started.
 * https://issuetracker.google.com/issues/37082857
 *
 * Inspired from:
 * https://gist.github.com/patrickhammond/19e584b90d7aae20f8f4
 */
class ClosingActivitiesRuleHelper {

    // See https://code.google.com/p/android-test-kit/issues/detail?id=66
    fun closeAllActivities() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            closeAllActivities(instrumentation)
        } catch (ex: Exception) {
            Timber.e("Could not use close all activities", ex)
        }
    }

    @Throws(Exception::class)
    private fun closeAllActivities(instrumentation: Instrumentation) {
        var i = 0
        while (closeActivity(instrumentation)) {
            if (i++ > NUMBER_OF_RETRIES) {
                throw AssertionError("Limit of retries excesses")
            }
            Thread.sleep(200)
        }
    }

    @Throws(Exception::class)
    private fun closeActivity(instrumentation: Instrumentation): Boolean {
        val activityClosed = callOnMainSync(instrumentation, Callable {
            val activities = getActivitiesInStages(Stage.RESUMED,
                    Stage.STARTED, Stage.PAUSED, Stage.STOPPED, Stage.CREATED)
            activities.removeAll(getActivitiesInStages(Stage.DESTROYED))
            if (activities.size > 0) {
                val activity = activities.iterator().next()
                activity.finish()
                true
            } else false
        })
        if (activityClosed) {
            instrumentation.waitForIdleSync()
        }
        return activityClosed
    }

    @Throws(Exception::class)
    private fun callOnMainSync(instrumentation: Instrumentation, callable: Callable<Boolean>): Boolean {
        val retAtomic = AtomicReference<Boolean>()
        val exceptionAtomic = AtomicReference<Throwable>()
        instrumentation.runOnMainSync {
            try {
                retAtomic.set(callable.call())
            } catch (e: Throwable) {
                exceptionAtomic.set(e)
            }
        }
        val exception = exceptionAtomic.get()
        if (exception != null) {
            Throwables.propagateIfInstanceOf(exception, Exception::class.java)
            Throwables.propagate(exception)
        }
        return retAtomic.get()
    }

    private fun getActivitiesInStages(vararg stages: Stage): MutableSet<Activity> {
        val activities = Sets.newHashSet<Activity>()
        val instance = ActivityLifecycleMonitorRegistry.getInstance()
        stages.mapNotNull { instance.getActivitiesInStage(it) }
                .forEach { activities.addAll(it) }
        return activities
    }
}
