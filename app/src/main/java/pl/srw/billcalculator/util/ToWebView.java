package pl.srw.billcalculator.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;

import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.rendersnake.HtmlAttributesFactory.src;

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
        HtmlCanvas htmlCanvas = new HtmlCanvas();
        try {
            htmlCanvas
                    .render(DocType.HTML5)
                    .html()
                    .body()

                    .img(src(bitmapToString(loadBitmapFromView(billView)))//TODO: reafactor/optimize
                            .width("100%").height("100%")
                    )

                    ._body()
                    ._html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlCanvas.toHtml();
    }

    private static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return "data:image/png;base64," + imageBase64;
    }

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
}
