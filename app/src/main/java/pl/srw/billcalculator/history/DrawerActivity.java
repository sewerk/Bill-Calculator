package pl.srw.billcalculator.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.greendao.query.LazyList;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import pl.srw.billcalculator.AboutActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.dialog.CheckPricesDialogFragment;
import pl.srw.billcalculator.form.fragment.FormFragment;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.history.list.HistoryAdapter;
import pl.srw.billcalculator.history.list.ShowViewOnEmptyDataObserver;
import pl.srw.billcalculator.history.list.item.HistoryItemTouchCallback;
import pl.srw.billcalculator.intent.BillActivityIntentFactory;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.BillSelection;
import pl.srw.billcalculator.util.strategy.Transitions;
import pl.srw.billcalculator.wrapper.Dependencies;
import pl.srw.mfvp.MvpActivity;
import pl.srw.mfvp.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.presenter.SinglePresenterHandlingDelegate;

public class DrawerActivity extends MvpActivity<HistoryComponent>
        implements HistoryPresenter.HistoryView,
        PresenterOwner,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bill_list) RecyclerView listView;
    @BindView(R.id.empty_history) View emptyHistoryView;
    @BindInt(R.integer.cardAmount) int cardAmount;
    private HistoryAdapter adapter;

    @Inject HistoryPresenter presenter;
//TODO    @Inject DrawerPresenter drawerPresenter;
    @Inject FabsMenuHandler fabsMenuHandler;
    @Inject BillSelection selection;
    private MenuItem deleteMenuAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dependencies.inject(this);

        fabsMenuHandler.setup(this);
        setupToolbar();
        setupDrawer();
        setupList();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_drawer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_activity, menu);
        deleteMenuAction = menu.findItem(R.id.action_delete);
        if (selection.isAnySelected()) {
            deleteMenuAction.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            presenter.helpMenuClicked();
            return true;
        } else if (id == R.id.action_delete) {
            presenter.deleteClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.history) {
            presenter.historyClicked();
        } else if (id == R.id.new_bill_pge) {
            presenter.newBillClicked(Provider.PGE);
        } else if (id == R.id.new_bill_pgnig) {
            presenter.newBillClicked(Provider.PGNIG);
        } else if (id == R.id.new_bill_tauron) {
            presenter.newBillClicked(Provider.TAURON);
        } else if (id == R.id.settings) {
            presenter.settingsClicked();
        } else if (id == R.id.about) {
            presenter.aboutClicked();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!presenter.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
    }

    @Override
    public HistoryComponent prepareComponent() {
        return Dependencies.getApplicationComponent().getHistoryComponent();
    }

    @Override
    public boolean isDrawerOpen() {
        return drawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showNewBillForm(Provider provider) {
        FormFragment.newInstance(provider).show(getSupportFragmentManager(), "FORM");
    }

    @Override
    public void openSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void showAbout() {
        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public void showHelp() {
        // TODO: impl overlaying help
    }

    @Override
    public void showDeleteButton() {
        deleteMenuAction.setVisible(true);
    }

    @Override
    public void hideDeleteButton() {
        deleteMenuAction.setVisible(false);
    }

    @Override
    public void showWelcomeDialog() {
        new CheckPricesDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void openBill(Bill bill, View viewClicked) {
        final Intent intent = BillActivityIntentFactory.create(this, bill);
        Transitions.getInstance().startActivity(this, intent, viewClicked);
    }

    @Override
    public void showUndoDeleteMessage(final int position) {
        Snackbar.make(coordinatorLayout, R.string.bill_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.undoDeleteClicked(position);
                    }
                })
                .show();
    }

    @Override
    public void setListData(LazyList<History> data) {
        adapter.setData(data);
    }

    @Override
    public void redrawList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemRemoveFromList(int position) {
        adapter.notifyItemRemoved(position);
        //TODO: animate swipe-delete
    }

    @Override
    public void onItemInsertedToList(int position) {
        adapter.notifyItemInserted(position);
        listView.smoothScrollToPosition(position);
    }

    private void setupList() {
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new GridLayoutManager(this, cardAmount));
        adapter = new HistoryAdapter(new ShowViewOnEmptyDataObserver(emptyHistoryView), presenter, selection);
        listView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new HistoryItemTouchCallback(presenter));
        helper.attachToRecyclerView(listView);
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.history);
        collapsingToolbarLayout.setTitle(toolbar.getTitle());
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
}