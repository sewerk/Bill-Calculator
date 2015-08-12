package pl.srw.billcalculator.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.form.fragment.HasTitle;
import pl.srw.billcalculator.history.list.EmptyHistoryDataObserver;
import pl.srw.billcalculator.history.list.HistoryAdapter;
import pl.srw.billcalculator.history.list.item.ItemTouchCallback;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.type.ContentType;

/**
 * Created by kseweryn on 09.07.15.
 */
public class MyBillsFragment extends Fragment implements HasTitle {

    @InjectView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.bill_list) RecyclerView rcList;
    @InjectView(R.id.tv_emptyHistory) TextView tvEmptyHistory;
    @InjectView(R.id.add_new_bill) FloatingActionButton bNewBill;
    private HistoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.my_bills, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcList.setHasFixedSize(true);
        rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(this, new EmptyHistoryDataObserver(tvEmptyHistory));
        rcList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchCallback(adapter));
        helper.attachToRecyclerView(rcList);

        AnalyticsWrapper.logContent(ContentType.HISTORY, "history size", String.valueOf(adapter.getItemCount()));

        toolbar.setTitle(getTitle());
        toolbarLayout.setTitle(toolbar.getTitle());
    }

    @OnClick(R.id.add_new_bill)
    public void onNewBillButtonClicked() {
        final pl.srw.billcalculator.db.PgePrices prices = new PgePrices().convertToDb();//TODO: remove
        Database.getSession().insert(prices);
        Database.getSession().insert(new PgeG11Bill(null, 1, 2, new Date(), new Date(), 666.77, prices.getId()));
        adapter.notifyDataSetChanged();
    }

    public void makeUndoDeleteSnackbar(final int position) {
        Snackbar.make(coordinatorLayout, getActivity().getString(R.string.bill_deleted), Snackbar.LENGTH_LONG)
            .setAction(R.string.action_undo_delete, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.undoDismiss(position);
                    rcList.smoothScrollToPosition(position);
                }
            })
            .show();
    }

    @Override
    public int getTitle() {
        return R.string.my_bills;
    }
}
