package pl.srw.billcalculator.history.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.greendao.query.LazyList;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.list.item.HistoryItemClickListener;
import pl.srw.billcalculator.history.list.item.HistoryItemViewHolder;
import pl.srw.billcalculator.util.BillSelection;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {

    private final ShowViewOnEmptyDataObserver dataChangeObserver;
    private final HistoryItemClickListener clickListener;
    private final BillSelection selection;
    private LazyList<History> lazyList;

    public HistoryAdapter(ShowViewOnEmptyDataObserver showViewOnEmptyDataObserver, HistoryItemClickListener clickListener,
                          BillSelection selection) {
        this.dataChangeObserver = showViewOnEmptyDataObserver;
        this.clickListener = clickListener;
        this.selection = selection;
    }

    public void setData(LazyList<History> data) {
        if (lazyList != null)
            lazyList.close();
        lazyList = data;
        dataChangeObserver.onChanged(this);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_drawer_history_item, parent, false);
        return new HistoryItemViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(final HistoryItemViewHolder holder, final int position) {
        History historyItem = lazyList.get(position);
        holder.bindEntry(historyItem, selection.isSelected(position));
    }

    @Override
    public int getItemCount() {
        if (lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }
}
