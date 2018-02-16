package pl.srw.billcalculator.settings.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import pl.srw.billcalculator.databinding.SettingsBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.ContentType
import javax.inject.Inject

class SettingsController : LifecycleController() {

    @Inject internal lateinit var vmFactory: SettingsVMFactory

    private val viewModel: SettingsVM by lazy { ViewModelProviders.of(activity, vmFactory).get(SettingsVM::class.java) }
    private lateinit var binding: SettingsBinding
    private val activity: FragmentActivity
        get() = super.getActivity() as FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Dependencies.inject(this)
        binding = SettingsBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            settingsList.adapter = SettingsListAdapter(viewModel.items)
        }
        onViewBound()
        return binding.root
    }

    private fun onViewBound() {
       with(viewModel) {
           switchProviderTab.observe(this@SettingsController, switchSettingsDetailsFor)
           openProviderSettings.observe(this@SettingsController, showProviderSettingsScreen)
           isOnTablet = binding.prefsFrame != null
       }
    }

    override fun onDestroyView(view: View) {
        with(viewModel) {
            switchProviderTab.removeObservers(this@SettingsController)
            openProviderSettings.removeObservers(this@SettingsController)
        }
    }

    private val switchSettingsDetailsFor = Observer<Provider> { provider: Provider? ->
        Analytics.contentView(ContentType.SETTINGS, "settings from", "Settings tablet", "settings for", provider)
        val detailsModel = SettingsDetailsController.createFor(provider!!)
        getChildRouter(binding.prefsFrame!!).setRoot(RouterTransaction.with(detailsModel))
        binding.settingsList.setItemChecked(binding.vm!!.selectedIndex, true)
    }

    private val showProviderSettingsScreen = Observer<Provider> { provider: Provider? ->
        Analytics.contentView(ContentType.SETTINGS, "settings from", "Settings phone", "settings for", provider)
        val detailsModel = SettingsDetailsController.createFor(provider!!)
        router.pushController(RouterTransaction.with(detailsModel)
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }
}
