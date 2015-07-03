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
