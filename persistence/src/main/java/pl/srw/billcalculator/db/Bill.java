package pl.srw.billcalculator.db;

import java.util.Date;

/**
 * Created by Kamil Seweryn.
 */
public interface Bill {

    Date getDateFrom();
    void setDateFrom(Date dateFrom);
    Date getDateTo();
    void setDateTo(Date dateTo);
    Double getAmountToPay();
    void setAmountToPay(Double value);
}
