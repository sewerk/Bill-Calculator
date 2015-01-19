package pl.srw.billcalculator.db;

import java.util.Date;

/**
 * Created by Kamil Seweryn.
 */
public interface Bill {

    Date getDateFrom();
    Date getDateTo();
    Double getAmountToPay();
}
