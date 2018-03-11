package pl.srw.billcalculator.settings.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.bundleOf
import pl.srw.billcalculator.databinding.SettingsDetailsBinding
import pl.srw.billcalculator.di.Dependencies
import pl.srw.billcalculator.settings.details.restore.ConfirmRestoreSettingsDialogFragment
import pl.srw.billcalculator.settings.help.ProviderSettingsHelpActivity
import pl.srw.billcalculator.type.Provider
import timber.log.Timber
import javax.inject.Inject

private const val ARG_PROVIDER = "settings.provider.arg.provider"

class SettingsDetailsFragment : Fragment() {

    companion object {
        fun createFor(provider: Provider) = SettingsDetailsFragment().apply {
            arguments = bundleOf(ARG_PROVIDER, provider)
        }
    }

    @Inject lateinit var vmFactory: SettingsDetailsVMFactory

    private lateinit var binding: SettingsDetailsBinding
    private val provider by lazy { arguments!!.getSerializable(ARG_PROVIDER) as Provider }
    private val viewModel: SettingsDetailsVM by lazy { ViewModelProviders.of(activity!!, vmFactory).get(SettingsDetailsVM::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.inject(this)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SettingsDetailsBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            vm!!.listItemsFor(provider)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupList()
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings, menu)
    }

    @SuppressWarnings("ReturnCount")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_help) { // FIXME: move to presenter
            showHelp()
            return true
        } else if (item.itemId == R.id.action_default) {
            Timber.i("Restore prices clicked for %s", provider)
            ConfirmRestoreSettingsDialogFragment.newInstance(provider)
                .show(activity!!.supportFragmentManager, null)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupList() {
        val listView = binding.settingsDetailsList
        val layoutManager = LinearLayoutManager(activity)
        listView.layoutManager = layoutManager
        listView.addItemDecoration(DividerItemDecoration(activity, layoutManager.orientation))
        listView.adapter = SettingsDetailsListAdapter(SettingsDetailsItemClickVisitor(activity!!))
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).supportActionBar!!.setTitle(provider.settingsTitleRes)
    }

    private fun showHelp() {
        val intent = ProviderSettingsHelpActivity.createIntent(getActivity(), provider)
        startActivity(intent)
    }
}
