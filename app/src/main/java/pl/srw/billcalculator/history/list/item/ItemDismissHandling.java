package pl.srw.billcalculator.history.list.item;

import android.support.v7.widget.RecyclerView;

/**
 * Created by kseweryn on 10.08.15.
 */
public interface ItemDismissHandling {

    void onItemDismiss(int position, RecyclerView.ViewHolder viewHolder);

    void undoDismiss(int position);
}
