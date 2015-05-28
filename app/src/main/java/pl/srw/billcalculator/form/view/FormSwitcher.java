package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ViewAnimator;

/**
 * Created by kseweryn on 28.05.15.
 */
public class FormSwitcher extends ViewAnimator {

    private int initialChild;

    public FormSwitcher(Context context) {
        super(context);
    }

    public FormSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("displayedChild", getDisplayedChild());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            initialChild = bundle.getInt("displayedChild");
            setDisplayedChild(initialChild);
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
