package pl.srw.billcalculator.settings.details

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import pl.srw.billcalculator.R
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.type.Provider

private const val ARG_PROVIDER = "settings.provider.arg.provider"

class SettingsDetailsController(bundle: Bundle) : Controller(bundle) {

    companion object {
        fun createFor(provider: Provider): SettingsDetailsController {
            val bundle = Bundle()
            bundle.putSerializable(ARG_PROVIDER, provider)
            return SettingsDetailsController(bundle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        Dependencies.inject(this)
        val view = inflater.inflate(R.layout.settings_details, container, false) as RecyclerView
        val provider: Provider = args.getSerializable(ARG_PROVIDER) as Provider
//        onViewBound(view, provider)
        return view
    }
}
