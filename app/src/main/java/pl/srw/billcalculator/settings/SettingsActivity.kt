package pl.srw.billcalculator.settings

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.BackableActivity
import pl.srw.billcalculator.databinding.SettingsActivityBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.list.SettingsController
import timber.log.Timber

class SettingsActivity : BackableActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SettingsActivityBinding>(this, R.layout.settings_activity)

        router = Conductor.attachRouter(this, binding.container, savedInstanceState)
        if (!router.hasRootController()) router.setRoot(RouterTransaction.with(SettingsController()))
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Dependencies.releaseSettingsComponent()
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        } else {
            Timber.i("Back pressed")
        }
    }
}
