package pl.srw.billcalculator.task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.event.PdfGeneratedEvent;
import pl.srw.billcalculator.task.TaskManager;
import pl.srw.billcalculator.util.PdfGenerator;
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
        Bitmap bitmap = Views.buildBitmapFrom(params[0]);
        return PdfGenerator.generate(path, bitmap);
    }

    @Override
    protected void onPostExecute(String s) {
        TaskManager.getInstance().unregister(path);
        EventBus.getDefault().post(new PdfGeneratedEvent(s));
    }

}
