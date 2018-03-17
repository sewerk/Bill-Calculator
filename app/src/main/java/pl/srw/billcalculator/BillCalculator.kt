package pl.srw.billcalculator

import android.app.Application
import android.os.StrictMode
import android.support.v7.app.AppCompatDelegate
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.settings.prices.PgePrices
import pl.srw.billcalculator.settings.prices.PgnigPrices
import pl.srw.billcalculator.settings.prices.TauronPrices
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.CrashlyticsLoggingTree
import timber.log.Timber
import javax.inject.Inject

class BillCalculator : Application() {

    @Inject lateinit var pgePrices: PgePrices
    @Inject lateinit var pgnigPrices: PgnigPrices
    @Inject lateinit var tauronPrices: TauronPrices

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Database.enableDatabaseLogging()
        } else {
            Analytics.enable(this)
            Timber.plant(CrashlyticsLoggingTree())
        }
        Dependencies.init(applicationContext)
        Dependencies.inject(this)

        AndroidThreeTen.init(this)
        Database.initialize(this)

        pgePrices.setDefaultIfNotSet()
        pgnigPrices.setDefaultIfNotSet()
        tauronPrices.setDefaultIfNotSet()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        enableStrictMode()
    }

    private fun enableStrictMode() {
        if (!BuildConfig.DEBUG) return

        val threadPolicy = StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeath()
            .build()
        val vmPolicy = StrictMode.VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .penaltyDeath()
            .build()
        StrictMode.setThreadPolicy(threadPolicy)
        StrictMode.setVmPolicy(vmPolicy)
    }
}
