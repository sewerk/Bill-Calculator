package pl.srw.billcalculator.history.list.item;

import android.view.View;

import pl.srw.billcalculator.db.Bill;

public interface HistoryItemClickListener {

    void onListItemClicked(Bill bill, View viewClicked);
}
