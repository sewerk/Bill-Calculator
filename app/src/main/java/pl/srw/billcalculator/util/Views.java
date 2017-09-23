package pl.srw.billcalculator.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.TextView;

import hugo.weaving.DebugLog;

public final class Views {
    
    private Views() {}

    public static void setTVInRow(View row, @IdRes int textViewId, @StringRes int stringId) {
        TextView tv = (TextView) row.findViewById(textViewId);
        tv.setText(stringId);
    }

    public static void setTVInRow(View row, @IdRes int textViewId, String string) {
        setTV(row, textViewId, string);
    }

    public static void setTV(Activity activity, @IdRes int textViewId, String string) {
        TextView tv = (TextView) activity.findViewById(textViewId);
        tv.setText(string);
    }

    public static void setTV(View parent, @IdRes int textViewId, String string) {
        TextView tv = (TextView) parent.findViewById(textViewId);
        tv.setText(string);
    }

    @UiThread
    @DebugLog
    public static Bitmap buildBitmapFrom(View v) {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //v.layout(left, top, width, height);
        v.draw(c);
        return b;
    }

    public static String getText(TextInputLayout view) {
        //noinspection ConstantConditions
        return view.getEditText().getText().toString();
    }

    public static String getText(TextView view) {
        return view.getText().toString();
    }

    public static int getIntText(final TextView textView) {
        return Integer.parseInt(getText(textView));
    }
}
