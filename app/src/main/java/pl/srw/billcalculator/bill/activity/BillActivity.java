package pl.srw.billcalculator.bill.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BuildConfig;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.dialog.ExplainPermissionRequestDialogFragment;
import pl.srw.billcalculator.intent.IntentCreator;
import pl.srw.billcalculator.settings.prices.SharedPrefsPrices;
import pl.srw.billcalculator.util.strategy.Transitions;
import pl.srw.mfvp.di.MvpComponent;
import timber.log.Timber;

import static pl.srw.billcalculator.settings.prices.SharedPrefsPricesKt.DISABLED_OPLATA_HANDLOWA;

abstract class BillActivity<P extends Prices, PP extends P, T extends MvpComponent>
        extends BackableActivity<T>
        implements BillPresenter.BillView {

    public static final String MIME_APPLICATION_PDF = "application/pdf";
    private static final String MIME_IMAGE = "image/*";

    private static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    private static final int PERMISSION_REQUEST_CODE = 101;

    protected LocalDate dateFrom;
    protected LocalDate dateTo;
    protected int readingFrom;
    protected int readingTo;
    protected P prices;

    @Inject BillPresenter presenter;
    @Inject SavedBillsRegistry savedBillsRegistry;
    @Inject PP prefsPrices;

    private Menu menu;
    private Runnable menuChange;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ViewCompat.setTransitionName(findViewById(R.id.decor_content_parent), Transitions.BILL_VIEW_TRANSITION_NAME);

        readExtraFrom(getIntent());

        presenter.setup(getBillIdentifier());
        attachPresenter(presenter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        this.menu = menu;
        if (menuChange != null) {
            menuChange.run();
            menuChange = null;
        }
        return true;
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            Timber.i("Print icon clicked");
            presenter.onPrintClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPrintReadyIcon() {
        if (menu != null) {
            MenuItem printMenuItem = menu.findItem(R.id.action_print);
            printMenuItem.setActionView(null);
            printMenuItem.setEnabled(true);
        }
    }

    @Override
    public void setPrintInProgressIcon() {
        if (menu != null) {
            MenuItem printMenuItem = menu.findItem(R.id.action_print);
            printMenuItem.setEnabled(false);
            printMenuItem.setActionView(new ProgressBar(this));
        } else {
            menuChange = this::setPrintInProgressIcon;
        }
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission);
    }

    @Override
    public void requestPermission(@NonNull String permission) {
        Timber.i("requesting print permission");
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean shouldShowExplanation(@NotNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
    }

    @Override
    public void showExplanationForRequestPermission(@NonNull String permission) {
        final ExplainPermissionRequestDialogFragment dialog
                = ExplainPermissionRequestDialogFragment.newInstance(new String[]{permission}, PERMISSION_REQUEST_CODE);
        dialog.show(getFragmentManager(), "RequestPermissionRationaleDialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                presenter.processRequestPermissionResponse(grantResults);
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @StringDef({MIME_APPLICATION_PDF, MIME_IMAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {
    }

    @Override
    public void openFile(@NonNull File file, @NonNull @FileType String type) {
        final Uri uri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.print_no_pdf_viewer, file.getAbsolutePath()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showMessage(@StringRes int textResIdRes) {
        Toast.makeText(this, textResIdRes, Toast.LENGTH_SHORT).show();
    }

    @NotNull
    @Override
    public View getContentView() {
        return findViewById(R.id.bill_content);
    }

    @CallSuper
    protected void readExtraFrom(Intent intent) {
        dateFrom = (LocalDate) intent.getSerializableExtra(IntentCreator.DATE_FROM);
        dateTo = (LocalDate) intent.getSerializableExtra(IntentCreator.DATE_TO);
        readingFrom = intent.getIntExtra(IntentCreator.READING_FROM, 0);
        readingTo = intent.getIntExtra(IntentCreator.READING_TO, 0);
        prices = getPricesFromIntentOr(prefsPrices);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract String getBillIdentifier();

    protected boolean isNewBill() {
        return !getIntent().hasExtra(IntentCreator.PRICES);
    }

    protected boolean isOplataHandlowaEnabled() {
        if (prices instanceof SharedPrefsPrices)
            return ((SharedPrefsPrices) prices).getEnabledOplataHandlowa();
        else
            return !prices.getOplataHandlowa().equals(DISABLED_OPLATA_HANDLOWA);
    }

    private P getPricesFromIntentOr(PP prefsPrices) {
        if (isNewBill()) return prefsPrices;
        else //noinspection unchecked
            return (P) getIntent().getSerializableExtra(IntentCreator.PRICES);
    }
}
