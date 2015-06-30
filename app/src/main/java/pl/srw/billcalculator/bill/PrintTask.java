package pl.srw.billcalculator.bill;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.activity.BillActivity;
import pl.srw.billcalculator.util.PdfGenerator;
import pl.srw.billcalculator.util.Views;

/**
 * Created by kseweryn on 30.06.15.
 */
public class PrintTask extends AsyncTask<Void, Void, String> {

    private final View view;
    private final MenuItem item;

    public PrintTask(View view, MenuItem item) {
        this.view = view;
        this.item = item;
    }

    @Override
    protected void onPreExecute() {
        item.setEnabled(false);
        item.setActionView(new ProgressBar(view.getContext()));
    }

    @Override
    protected String doInBackground(Void... params) {
        Bitmap bitmap = Views.buildBitmapFrom(view);
        return PdfGenerator.generate(getPath(), bitmap);
    }

    private String getPath() {
        String tmp = "bill";//TODO
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Rachunki");
        if (!direct.exists()) {
            boolean dirCreated = direct.mkdirs();
        }
        return direct.getAbsolutePath() + "/" + tmp + ".pdf";
    }

    @Override
    protected void onPostExecute(String filePath) {
        item.setActionView(null);
        if (filePath == null)
            Toast.makeText(view.getContext(), "Saving failed", Toast.LENGTH_SHORT).show();
        else
//            Toast.makeText(view.getContext(), "SUCCESS", Toast.LENGTH_SHORT).show();
            ((BillActivity) view.getContext()).openFile(Uri.fromFile(new File(filePath)));
        item.setEnabled(true);
    }
}
