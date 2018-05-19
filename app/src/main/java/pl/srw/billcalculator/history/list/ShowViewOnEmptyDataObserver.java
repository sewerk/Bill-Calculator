package pl.srw.billcalculator.history.list;

import android.view.View;

import java.util.Collection;

public class ShowViewOnEmptyDataObserver {

    private final View view;

    public ShowViewOnEmptyDataObserver(View emptyHistoryView) {
        view = emptyHistoryView;
    }

    public void onChanged(Collection data) {
        if (data.isEmpty()) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }
}
