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
import butterknife.ButterKnife;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.about.AboutDialogFragment;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.di.Dependencies;
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
import pl.srw.mfvp.MvpActivity;
import timber.log.Timber;

public class DrawerActivity extends MvpActivity<HistoryComponent>
        implements HistoryPresenter.HistoryView,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.bill_list) RecyclerView listView;
    @BindView(R.id.empty_history) View emptyHistoryView;
    @BindInt(R.integer.cardAmount) int cardAmount;

    @Inject HistoryPresenter presenter;
//FIXME    @Inject DrawerPresenter drawerPresenter;
    @Inject FabsMenuViewHandler fabsMenuHandler;
    @Inject HelpHandler helpHandler;
    @Inject BillSelection selection;
    @Inject MenuClickHandlerExtension menuHandlerExtension;

    private HistoryAdapter adapter;
    private MenuItem deleteMenuAction;
    private HistoryItemTouchCallback touchCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);

        fabsMenuHandler.init(this);
        setupToolbar();
        setupDrawer();
        setupList();

        attachPresenter(presenter);
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
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) Dependencies.INSTANCE.releaseHistoryComponent();
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
        } else if (menuHandlerExtension.onOptionsItemSelected(item)) {
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
            Timber.i("Drawer: New bill picked: PGE");
            presenter.newBillClicked(Provider.PGE);
        } else if (id == R.id.new_bill_pgnig) {
            Timber.i("Drawer: New bill picked: PGNIG");
            presenter.newBillClicked(Provider.PGNIG);
        } else if (id == R.id.new_bill_tauron) {
            Timber.i("Drawer: New bill picked: TAURON");
            presenter.newBillClicked(Provider.TAURON);
        } else if (id == R.id.settings) {
            Timber.i("Drawer: Settings clicked");
            presenter.settingsClicked();
        } else if (id == R.id.about) {
            Timber.i("Drawer: About clicked");
            presenter.aboutClicked();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Timber.i("Back clicked");
        if (!presenter.handleBackPressed()
                && !fabsMenuHandler.handleBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public HistoryComponent prepareComponent() {
        return Dependencies.INSTANCE.getHistoryComponent();
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
        new AboutDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void showHelp() {
        helpHandler.show(this);
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
    public void enableSwipeDelete() {
        touchCallback.setSwipeEnabled(true);
    }

    @Override
    public void disableSwipeDelete() {
        touchCallback.setSwipeEnabled(false);
    }

    @Override
    public void showWelcomeDialog() {
        new CheckPricesDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void showNewUIDialog() {
        new NewUIDialogFragment().show(getSupportFragmentManager(), null);
    }

    @Override
    public void openBill(Bill bill, View viewClicked) {
        final Intent intent = BillActivityIntentFactory.create(this, bill);
        Transitions.getInstance().startActivity(this, intent, viewClicked);
    }

    @Override
    public void showUndoDeleteMessage(final int... positions) {
        Snackbar.make(coordinatorLayout, R.string.bill_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_undo_delete, v -> {
                    Timber.i("Undo clicked");
                    presenter.undoDeleteClicked(positions);
                })
                .setActionTextColor(getResources().getColor(R.color.yellow))
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

        touchCallback = new HistoryItemTouchCallback(presenter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchCallback);
        itemTouchHelper.attachToRecyclerView(listView);
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.history_label);
        collapsingToolbarLayout.setTitle(toolbar.getTitle());
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerSlideAnimationEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
}