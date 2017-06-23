package pl.srw.billcalculator.history.list.item;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import pl.srw.billcalculator.db.Bill;

public class HistoryItemTouchCallback extends ItemTouchHelper.Callback {

    private final HistoryItemDismissHandling handler;

    public HistoryItemTouchCallback(HistoryItemDismissHandling handler) {
        this.handler = handler;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final Bill bill = ((HistoryItemViewHolder) viewHolder).getBill();
        handler.onListItemDismissed(viewHolder.getAdapterPosition(), bill);
    }
}