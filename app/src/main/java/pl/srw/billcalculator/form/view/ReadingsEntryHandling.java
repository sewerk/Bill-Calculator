package pl.srw.billcalculator.form.view;

import android.support.annotation.StringRes;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kseweryn on 23.09.15.
 */
public interface ReadingsEntryHandling {

    String getText();
    EditText getEditText();
    void setErrorView(TextView errorView);
    void setError(CharSequence errorMsg);
    void setError(@StringRes int errorMsgRes);
    void setHint(CharSequence hint);

}
