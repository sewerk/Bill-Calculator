package pl.srw.billcalculator.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.AnalyticsWrapper;

/**
 * Created by Kamil Seweryn.
 */
public final class PdfGenerator {

    @DebugLog
    public static String generate(final String path, Bitmap bitmap) {
        try {//TODO: optimise performance: 7731ms
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image image = Image.getInstance(byteArray);
            scaleToFit(document, image);
            image.setAlignment(Image.ALIGN_MIDDLE);//TODO: fix
            document.add(image);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
            //TODO: later maybe: return null;
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
