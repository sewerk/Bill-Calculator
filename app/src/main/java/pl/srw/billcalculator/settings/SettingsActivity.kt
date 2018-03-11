package pl.srw.billcalculator.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.BackableActivity
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.SettingsDetailsFragment
import pl.srw.billcalculator.settings.list.SettingsFragment
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

    private val fromFormLink by lazy { intent.hasExtra(ARG_PROVIDER) }
    private val provider by lazy { intent.getSerializableExtra(ARG_PROVIDER) as Provider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        initFragments(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) Dependencies.releaseSettingsComponent()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            Timber.i("Back pressed from provider screen")
            // phone config
            supportActionBar!!.setTitle(R.string.settings_label)
        }
        super.onBackPressed()
    }

    fun replaceFragment(detailsFragment: SettingsDetailsFragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
            .replace(R.id.container, detailsFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun initFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) return

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, getFragment())
            .commit()
    }

    private fun getFragment(): Fragment {
        return if (fromFormLink)
            SettingsDetailsFragment.createFor(provider)
        else
            SettingsFragment()
    }
}
