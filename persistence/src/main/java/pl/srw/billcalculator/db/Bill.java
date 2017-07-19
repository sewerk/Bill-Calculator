package pl.srw.billcalculator.db;

import java.util.Date;

public interface Bill {

    Long getId();
    Long getPricesId();

    Date getDateFrom();
    void setDateFrom(Date dateFrom);
    Date getDateTo();
    void setDateTo(Date dateTo);
    Double getAmountToPay();
    void setAmountToPay(Double value);
}
