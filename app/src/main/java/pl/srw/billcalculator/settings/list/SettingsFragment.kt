package pl.srw.billcalculator.settings.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.srw.billcalculator.R
import pl.srw.billcalculator.databinding.SettingsBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.SettingsActivity
import pl.srw.billcalculator.settings.details.SettingsDetailsFragment
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.ContentType
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject lateinit var vmFactory: SettingsVMFactory

    private val viewModel: SettingsVM by lazy { ViewModelProviders.of(activity!!, vmFactory).get(SettingsVM::class.java) }
    private lateinit var binding: SettingsBinding
    private var initialTabSwitch = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Dependencies.inject(this)
        binding = SettingsBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            settingsList.adapter = SettingsListAdapter(viewModel.items)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(viewModel) {
            switchProviderTab.observe(this@SettingsFragment, switchSettingsDetailsFor)
            openProviderSettings.observe(this@SettingsFragment, showProviderSettingsScreen)
            isOnTablet = binding.prefsFrame != null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(viewModel) {
            switchProviderTab.removeObservers(this@SettingsFragment)
            openProviderSettings.removeObservers(this@SettingsFragment)
        }
    }

    private val showProviderSettingsScreen = Observer<Provider> { provider: Provider? ->
        Analytics.contentView(ContentType.SETTINGS, "settings from", "Settings phone", "settings for", provider)
        val detailsFragment = SettingsDetailsFragment.createFor(provider!!)
        (activity as SettingsActivity).replaceFragment(detailsFragment)
    }

    private val switchSettingsDetailsFor = Observer<Provider> { provider: Provider? ->
        Analytics.contentView(ContentType.SETTINGS, "settings from", "Settings tablet", "settings for", provider)
        val detailsFragment = SettingsDetailsFragment.createFor(provider!!)
        switchFragment(detailsFragment)
        binding.settingsList.setItemChecked(binding.vm!!.selectedIndex, true)
    }

    private fun switchFragment(detailsFragment: SettingsDetailsFragment) {
        val transaction = childFragmentManager.beginTransaction()

        if (initialTabSwitch) initialTabSwitch = false
        else transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.fade_out)

        transaction
            .replace(R.id.prefs_frame, detailsFragment)
            .commit()
    }
}
