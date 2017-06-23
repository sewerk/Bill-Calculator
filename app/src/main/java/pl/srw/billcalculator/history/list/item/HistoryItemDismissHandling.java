package pl.srw.billcalculator.history.list.item;

import pl.srw.billcalculator.db.Bill;

public interface HistoryItemDismissHandling {

    void onListItemDismissed(int position, Bill bill);
}
