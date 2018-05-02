package pl.srw.billcalculator.history.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import pl.srw.billcalculator.R
import pl.srw.billcalculator.db.History
import pl.srw.billcalculator.history.list.item.HistoryItemClickListener
import pl.srw.billcalculator.history.list.item.HistoryItemViewHolder
import pl.srw.billcalculator.util.BillSelection

class HistoryAdapter(
    private val dataChangeObserver: ShowViewOnEmptyDataObserver,
    private val clickListener: HistoryItemClickListener,
    private val selection: BillSelection
) : ListAdapter<History, HistoryItemViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_drawer_history_item, parent, false)
        return HistoryItemViewHolder(itemView, clickListener)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bindEntry(historyItem, selection.isSelected(position))
    }

    override fun submitList(list: MutableList<History>?) {
        super.submitList(list)
        dataChangeObserver.onChanged(this)
    }
}

private object DiffCallback : DiffUtil.ItemCallback<History>() {
    override fun areItemsTheSame(oldItem: History, newItem: History) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
        return oldItem == newItem
    }

}
