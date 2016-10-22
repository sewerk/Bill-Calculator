package pl.srw.billcalculator.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;

/**
 * Created by kseweryn on 27.04.15.
 */
public final class ToWebView {

    private ToWebView() {
    }

    public static View wrapByWebView(Context context, View billView) {
        WebView view = new WebView(context);
        view.getSettings().setBuiltInZoomControls(true);
        return loadHtmlData(view, billView);
    }

    private static View loadHtmlData(WebView view, View billView) {
        final String mime = "text/html";
        final String encoding = "utf-8";

        String html = generateHtml(billView);
        view.loadData(html, mime, encoding);
        return view;
    }

    private static String generateHtml(View billView) {
        return "<!DOCTYPE html><html><body><img width='100%' height='100%' src='" + bitmapToString(Views.buildBitmapFrom(billView)) + "' /></body></html>";
//FIXME: reafactor/optimize
    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return "data:image/png;base64," + imageBase64;
    }
}
