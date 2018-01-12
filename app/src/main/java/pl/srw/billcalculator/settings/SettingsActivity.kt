package pl.srw.billcalculator.settings

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import pl.srw.billcalculator.R
import pl.srw.billcalculator.databinding.SettingsActivityBinding
import pl.srw.billcalculator.di.Dependencies
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val binding = DataBindingUtil.setContentView<SettingsActivityBinding>(this, R.layout.settings_activity)

        router = Conductor.attachRouter(this, binding.container, savedInstanceState)
        if (!router.hasRootController()) router.setRoot(RouterTransaction.with(SettingsController()))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Dependencies.releaseSettingsComponent()
    }

    override fun onBackPressed() {
        Timber.i("Back pressed")
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
