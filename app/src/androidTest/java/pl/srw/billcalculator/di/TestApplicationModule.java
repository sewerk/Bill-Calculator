package pl.srw.billcalculator.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.srw.billcalculator.data.ApplicationRepo;
import pl.srw.billcalculator.data.settings.PricesRepo;
import pl.srw.billcalculator.settings.prices.PgePrices;
import pl.srw.billcalculator.util.BillSelection;

@Module
class TestApplicationModule {

    private ApplicationComponent productionComponent;

    TestApplicationModule(ApplicationComponent productionComponent) {
        this.productionComponent = productionComponent;
    }

    @Provides
    @Singleton
    PgePrices providePgePrices() {
        return productionComponent.getPgePrices();
    }

    @Provides
    @Singleton
    ApplicationRepo provideApplicationRepo() {
        return productionComponent.getApplicationRepo();
    }

    @Provides
    @Singleton
    BillSelection provideBillSelection() {
        return productionComponent.getBillSelection();
    }

    @Provides
    @Singleton
    PricesRepo providePricesRepo() { return productionComponent.getPricesRepo(); }
}
