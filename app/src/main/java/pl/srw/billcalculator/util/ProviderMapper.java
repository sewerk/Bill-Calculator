package pl.srw.billcalculator.util;

import java.util.Map;

import javax.inject.Inject;

import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.type.Provider;

public class ProviderMapper {

    private Map<Provider, RestorablePrices> pricesMap;

    @Inject
    public ProviderMapper(Map<Provider, RestorablePrices> pricesMap) {
        this.pricesMap = pricesMap;
    }

    public RestorablePrices getPrices(Provider provider) {
        return pricesMap.get(provider);
    }
}
