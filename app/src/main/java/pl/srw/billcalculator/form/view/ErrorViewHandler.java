package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import pl.srw.billcalculator.R;

/**
 * Created by kseweryn on 25.09.15.
 */
class ErrorViewHandler {

    private final View handlingView;
    private TextView errorView;
    @IdRes private int errorViewId;

    public ErrorViewHandler(View view, AttributeSet attrs, int defStyleAttr) {
        this.handlingView = view;
        readCustomAttrs(view.getContext(), attrs, defStyleAttr);
    }

    private void readCustomAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ReadingsEntryView,
                defStyleAttr, 0);

        try {
            errorViewId = a.getResourceId(R.styleable.ReadingsEntryView_errorView, 0);
        } finally {
            a.recycle();
        }
    }

    public void setView(TextView view) {
        errorView = view;
    }

    private TextView obtainView(View view) {
        if (errorView == null)
            errorView = (TextView) view.getRootView().findViewById(errorViewId);
        return errorView;
    }

    public void setError(@StringRes int errorMsgRes) {
        obtainView(handlingView).setText(errorMsgRes);
    }

    public void setError(CharSequence errorMsg) {
//        if (errorMsg == null && errorView == null) return;
        obtainView(handlingView).setText(errorMsg);
    }
}
