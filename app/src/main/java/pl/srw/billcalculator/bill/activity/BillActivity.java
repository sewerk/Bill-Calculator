package pl.srw.billcalculator.bill.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Inject;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.dialog.ExplainPermissionRequestDialogFragment;
import pl.srw.billcalculator.util.strategy.Transitions;
import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.presenter.PresenterHandlingDelegate;
import pl.srw.mfvp.presenter.PresenterOwner;
import pl.srw.mfvp.presenter.SinglePresenterHandlingDelegate;

abstract class BillActivity<T extends MvpComponent>
        extends BackableActivity<T>
        implements PresenterOwner, BillPresenter.BillView {

    public static final String MIME_APPLICATION_PDF = "application/pdf";
    public static final String MIME_IMAGE = "image/*";

    private static final int PERMISSION_REQUEST_CODE = 101;

    @Inject BillPresenter presenter;

    private Menu menu;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewCompat.setTransitionName(findViewById(R.id.decor_content_parent), Transitions.BILL_VIEW_TRANSITION_NAME);
    }

    @Override
    protected void onStart() {
        presenter.setup(getBillIdentifier());
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        this.menu = menu;
        return true;
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_print) {
            presenter.onPrintClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public PresenterHandlingDelegate createPresenterDelegate() {
        return new SinglePresenterHandlingDelegate(this, presenter);
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
        }
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permission);
    }

    @Override
    public void requestPermission(@NonNull String permission) {
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
    public @interface FileType {}

    @Override
    public void openFile(@NonNull File file, @NonNull @FileType String type) {
        final Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent); // TODO: StrictMode.onFileUriExposed: exposed beyond app through Intent.getData()
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

    protected abstract String getBillIdentifier();
}
