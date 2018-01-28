package pl.srw.billcalculator.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.data.settings.prices.PricesRepo;
import pl.srw.billcalculator.data.settings.prices.PricesRepoImpl;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.settings.prices.PgnigPrices;
import pl.srw.billcalculator.settings.prices.RestorablePrices;
import pl.srw.billcalculator.settings.prices.TauronPrices;
import pl.srw.billcalculator.type.Provider;

@Module
public class ApplicationModule {

    public static final String GLOBAL_SHARED_PREFS = "global";
    public static final String SHARED_PREFERENCES_FILE = "PreferencesFile";
    //    private static final String DEFAULT_SHARED_PREFS = "default";

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
//    @Named(DEFAULT_SHARED_PREFS) does not work (with map dependency?)
    SharedPreferences provideDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides(type = Provides.Type.MAP)
    @Singleton
    @DependencyMapProviderKey(Provider.PGE)
    RestorablePrices providePgeSharedPreferencesPrices(SharedPreferences prefs) {
        return new PgePrices(prefs);
    }

    @Provides(type = Provides.Type.MAP)
    @Singleton
    @DependencyMapProviderKey(Provider.PGNIG)
    RestorablePrices providePgnigSharedPreferencesPrices(SharedPreferences prefs) {
        return new PgnigPrices(prefs);
    }

    @Provides(type = Provides.Type.MAP)
    @Singleton
    @DependencyMapProviderKey(Provider.TAURON)
    RestorablePrices provideTauronSharedPreferencesPrices(SharedPreferences prefs) {
        return new TauronPrices(prefs);
    }

    @Provides
    @Singleton
    @Named(GLOBAL_SHARED_PREFS)
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Provider[] providerProviders() {
        return Provider.values();
    }

    @Provides
    @Singleton
    String providePrintPathDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + context.getString(R.string.print_dir);
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return context.getResources();
    }

    @Provides
    @Singleton
    PricesRepo providePricesRepo(PricesRepoImpl repo) {
        return repo;
    }
}
