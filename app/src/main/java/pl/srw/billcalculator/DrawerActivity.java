package pl.srw.billcalculator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.form.fragment.HasTitle;
import pl.srw.billcalculator.form.fragment.PgeFormFragment;
import pl.srw.billcalculator.form.fragment.PgnigFormFragment;
import pl.srw.billcalculator.form.fragment.TauronFormFragment;
import pl.srw.billcalculator.history.MyBillsFragment;

/**
 * Created by kseweryn on 07.07.15.
 */
public class DrawerActivity extends AppCompatActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.drawer_navigation)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ButterKnife.inject(this);
        setupToolbar();
        setupNavigationView();
        switchContentFragment(new MyBillsFragment());
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null)
            return;
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupNavigationView() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switchContent(menuItem.getItemId());
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void switchContent(@IdRes final int id) {
        switch (id) {
            case R.id.my_bills:
                switchContentFragment(new MyBillsFragment());
                return;
            case R.id.new_bill_pge:
                switchContentFragment(new PgeFormFragment());
                return;
            case R.id.new_bill_pgnig:
                switchContentFragment(new PgnigFormFragment());
                return;
            case R.id.new_bill_tauron:
                switchContentFragment(new TauronFormFragment());
                return;
                //TODO
        }
    }

    private void switchContentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        setTitle(((HasTitle) fragment).getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                //NO-OP
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
