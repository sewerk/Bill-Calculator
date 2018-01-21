package pl.srw.billcalculator.settings.list

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import pl.srw.billcalculator.databinding.SettingsListItemBinding

class SettingsListAdapter(private val items: List<SettingsListItem> = emptyList()) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val binding = if (view == null) {
            val inflater = LayoutInflater.from(viewGroup.context)
            SettingsListItemBinding.inflate(inflater, viewGroup, false)
        } else DataBindingUtil.getBinding(view)

        with(binding) {
            item = getItem(position)
            executePendingBindings()
        }
        return binding.root
    }

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = items.size
}
