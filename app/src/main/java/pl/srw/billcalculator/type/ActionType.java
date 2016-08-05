package pl.srw.billcalculator.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Kamil Seweryn.
 */
@StringDef({ActionType.PRINT, ActionType.RESTORE_PRICES, ActionType.WARNING})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
    String PRINT = "Print";
    String RESTORE_PRICES = "Restore default prices";
    String WARNING = "Warning";
}
