package pl.srw.billcalculator.history.list.item

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class HistoryItemTouchCallback(private val handler: HistoryItemDismissHandling) : ItemTouchHelper.Callback() {

    var swipeEnabled = true

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = swipeEnabled

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return ItemTouchHelper.Callback.makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val bill = (viewHolder as HistoryItemViewHolder).bill
        handler.onListItemDismissed(viewHolder.getAdapterPosition(), bill)
    }
}