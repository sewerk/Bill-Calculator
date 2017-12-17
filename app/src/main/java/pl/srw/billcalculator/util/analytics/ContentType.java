package pl.srw.billcalculator.util.analytics;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static pl.srw.billcalculator.util.analytics.ContentType.ABOUT;
import static pl.srw.billcalculator.util.analytics.ContentType.BILL;
import static pl.srw.billcalculator.util.analytics.ContentType.FORM;
import static pl.srw.billcalculator.util.analytics.ContentType.HISTORY_HELP;
import static pl.srw.billcalculator.util.analytics.ContentType.SETTINGS;
import static pl.srw.billcalculator.util.analytics.ContentType.SETTINGS_HELP;

@StringDef({BILL, SETTINGS, ABOUT, SETTINGS_HELP, HISTORY_HELP, FORM})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentType {
    String BILL = "Bill";
    String SETTINGS = "Settings";
    String ABOUT = "About";
    String SETTINGS_HELP = "Settings Help";
    String HISTORY_HELP = "History help";
    String FORM = "Form";
}