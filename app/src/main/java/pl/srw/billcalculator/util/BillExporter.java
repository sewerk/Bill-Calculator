package pl.srw.billcalculator.util;

import android.graphics.Bitmap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import hugo.weaving.DebugLog;

public class BillExporter {
    @DebugLog
    public static File writeToImage(final File imageFile, final Bitmap bitmap) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception ignored) { }
        }
        return imageFile;
    }

    @DebugLog
    public static File printToPdf(final File targetFile, final String imageFilePath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(targetFile));
        document.open();

        Image image = Image.getInstance(imageFilePath);
        scaleToFit(document, image);
        document.add(image);

        document.close();
        return targetFile;
    }

    @DebugLog
    private static void scaleToFit(Document document, Image image) {
        float scale = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin()) / image.getWidth()) * 100;
        image.scalePercent(scale);
    }
}
