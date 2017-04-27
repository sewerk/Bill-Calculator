package pl.srw.billcalculator.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({ActionType.PRINT, ActionType.RESTORE_PRICES, ActionType.BACKUP_RESTORE, ActionType.DELETE_BILL})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
    String PRINT = "Print";
    String RESTORE_PRICES = "Restore default prices";
    String BACKUP_RESTORE = "Restore backup";
    String DELETE_BILL = "Delete bill";
}
