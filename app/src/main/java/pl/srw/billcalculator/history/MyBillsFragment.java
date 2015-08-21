package pl.srw.billcalculator.history;

import android.animation.AnimatorSet;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wnafee.vector.compat.ResourcesCompat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.DrawerHandling;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.history.list.EmptyHistoryDataObserver;
import pl.srw.billcalculator.history.list.HistoryAdapter;
import pl.srw.billcalculator.history.list.item.ItemTouchCallback;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Animations;

/**
 * Created by kseweryn on 09.07.15.
 */
public class MyBillsFragment extends Fragment {

    @InjectView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.bill_list) RecyclerView rcList;
    @InjectView(R.id.tv_emptyHistory) TextView tvEmptyHistory;
    @InjectView(R.id.dim_overlay) RelativeLayout dimLayout;
    @InjectView(R.id.add_new_bill) FloatingActionButton fab;
    @InjectView(R.id.new_bill_pge) FloatingActionButton fabPge;
    @InjectView(R.id.new_bill_pgnig) FloatingActionButton fabPgnig;
    @InjectView(R.id.new_bill_tauron) FloatingActionButton fabTauron;
    private HistoryAdapter adapter;
    private AnimatorSet expandAnimation;
    private AnimatorSet collapseAnimation;

    public static MyBillsFragment newInstance() {
        return new MyBillsFragment();
    }
    
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

        setupToolbar();
        setupList();
        setupFAB();

        AnalyticsWrapper.logContent(ContentType.HISTORY, "history size", String.valueOf(adapter.getItemCount()));
    }

    @Override
    public void onDestroyView() {
        expandAnimation = null;
        collapseAnimation = null;
        super.onDestroyView();
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.my_bills);
        toolbarLayout.setTitle(toolbar.getTitle());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupList() {
        rcList.setHasFixedSize(true);
        rcList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HistoryAdapter(this, new EmptyHistoryDataObserver(tvEmptyHistory));
        rcList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchCallback(adapter));
        helper.attachToRecyclerView(rcList);
    }

    private void setupFAB() {
        Drawable plusDrawable = ResourcesCompat.getDrawable(getActivity(), R.drawable.ic_add_white_24px);
        fab.setImageDrawable(plusDrawable);
        fabPge.setScaleType(ImageView.ScaleType.CENTER_CROP);
        fabPgnig.setScaleType(ImageView.ScaleType.CENTER_CROP);
        fabTauron.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @OnClick(R.id.add_new_bill)
    public void onNewBillButtonClicked() {
        if (!dimLayout.isClickable()) expandFabs();
        else collapseFabs();
    }

    @OnClick(R.id.new_bill_pge)
    public void onFabPgeClicked() {
        ((DrawerHandling) getActivity()).showForm(Provider.PGE);
    }

    @OnClick(R.id.new_bill_pgnig)
    public void onFabPgnigClicked() {
        ((DrawerHandling) getActivity()).showForm(Provider.PGNIG);
    }

    @OnClick(R.id.new_bill_tauron)
    public void onFabTauronClicked() {
        ((DrawerHandling) getActivity()).showForm(Provider.TAURON);
    }

    private void expandFabs() {
        if (collapseAnimation != null && collapseAnimation.isRunning())
            collapseAnimation.end();
        if (expandAnimation == null)
            expandAnimation = Animations.getExpandFabs(fab, fabTauron, fabPgnig, fabPge);

        expandAnimation.start();
        dimLayout.setBackgroundResource(R.color.dim_overlay);
        dimLayout.setClickable(true);
    }

    private void collapseFabs() {
        if (expandAnimation != null && expandAnimation.isRunning())
            expandAnimation.end();
        if (collapseAnimation == null)
            collapseAnimation = Animations.getCollapseFabs(fab, fabTauron, fabPgnig, fabPge);

        dimLayout.setBackgroundResource(0);
        dimLayout.setClickable(false);
        collapseAnimation.start();
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
}