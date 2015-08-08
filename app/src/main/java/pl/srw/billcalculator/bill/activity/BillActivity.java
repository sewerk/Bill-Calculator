package pl.srw.billcalculator.bill.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.event.PdfGeneratedEvent;
import pl.srw.billcalculator.task.PrintTask;
import pl.srw.billcalculator.task.TaskManager;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.util.ToWebView;

/**
 * Created by kseweryn on 18.06.15.
 */
public abstract class BillActivity extends BackableActivity {

    private static final String PRINT_TARGET_DIR = BillCalculator.context.getString(R.string.print_dir);
    public static final String MIME_APPLICATION_PDF = "application/pdf";
    public static final String MIME_IMAGE = "image/*";
    private Menu menu;

    @Override
    @CallSuper
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill, menu);
        this.menu = menu;
        if (isPrintTaskRunning())
            setPrintInProgress();
        return true;
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        setPrintFinished();
        super.onDestroy();
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_zoom_out) {
            //TODO: consider loading icon, hide after done
            setContentView(ToWebView.wrapByWebView(this, findViewById(R.id.bill_content)));
            return true;
        } else if (item.getItemId() == R.id.action_print) {
            final File targetFile = getTargetFile("pdf");
            if (targetFile != null) {
                AnalyticsWrapper.logAction(ActionType.PRINT,
                        "print file", targetFile.getName(),
                        "print file exist", String.valueOf(targetFile.exists()));
                if (targetFile.exists()) {
                    openFile(targetFile, MIME_APPLICATION_PDF);
                } else {
                    setPrintInProgress();
                    final PrintTask printTask = new PrintTask(targetFile.getAbsolutePath());
                    TaskManager.getInstance().register(targetFile.getAbsolutePath(), printTask);

                    final View contentView = findViewById(R.id.bill_content);
                    printTask.execute(contentView);
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(final PdfGeneratedEvent event) {
        final String filePath = event.getPath();
        if (filePath == null) {
            Toast.makeText(this, getString(R.string.print_failed), Toast.LENGTH_SHORT).show();
            AnalyticsWrapper.warning("Saving PDF failed");
        } else
            openFile(new File(filePath), MIME_APPLICATION_PDF);
        setPrintFinished();
    }

    @StringDef({MIME_APPLICATION_PDF, MIME_IMAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileType {}

    private void openFile(final File file, @FileType final String type) {
        final Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @StringDef({"pdf", "jpg"})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FileExtention {}

    @Nullable
    private File getTargetFile(@FileExtention final String ext) {
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PRINT_TARGET_DIR);
        if (!direct.exists())
            if (!direct.mkdirs()) {
                Toast.makeText(this, getString(R.string.print_io_problem), Toast.LENGTH_LONG).show();
                AnalyticsWrapper.warning("mkdir failed");
                return null;
            }
        return new File(direct.getAbsolutePath(), getBillIdentifier() + "." + ext);
    }

    protected abstract String getBillIdentifier();

    private void setPrintInProgress() {
        if (menu != null) {
            MenuItem printMenuItem = menu.findItem(R.id.action_print);
            printMenuItem.setEnabled(false);
            printMenuItem.setActionView(new ProgressBar(this));
        }
    }

    private void setPrintFinished() {
        if (menu != null) {
            MenuItem printMenuItem = menu.findItem(R.id.action_print);
            if (printMenuItem.getActionView() != null) {
                printMenuItem.setActionView(null);
                printMenuItem.setEnabled(true);
            }
        }
    }

    private boolean isPrintTaskRunning() {
        final AsyncTask task = TaskManager.getInstance().findBy(getTargetFile("pdf").getAbsolutePath());
        return task != null && task.getStatus() != AsyncTask.Status.FINISHED;
    }
}
