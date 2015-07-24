package pl.srw.billcalculator.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.annotation.WorkerThread;
import android.view.View;
import android.widget.TextView;

import hugo.weaving.DebugLog;
import pl.srw.billcalculator.BillCalculator;

/**
 * Created by Kamil Seweryn.
 */
public final class Views {
    
    private Views() {}


    public static void setTVInRow(View row, @IdRes int textViewId, @StringRes int stringId) {
        TextView tv = (TextView) row.findViewById(textViewId);
        setTV(tv, stringId);
    }

    public static void setTVInRow(View row, @IdRes int textViewId, String string) {
        TextView tv = (TextView) row.findViewById(textViewId);
        setTV(tv, string);
    }

    public static void setTV(Activity activity, @IdRes int textViewId, String string) {
        TextView tv = (TextView) activity.findViewById(textViewId);
        setTV(tv, string);
    }

    public static void setTV(View parent, @IdRes int textViewId, String string) {
        TextView tv = (TextView) parent.findViewById(textViewId);
        setTV(tv, string);
    }
    
    public static void setTV(TextView tv, String string) {
        tv.setText(string);
    }

    public static void setTV(TextView tv, @StringRes int stringId) {
        tv.setText(BillCalculator.context.getString(stringId));
    }

    @WorkerThread
    @DebugLog
    public static Bitmap buildBitmapFrom(View v) {
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //v.layout(left, top, width, height);
        v.draw(c);
        return b;
    }

}
