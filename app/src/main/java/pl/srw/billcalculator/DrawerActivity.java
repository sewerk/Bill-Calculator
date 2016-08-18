package pl.srw.billcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.history.FabsMenuHandler;
import pl.srw.billcalculator.history.di.HistoryComponent;
import pl.srw.billcalculator.history.HistoryPresenter;
import pl.srw.billcalculator.settings.activity.SettingsActivity;
import pl.srw.mfvp.MvpActivity;
import pl.srw.mfvp.view.delegate.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.view.delegate.presenter.PresenterOwner;
import pl.srw.mfvp.view.delegate.presenter.SinglePresenterHandlingDelegate;

public class DrawerActivity extends MvpActivity<HistoryComponent>
        implements HistoryPresenter.HistoryView,
        PresenterOwner,
        NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.nav_view) NavigationView navigationView;
    @Bind(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.bill_list) RecyclerView recyclerView;

    @Inject HistoryPresenter presenter;
    @Inject FabsMenuHandler fabsMenuHandler;

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_help) {
            presenter.helpMenuClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.my_bills) {
            presenter.myBillsClicked();
        } else if (id == R.id.new_bill_pge) {
            presenter.newPgeBillClicked();
        } else if (id == R.id.new_bill_pgnig) {
            presenter.newPgnigButtonClicked();
        } else if (id == R.id.new_bill_tauron) {
            presenter.newTauronBillClicked();
        } else if (id == R.id.settings) {
            presenter.settingsClicked();
        } else if (id == R.id.about) {
            presenter.aboutClicked();
        }

        drawer.closeDrawer(GravityCompat.START);
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
        return BillCalculator.get(this).getApplicationComponent().getHistoryComponent();
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

    private void setupList() {
        // TODO
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.my_bills);
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
