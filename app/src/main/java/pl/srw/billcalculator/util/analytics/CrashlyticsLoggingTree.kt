package pl.srw.billcalculator.util.analytics

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Release logging tree which will send logs to Crashlytics
 */
class CrashlyticsLoggingTree : Timber.Tree() {
    override fun isLoggable(tag: String?, priority: Int) = priority >= Log.INFO

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Crashlytics.log(message)
        if (priority >= Log.WARN) {
            if (t != null) {
                Crashlytics.logException(t)
            }
            Analytics.warning(message)
        }
    }
}
