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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {

    private final ShowViewOnEmptyDataObserver dataChangeObserver;
    private final HistoryItemClickListener clickListener;
    private LazyList<History> lazyList;

    public HistoryAdapter(ShowViewOnEmptyDataObserver showViewOnEmptyDataObserver, HistoryItemClickListener clickListener) {
        this.dataChangeObserver = showViewOnEmptyDataObserver;
        this.clickListener = clickListener;
    }

    public void setData(LazyList<History> data) {
        if (lazyList != null)
            lazyList.close();
        lazyList = data;
        dataChangeObserver.onChanged(this);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_drawer_my_bills_item, parent, false);
        return new HistoryItemViewHolder(itemView, clickListener);
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
}
