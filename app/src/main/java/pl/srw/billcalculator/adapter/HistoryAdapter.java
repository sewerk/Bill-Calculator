package pl.srw.billcalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.dao.query.LazyList;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.HistoryActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.util.MultiSelect;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {

    private HistoryActivity activity;
    private LazyList<History> lazyList;
    private MultiSelect<Integer, Bill> selection = new MultiSelect<>();

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
        holder.bindEntry(historyItem, position);
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
        for (Bill bill : selection.getItems())
            Database.getSession().delete(bill);
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
}
