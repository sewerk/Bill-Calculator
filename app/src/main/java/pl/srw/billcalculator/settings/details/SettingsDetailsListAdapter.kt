package pl.srw.billcalculator.settings.details

import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.srw.billcalculator.R
import pl.srw.billcalculator.common.list.DataBindingAdapter
import pl.srw.billcalculator.databinding.SettingsDetailsItemBinding
import pl.srw.billcalculator.settings.details.SettingsDetailsListItemTextExtractor.getSummary
import pl.srw.billcalculator.settings.details.SettingsDetailsListItemTextExtractor.getTitle

class SettingsDetailsListAdapter(items : List<SettingsDetailsListItem>,
                                 private val clickVisitor: SettingsDetailsItemClickVisitor)
    : DataBindingAdapter<SettingsDetailsListItem, SettingsDetailsItemBinding>(items) {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): SettingsDetailsItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.settings_details_item, parent, false)

    override fun bind(binding: SettingsDetailsItemBinding, item: SettingsDetailsListItem) {
        val context = binding.root.context
        binding.titleValue = getTitle(context, item)
        binding.summaryValue = getSummary(context, item)
        binding.settingsDetailsItem.setOnClickListener { item.visit(clickVisitor) }
    }
}
