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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.form.fragment.HasTitle;
import pl.srw.billcalculator.history.list.HistoryAdapter;

/**
 * Created by kseweryn on 09.07.15.
 */
public class MyBillsFragment extends Fragment implements HasTitle {

    @InjectView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.bill_list) RecyclerView rcList;
    @InjectView(R.id.add_new_bill) FloatingActionButton bNewBill;

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
        rcList.setAdapter(new HistoryAdapter(getActivity()));
        bNewBill.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //TODO
                Snackbar.make(coordinatorLayout, "Snakkkkiiii", Snackbar.LENGTH_LONG)
//                        .setAction(R.string.snackbar_action, myOnClickListener)
                        .show();
                return true;
            }
        });
        toolbar.setTitle(getTitle());
        toolbarLayout.setTitle(toolbar.getTitle());
    }

    @Override
    public int getTitle() {
        return R.string.my_bills;
    }
}
