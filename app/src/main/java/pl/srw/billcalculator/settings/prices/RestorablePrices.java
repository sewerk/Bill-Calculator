package pl.srw.billcalculator.settings.prices;

public interface RestorablePrices {

    void clear();

    void setDefaultIfNotSet();

    void setDefault();
}
