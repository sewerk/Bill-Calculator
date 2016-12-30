package pl.srw.billcalculator.history.list.item;

import pl.srw.billcalculator.db.Bill;

public interface HistoryItemClickListener {

    void onListItemClicked(Bill bill);
}
