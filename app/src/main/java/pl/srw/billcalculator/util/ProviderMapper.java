package pl.srw.billcalculator.util;

import java.util.Map;

import javax.inject.Inject;

import pl.srw.billcalculator.settings.prices.SharedPrefsPrices;
import pl.srw.billcalculator.type.Provider;

public class ProviderMapper {

    private final Map<Provider, SharedPrefsPrices> pricesMap;

    @Inject
    public ProviderMapper(Map<Provider, SharedPrefsPrices> pricesMap) {
        this.pricesMap = pricesMap;
    }

    public SharedPrefsPrices getPrefsPrices(Provider provider) {
        return pricesMap.get(provider);
    }
}
