package pl.srw.billcalculator.di;

import dagger.MapKey;
import pl.srw.billcalculator.type.Provider;

@MapKey
@interface DependencyMapProviderKey {
    Provider value();
}
