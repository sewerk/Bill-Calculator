package pl.srw.billcalculator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wnafee.vector.compat.ResourcesCompat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hrisey.InstanceState;
import pl.srw.billcalculator.form.fragment.PgeFormFragment;
import pl.srw.billcalculator.form.fragment.PgnigFormFragment;
import pl.srw.billcalculator.form.fragment.TauronFormFragment;
import pl.srw.billcalculator.history.MyBillsFragment;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 07.07.15.
 */
public class DrawerActivity extends AppCompatActivity implements DrawerHandling {

    @InjectView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.drawer_navigation) NavigationView navigationView;
    @InstanceState @IdRes private int currentItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ButterKnife.inject(this);

        setupContent(savedInstanceState);
        setupNavigationView();
    }

    private void setupContent(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, MyBillsFragment.newInstance()).commit();
            currentItem = R.id.my_bills;
        }
    }

    private void setupNavigationView() {
        Drawable listDrawable = ResourcesCompat.getDrawable(this, R.drawable.ic_list_white_24px);
        navigationView.getMenu().findItem(R.id.my_bills).setIcon(listDrawable);
        Drawable infoDrawable = ResourcesCompat.getDrawable(this, R.drawable.ic_info_outline_white_24px);
        navigationView.getMenu().findItem(R.id.about).setIcon(infoDrawable);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        onDrawerItemClicked(menuItem);
                        return true;
                    }
                });
    }

    private void onDrawerItemClicked(final MenuItem menuItem) {
        final int id = menuItem.getItemId();
        if (currentItem == id) return;
        if (id == R.id.about)
            startActivity(new Intent(this, AboutActivity.class));
        else {
            if (id == R.id.my_bills) backToHome();
            else if (id == R.id.new_bill_pge) showForm(Provider.PGE);
            else if (id == R.id.new_bill_pgnig) showForm(Provider.PGNIG);
            else if (id == R.id.new_bill_tauron) showForm(Provider.TAURON);
        }
    }

    @Override
    public void showForm(Provider provider) {
        Fragment fragment;
        switch (provider) {
            case PGE:
                fragment = new PgeFormFragment();
                currentItem = R.id.new_bill_pge;
                break;
            case PGNIG:
                fragment = new PgnigFormFragment();
                currentItem = R.id.new_bill_pgnig;
                break;
            case TAURON:
                fragment = new TauronFormFragment();
                currentItem = R.id.new_bill_tauron;
                break;
            default:
                throw new EnumVariantNotHandledException(provider);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT > 15) {
            //cause exception on 4.0.4
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.shrink_fade_out_center, R.anim.grow_fade_in_center, R.anim.slide_out_right);
        }
        ft.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        navigationView.getMenu().findItem(currentItem).setChecked(true);
    }

    @Override
    public void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void backToHome() {
        getSupportFragmentManager().popBackStack();
        currentItem = R.id.my_bills;
        navigationView.getMenu().findItem(currentItem).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else if (currentItem != R.id.my_bills)
            backToHome();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.openDrawer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
