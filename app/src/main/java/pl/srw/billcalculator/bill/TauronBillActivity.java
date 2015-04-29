package pl.srw.billcalculator.bill;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import org.rendersnake.DocType;
import org.rendersnake.HtmlCanvas;
import org.threeten.bp.LocalDate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.util.Dates;

import static org.rendersnake.HtmlAttributesFactory.align;

/**
 * Created by kseweryn on 23.04.15.
 */
public class TauronBillActivity extends BackableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView view = new WebView(this);
        view.getSettings().setBuiltInZoomControls(true);
        setContentView(loadHtmlData(view));
    }

    private View loadHtmlData(WebView view) {
        final String mime = "text/html";
        final String encoding = "utf-8";

        String html = generateHtml();
        view.loadData(html, mime, encoding);
        return view;
    }

    private String generateHtml() {
        HtmlCanvas htmlCanvas = new HtmlCanvas();
        try {
            htmlCanvas
                    .render(DocType.HTML5)
                    .html()
                    .body()

                    .table()
                    .tr().td()
                        .img(align("right").src(bitmapToString(getDrawableMy(R.drawable.tauron))).width(400).height(200))
                    ._td()._tr()
                    .tr().td(align("right"))
                        .write("Data wystawienia: " + Dates.format(LocalDate.now()))
                    ._td()._tr()

                    .tr().td()
                    ._td()._tr()
                    ._table()

                    ._body()
                    ._html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlCanvas.toHtml();
    }

    private Bitmap getDrawableMy(int drawableId) {
//        Bitmap bitmap = getBitmap(drawableId);
        return drawableToBitmap(getResources().getDrawable(drawableId));
    }

    private String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        final String imgageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return "data:image/png;base64," + imgageBase64;
    }

    private Bitmap getBitmap(int drawableId) {
        return BitmapFactory.decodeResource(this.getResources(), drawableId);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            Log.d("TAURON", "BitmapDrawable");
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
