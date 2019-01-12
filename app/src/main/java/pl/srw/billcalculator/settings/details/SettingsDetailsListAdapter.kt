package pl.srw.billcalculator.settings.details

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.list.DataBindingAdapter
import pl.srw.billcalculator.databinding.SettingsDetailsItemBinding
import pl.srw.billcalculator.settings.details.SettingsDetailsListItemTextExtractor.getSummary
import pl.srw.billcalculator.settings.details.SettingsDetailsListItemTextExtractor.getTitle

class SettingsDetailsListAdapter(private val clickVisitor: SettingsDetailsItemClickVisitor)
    : DataBindingAdapter<SettingsDetailsListItem, SettingsDetailsItemBinding>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): SettingsDetailsItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.settings_details_item, parent, false)

    override fun bind(binding: SettingsDetailsItemBinding, item: SettingsDetailsListItem) {
        val context = binding.root.context
        binding.titleValue = getTitle(context, item)
        binding.summaryValue = getSummary(context, item)
        binding.settingsDetailsItem.setOnClickListener { item.visit(clickVisitor) }
    }

    override fun areItemsTheSame(oldItem: SettingsDetailsListItem, newItem: SettingsDetailsListItem) = when (oldItem) {
        is InputSettingsDetailsListItem -> newItem is InputSettingsDetailsListItem && oldItem.title == newItem.title
        is PickingSettingsDetailsListItem -> newItem is PickingSettingsDetailsListItem && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: SettingsDetailsListItem, newItem: SettingsDetailsListItem) = when (oldItem) {
        is InputSettingsDetailsListItem -> oldItem.value == (newItem as InputSettingsDetailsListItem).value &&
                oldItem.measure == newItem.measure && oldItem.enabled == newItem.enabled
        is PickingSettingsDetailsListItem -> oldItem.value == (newItem as PickingSettingsDetailsListItem).value
    }
}
