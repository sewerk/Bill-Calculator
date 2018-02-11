package pl.srw.billcalculator.settings

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.BackableActivity
import pl.srw.billcalculator.databinding.SettingsActivityBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.settings.list.SettingsController
import pl.srw.billcalculator.type.Provider
import timber.log.Timber

private const val ARG_PROVIDER = "settings.provider"

class SettingsActivity : BackableActivity() {

    companion object {
        @JvmStatic fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java))
        }

        @JvmStatic fun start(context: Context, provider: Provider) {
            val intent = Intent(context, SettingsActivity::class.java)
            intent.putExtra(ARG_PROVIDER, provider)
            context.startActivity(intent)
        }
    }

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<SettingsActivityBinding>(this, R.layout.settings_activity)
        val provider = intent.getSerializableExtra(ARG_PROVIDER) as Provider?

        router = Conductor.attachRouter(this, binding.container, savedInstanceState)
        if (!router.hasRootController()) {
            val controller = if (provider != null)
                SettingsDetailsController.createFor(provider)
            else
                SettingsController()
            router.setRoot(RouterTransaction.with(controller))
        }
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
