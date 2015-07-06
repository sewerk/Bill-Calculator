package pl.srw.billcalculator.bill.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.task.PrintTask;
import pl.srw.billcalculator.event.PdfGeneratedEvent;
import pl.srw.billcalculator.task.TaskManager;
import pl.srw.billcalculator.util.ToWebView;

/**
 * Created by kseweryn on 18.06.15.
 */
public abstract class BillActivity extends BackableActivity {

    private static final String PRINT_TARGET_DIR = BillCalculator.context.getString(R.string.print_dir);
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
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        setPrintFinished();
    }

    @CallSuper
    @CheckResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View billView = findViewById(R.id.bill_content);
        if (item.getItemId() == R.id.action_zoom_out) {
            //TODO: consider loading icon, hide after done
            setContentView(ToWebView.wrapByWebView(this, billView));
            return true;
        } else if (item.getItemId() == R.id.action_print) {
            final File targetFile = getTargetPdfFile();
            if (targetFile.exists())
                openPdfFile(targetFile);
            else {
                setPrintInProgress();
                final PrintTask printTask = new PrintTask(targetFile.getAbsolutePath());
                TaskManager.getInstance().register(targetFile.getAbsolutePath(), printTask);
                printTask.execute(billView);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(final PdfGeneratedEvent event) {
        final String filePath = event.getPath();
        if (filePath == null)
            Toast.makeText(this, getString(R.string.print_failed), Toast.LENGTH_SHORT).show();
        else
            openPdfFile(new File(filePath));
        setPrintFinished();
    }

    private void openPdfFile(File file) {
        final Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private File getTargetPdfFile() {
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + PRINT_TARGET_DIR);
        if (!direct.exists())
            if (!direct.mkdirs())
                Toast.makeText(this, getString(R.string.print_io_problem), Toast.LENGTH_LONG).show();
        return new File(direct.getAbsolutePath(), getBillIdentifier() + ".pdf");
    }

    protected abstract String getBillIdentifier();

    private void setPrintInProgress() {
        MenuItem printMenuItem = menu.findItem(R.id.action_print);
        printMenuItem.setEnabled(false);
        printMenuItem.setActionView(new ProgressBar(this));
    }

    private void setPrintFinished() {
        MenuItem printMenuItem = menu.findItem(R.id.action_print);
        if (printMenuItem.getActionView() != null) {
            printMenuItem.setActionView(null);
            printMenuItem.setEnabled(true);
        }
    }

    private boolean isPrintTaskRunning() {
        final AsyncTask task = TaskManager.getInstance().findBy(getTargetPdfFile().getAbsolutePath());
        return task != null && task.getStatus() != AsyncTask.Status.FINISHED;
    }
}
