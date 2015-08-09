package pl.srw.billcalculator.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kamil Seweryn.
 */
@StringDef({ContentType.SETTINGS, ContentType.HISTORY, ContentType.PGE_BILL, ContentType.PGNIG_BILL, ContentType.TAURON_BILL})
@Retention(RetentionPolicy.SOURCE)
public @interface ContentType {
    public static final String SETTINGS = "Settings";
    public static final String HISTORY = "History";
    public static final String TAURON_BILL = "TauronBill";
    public static final String PGNIG_BILL = "PgnigBill";
    public static final String PGE_BILL = "PgeBill";
}