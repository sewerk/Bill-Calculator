package pl.srw.billcalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.dao.query.LazyList;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistroryItemViewHolder> {

    protected boolean dataValid;
    protected LazyList<History> lazyList;

    public HistoryAdapter(LazyList<History> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
    }

    @Override
    public HistroryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);

        return new HistroryItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HistroryItemViewHolder holder, final int position) {
        History historyItem = lazyList.get(position);
        holder.bindEntry(historyItem);
    }

    @Override
    public int getItemCount() {
        if (dataValid && lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

}
