package pl.srw.billcalculator.history.list.item;

import android.view.View;

import pl.srw.billcalculator.db.Bill;

/**
 * Represents view item from history list
 */
public interface HistoryViewItem {

    Bill getBill();

    int getPositionOnList();

    View getView();

    void select();

    void deselect();
}