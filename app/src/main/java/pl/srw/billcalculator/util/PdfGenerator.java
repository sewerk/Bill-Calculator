package pl.srw.billcalculator.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by Kamil Seweryn.
 */
public final class PdfGenerator {

    private static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0 || v.getLayoutParams().height <= 0) {
            int specWidth = View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST);
            if (specWidth <= 0)
                specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
            v.measure(specWidth, specWidth);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public static String generateFrom(final View view, final String name) {
        File direct = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Rachunki");
        if (!direct.exists()) {
            boolean dirCreated = direct.mkdirs();
        }
        String path = direct.getAbsolutePath() + "/" + name + ".pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            loadBitmapFromView(view).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image image = Image.getInstance(byteArray);
            scaleToFit(document, image);
            image.setAlignment(Image.ALIGN_MIDDLE);//TODO: fix
            document.add(image);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return path;
    }

    private static void scaleToFit(Document document, Image image) {
        //image.scaleToFit(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        //if you would have a chapter indentation
        int indentation = 0;
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - indentation) / image.getWidth()) * 100;

        image.scalePercent(scaler);
    }
}
