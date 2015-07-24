package pl.srw.billcalculator.util;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.AnalyticsWrapper;

/**
 * Created by Kamil Seweryn.
 */
public final class BillExporter {

    @DebugLog
    @Nullable public static File writeToImage(final File imageFile, final Bitmap bitmap) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } catch (IOException e) {
            AnalyticsWrapper.log(e.getMessage());
            return null;
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception ignored) { }
        }
        return imageFile;
    }

    @DebugLog
    @Nullable public static File printToPdf(final File targetFile, final String imageFilePath) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(targetFile));
            document.open();

            Image image = Image.getInstance(imageFilePath);
            scaleToFit(document, image);
            document.add(image);

            document.close();
        } catch (Exception e) {
            AnalyticsWrapper.log(e.getMessage());
            return null;
        }
        return targetFile;
    }

    @DebugLog
    private static void scaleToFit(Document document, Image image) {
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / image.getWidth()) * 100;
        image.scalePercent(scaler);
    }
}
