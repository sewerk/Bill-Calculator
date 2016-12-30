package pl.srw.billcalculator.history.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ShowViewOnEmptyDataObserver {

    private final View view;

    public ShowViewOnEmptyDataObserver(View emptyHistoryView) {
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
