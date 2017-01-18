package pl.srw.billcalculator.bill.activity.print;

import android.graphics.Bitmap;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pl.srw.billcalculator.util.BillExporter;
import pl.srw.billcalculator.util.Views;
import pl.srw.billcalculator.wrapper.Analytics;

@Singleton
public class Printer {

    private Set<String> inProgressFiles;
    private Listener listener;

    @Inject
    Printer() {
        inProgressFiles = new HashSet<>(1);
    }

    public void register(Listener listener) {
        this.listener = listener;
    }

    public void unregister(Listener listener) {
        this.listener = null;
    }

    public void printToPdf(final View contentView, final File targetFile) {
        inProgressFiles.add(targetFile.getAbsolutePath());
        if (listener != null) {
            listener.onPrintStarted(targetFile);
        }

        Single.just(contentView)
                .map(new Function<View, Bitmap>() {
                    @Override
                    public Bitmap apply(View view) throws Exception {
                        return buildBitmapFrom(contentView);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<Bitmap, File>() {
                    @Override
                    public File apply(Bitmap bitmap) throws Exception {
                        File tmpImg = null;
                        try {
                            tmpImg = File.createTempFile("img", null, targetFile.getParentFile());
                            tmpImg = writeToImage(bitmap, tmpImg);

                            return imageToPdf(tmpImg, targetFile);
                        } finally {
                            if (tmpImg != null) {
                                final boolean deleted = tmpImg.delete();
                                if (!deleted)
                                    Analytics.warning("Tmp file NOT deleted");
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                inProgressFiles.remove(targetFile.getAbsolutePath());

                if (listener != null) {
                    listener.onPrintoutReady(file);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                inProgressFiles.remove(targetFile.getAbsolutePath());

                if (listener != null) {
                    listener.onPrintFailed(throwable);
                }
            }
        });
    }

    public boolean isPrintInProgress(File targetFile) {
        return inProgressFiles.contains(targetFile.getAbsolutePath());
    }

    @VisibleForTesting
    Bitmap buildBitmapFrom(View contentView) {
        return Views.buildBitmapFrom(contentView);// TODO: rewrite to Rx
    }

    @VisibleForTesting
    File writeToImage(Bitmap bitmap, File tmpImg) throws IOException {
        return BillExporter.writeToImage(tmpImg, bitmap);// TODO: rewrite to Rx
    }

    @VisibleForTesting
    File imageToPdf(File tmpImg, File targetFile) throws IOException, DocumentException {
        return BillExporter.printToPdf(targetFile, tmpImg.getAbsolutePath());
    }

    public interface Listener {
        void onPrintStarted(File file);
        void onPrintoutReady(File file);
        void onPrintFailed(Throwable exception);
    }
}