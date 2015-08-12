package pl.srw.billcalculator.history.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.dao.query.LazyList;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.MyBillsFragment;
import pl.srw.billcalculator.history.list.item.HistoryItemViewHolder;
import pl.srw.billcalculator.history.list.item.ItemDismissHandling;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> implements ItemDismissHandling {

    private final MyBillsFragment view;
    private final EmptyHistoryDataObserver dataChangeObserver;
    private LazyList<History> lazyList;

    public HistoryAdapter(MyBillsFragment myBillsFragment, EmptyHistoryDataObserver emptyHistoryDataObserver) {
        view = myBillsFragment;
        dataChangeObserver = emptyHistoryDataObserver;
        loadListFromDB();
    }

    private void loadListFromDB() {
        if (lazyList != null)
            lazyList.close();
        lazyList = Database.getHistory();
        dataChangeObserver.onChanged(this);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryItemViewHolder(itemView);
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

    @DebugLog
    @Override
    public void onItemDismiss(int position, RecyclerView.ViewHolder viewHolder) {
        final Bill bill = ((HistoryItemViewHolder) viewHolder).getBill();
        Database.deleteBillWithPrices(BillType.valueOf(bill), bill.getId(), bill.getPricesId());
        loadListFromDB();

        notifyItemRemoved(position);
        view.makeUndoDeleteSnackbar(position);
    }

    @DebugLog
    @Override
    public void undoDismiss(int position) {
        Database.undelete();
        loadListFromDB();
        notifyItemInserted(position);
    }
}
