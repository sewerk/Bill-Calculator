package pl.srw.billcalculator.bill.task;

import android.os.AsyncTask;
import android.view.View;

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
//TODO        final View contentView = params[0];
//        final Bitmap bitmap = Views.buildBitmapFrom(contentView);//TODO: do in UIThread
//        final File targetFile = new File(path);
//        File tmpImg = null;
//        try {
//            tmpImg = File.createTempFile("img", null, targetFile.getParentFile());
//            tmpImg = BillExporter.writeToImage(tmpImg, bitmap);
//
//            final File pdfFile = BillExporter.printToPdf(targetFile, tmpImg.getAbsolutePath());
//            return pdfFile.getAbsolutePath();
//        } catch (IOException | DocumentException e) {
//            Analytics.error(e);
//        } finally {
//            if (tmpImg != null) {
//                final boolean deleted = tmpImg.delete();
//                if (!deleted)
//                    Analytics.warning("Tmp file deleted=" + deleted);
//            }
//        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        TaskManager.getInstance().unregister(path);
//TODO        EventBus.getDefault().post(new PdfGeneratedEvent(s));
    }

}
