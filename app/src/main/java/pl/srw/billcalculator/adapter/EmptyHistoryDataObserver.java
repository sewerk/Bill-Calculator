package pl.srw.billcalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Kamil Seweryn.
 */
public class EmptyHistoryDataObserver extends RecyclerView.AdapterDataObserver {

    private View view;
    private RecyclerView.Adapter adapter;

    public EmptyHistoryDataObserver(RecyclerView.Adapter adapter, View emptyHistoryView) {
        this.adapter = adapter;
        view = emptyHistoryView;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        if (adapter.getItemCount() == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
