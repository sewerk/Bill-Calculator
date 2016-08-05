package pl.srw.billcalculator.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.type.Provider;

@Module
public class ApplicationModule {

    public static final String PRICES_PGE = "PGE";
    public static final String PRICES_PGNIG = "PGNIG";
    public static final String PRICES_TAURON = "TAURON";

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    protected SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    @Named(value = PRICES_PGE)
    protected RestorablePrices providePgeSharedPreferencesPrices(SharedPreferences prefs) {
        return new PgePrices(prefs);
    }

    @Provides
    @Singleton
    @Named(value = PRICES_PGNIG)
    protected RestorablePrices providePgnigSharedPreferencesPrices(SharedPreferences prefs) {
        return new PgnigPrices(prefs);
    }

    @Provides
    @Singleton
    @Named(value = PRICES_TAURON)
    protected RestorablePrices provideTauronSharedPreferencesPrices(SharedPreferences prefs) {
        return new TauronPrices(prefs);
    }

    @Provides
    @Singleton
    protected Provider[] providerProviders() {
        return Provider.values();
    }
}
