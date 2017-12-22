package pl.srw.billcalculator.util.analytics;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static pl.srw.billcalculator.util.analytics.EventType.BACKUP_RESTORE;
import static pl.srw.billcalculator.util.analytics.EventType.CALCULATE;
import static pl.srw.billcalculator.util.analytics.EventType.CHECK_PRICES;
import static pl.srw.billcalculator.util.analytics.EventType.CONTACT;
import static pl.srw.billcalculator.util.analytics.EventType.DELETE_BILL;
import static pl.srw.billcalculator.util.analytics.EventType.PERMISSION;
import static pl.srw.billcalculator.util.analytics.EventType.PRINT;
import static pl.srw.billcalculator.util.analytics.EventType.RESTORE_PRICES;
import static pl.srw.billcalculator.util.analytics.EventType.UNEXPECTED;

@StringDef({PRINT, RESTORE_PRICES, BACKUP_RESTORE, DELETE_BILL, CALCULATE, CHECK_PRICES, CONTACT, PERMISSION, UNEXPECTED})
@Retention(RetentionPolicy.SOURCE)
public @interface EventType {
    String PRINT = "Print";
    String RESTORE_PRICES = "Restore default prices";
    String BACKUP_RESTORE = "Restore backup";
    String DELETE_BILL = "Delete bill";
    String CALCULATE = "Calculate bill";
    String CHECK_PRICES = "Check prices dialog";
    String CONTACT = "About contact";
    String PERMISSION = "Requesting permission";
    String UNEXPECTED = "Unexpected situation";
}
