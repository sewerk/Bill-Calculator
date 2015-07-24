package pl.srw.billcalculator.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.event.PdfGeneratedEvent;
import pl.srw.billcalculator.util.BillExporter;
import pl.srw.billcalculator.util.Views;

/**
 * Created by kseweryn on 30.06.15.
 */
public class PrintTask extends AsyncTask<View, Void, String> {

    private final String path;

    public PrintTask(final String path) {
        this.path = path;
    }

    @Override
    protected String doInBackground(View... params) {
        final View contentView = params[0];
        final Bitmap bitmap = Views.buildBitmapFrom(contentView);
        final File targetFile = new File(path);
        File tmpImg = null;
        try {
            tmpImg = File.createTempFile("img", null, targetFile.getParentFile());
            tmpImg = BillExporter.writeToImage(tmpImg, bitmap);
            if (tmpImg != null) {
                final File pdfFile = BillExporter.printToPdf(targetFile, tmpImg.getAbsolutePath());
                if (pdfFile != null) return pdfFile.getAbsolutePath();
            }
        } catch (IOException e) {
            AnalyticsWrapper.log(e.getMessage());
        } finally {
            if (tmpImg != null) {
                final boolean deleted = tmpImg.delete();
                if (!deleted)
                    AnalyticsWrapper.log("Tmp file deleted=" + deleted);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        TaskManager.getInstance().unregister(path);
        EventBus.getDefault().post(new PdfGeneratedEvent(s));
    }

}
