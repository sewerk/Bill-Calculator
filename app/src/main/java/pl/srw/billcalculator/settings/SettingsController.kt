package pl.srw.billcalculator.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import pl.srw.billcalculator.databinding.SettingsBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.SettingsDetailsController
import pl.srw.billcalculator.type.Provider
import javax.inject.Inject

class SettingsController : Controller() {

    @Inject internal lateinit var vmFactory: SettingsVMFactory

    private lateinit var binding: SettingsBinding
    private val activity: FragmentActivity
        get() = super.getActivity() as FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Dependencies.inject(this)
        binding = SettingsBinding.inflate(inflater, container, false)
        onViewBound()
        return binding.root
    }

    private fun onViewBound() {
       getViewModel().also {
           binding.vm = it
           binding.list.adapter = SettingsListAdapter(it.items)
       }.apply {
           switchProviderTab.observe(activity, switchSettingsDetailsFor)
           openProviderSettings.observe(activity, showProviderSettingsScreen)
           isOnTablet = binding.prefsFrame != null
       }
    }

    private val switchSettingsDetailsFor = Observer<Provider> { provider: Provider? ->
        val detailsModel = SettingsDetailsController.createFor(provider!!)
        getChildRouter(binding.prefsFrame!!).setRoot(RouterTransaction.with(detailsModel))
        binding.list.setItemChecked(binding.vm!!.selectedIndex, true)
    }

    private val showProviderSettingsScreen = Observer<Provider> { provider: Provider? ->
        val detailsModel = SettingsDetailsController.createFor(provider!!)
        router.pushController(RouterTransaction.with(detailsModel)
                .pushChangeHandler(HorizontalChangeHandler())
                .popChangeHandler(HorizontalChangeHandler()))
    }

    private fun getViewModel() = ViewModelProviders.of(activity, vmFactory).get(SettingsVM::class.java)
}
