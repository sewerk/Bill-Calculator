package pl.srw.billcalculator.history.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kamil Seweryn.
 */
public class EmptyHistoryDataObserver {

    private final View view;

    public EmptyHistoryDataObserver(View emptyHistoryView) {
        view = emptyHistoryView;
    }

    public void onChanged(RecyclerView.Adapter adapter) {
        if (adapter.getItemCount() == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
