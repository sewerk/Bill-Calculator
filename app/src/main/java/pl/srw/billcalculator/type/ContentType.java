package pl.srw.billcalculator.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({ContentType.SETTINGS, ContentType.HISTORY, ContentType.ABOUT, ContentType.HELP, ContentType.CHECK_PRICES,
        ContentType.PGE_BILL, ContentType.PGNIG_BILL, ContentType.TAURON_BILL})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentType {
    String SETTINGS = "Settings";
    String HISTORY = "History";
    String TAURON_BILL = "TauronBill";
    String PGNIG_BILL = "PgnigBill";
    String PGE_BILL = "PgeBill";
    String ABOUT = "About";
    String HELP = "Help";
    String CHECK_PRICES = "Check prices";
}