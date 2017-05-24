package pl.srw.billcalculator.type;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static pl.srw.billcalculator.type.ActionType.BACKUP_RESTORE;
import static pl.srw.billcalculator.type.ActionType.CALCULATE;
import static pl.srw.billcalculator.type.ActionType.DELETE_BILL;
import static pl.srw.billcalculator.type.ActionType.OPEN_BILL;
import static pl.srw.billcalculator.type.ActionType.PRINT;
import static pl.srw.billcalculator.type.ActionType.RESTORE_PRICES;

@StringDef({PRINT, RESTORE_PRICES, BACKUP_RESTORE, DELETE_BILL, CALCULATE, OPEN_BILL})
@Retention(RetentionPolicy.SOURCE)
public @interface ActionType {
    String PRINT = "Print";
    String RESTORE_PRICES = "Restore default prices";
    String BACKUP_RESTORE = "Restore backup";
    String DELETE_BILL = "Delete bill";
    String CALCULATE = "Calculate bill";
    String OPEN_BILL = "Open bill";
}
