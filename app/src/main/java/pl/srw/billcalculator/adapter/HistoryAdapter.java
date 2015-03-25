package pl.srw.billcalculator.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.dao.query.LazyList;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.HistoryActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.MultiSelect;
import pl.srw.billcalculator.util.SelectedBill;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {

    public static final String STATE_SELECTION = "SELECTION";
    private HistoryActivity activity;
    private LazyList<History> lazyList;
    private MultiSelect<Integer, SelectedBill> selection = new MultiSelect<>();

    public HistoryAdapter(HistoryActivity activity) {
        this.activity = activity;
        loadListFromDB();
    }

    private void loadListFromDB() {
        this.lazyList = Database.getHistory();
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);

        return new HistoryItemViewHolder(this, selection, itemView);
    }

    @Override
    public void onBindViewHolder(final HistoryItemViewHolder holder, final int position) {
        History historyItem = lazyList.get(position);
        holder.bindEntry(historyItem);
    }

    @Override
    public int getItemCount() {
        if (lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

    public HistoryActivity getActivity() {
        return activity;
    }

    @DebugLog
    public void deleteSelected() {
        for (SelectedBill bill : selection.getItems())
            Database.deleteBillWithPrices(bill.getType(), bill.getBillId(), bill.getPricesId());
        loadListFromDB();

        for (Integer position : selection.getPositionsReverseOrder())
            notifyItemRemoved(position);

        selection.deselectAll();
    }

    public void exitSelectMode() {
        if (selection.isAnySelected()) {
            selection.deselectAll();
            notifyDataSetChanged();
        }
    }

    public void onRestoreInstanceState(final Bundle savedInstanceState) {
        selection = savedInstanceState.getParcelable(STATE_SELECTION);
    }

    public void onSaveInstanceState(final Bundle outState) {
        outState.putParcelable(STATE_SELECTION, selection);
    }
}
