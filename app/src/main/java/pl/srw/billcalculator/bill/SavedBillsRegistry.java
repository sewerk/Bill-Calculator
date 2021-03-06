package pl.srw.billcalculator.bill;

import android.support.v4.util.SimpleArrayMap;

import org.threeten.bp.LocalDate;

import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.Prices;
import pl.srw.billcalculator.db.TauronG11Bill;
import pl.srw.billcalculator.db.TauronG12Bill;
import pl.srw.billcalculator.pojo.IPgePrices;
import pl.srw.billcalculator.pojo.IPgnigPrices;
import pl.srw.billcalculator.pojo.ITauronPrices;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.analytics.Analytics;
import timber.log.Timber;

@Singleton
public class SavedBillsRegistry {

    private final SimpleArrayMap<String, Long> registry;

    @Inject
    SavedBillsRegistry() {
        registry = new SimpleArrayMap<>();
    }

    public String register(Bill bill) {
        Timber.d("register() called with: bill = [%s]", bill);
        String key;
        if (bill instanceof PgnigBill) {
            PgnigBill b = (PgnigBill) bill;
            key = getKey(Provider.PGNIG, b.getReadingFrom(), b.getReadingTo(), 0, 0, Dates.toLocalDate(b.getDateFrom()), Dates.toLocalDate(b.getDateTo()), b.getPgnigPrices());
        } else if (bill instanceof PgeG11Bill) {
            PgeG11Bill b = (PgeG11Bill) bill;
            key = getKey(Provider.PGE, b.getReadingFrom(), b.getReadingTo(), 0, 0, Dates.toLocalDate(b.getDateFrom()), Dates.toLocalDate(b.getDateTo()), b.getPgePrices());
        } else if (bill instanceof PgeG12Bill) {
            PgeG12Bill b = (PgeG12Bill) bill;
            key = getKey(Provider.PGE, b.getReadingDayFrom(), b.getReadingDayTo(), b.getReadingNightFrom(), b.getReadingNightTo(), Dates.toLocalDate(b.getDateFrom()), Dates.toLocalDate(b.getDateTo()), b.getPgePrices());
        } else if (bill instanceof TauronG11Bill) {
            TauronG11Bill b = (TauronG11Bill) bill;
            key = getKey(Provider.TAURON, b.getReadingFrom(), b.getReadingTo(), 0, 0, Dates.toLocalDate(b.getDateFrom()), Dates.toLocalDate(b.getDateTo()), b.getTauronPrices());
        } else if (bill instanceof TauronG12Bill) {
            TauronG12Bill b = (TauronG12Bill) bill;
            key = getKey(Provider.TAURON, b.getReadingDayFrom(), b.getReadingDayTo(), b.getReadingNightFrom(), b.getReadingNightTo(), Dates.toLocalDate(b.getDateFrom()), Dates.toLocalDate(b.getDateTo()), b.getTauronPrices());
        } else
            throw new RuntimeException("Unknown type " + bill.getClass().getSimpleName());
        if (!registry.containsKey(key))
            registry.put(key, bill.getId());
        return key;
    }

    public String getIdentifier(Provider provider, int readingFrom, int readingTo, LocalDate dateFrom, LocalDate dateTo, Prices prices) {
        return getIdentifier(provider, readingFrom, readingTo, 0, 0, dateFrom, dateTo, prices);
    }

    public String getIdentifier(Provider provider, int readingFrom, int readingTo, int readingFrom2, int readingTo2, LocalDate dateFrom, LocalDate dateTo, Prices prices) {
        Timber.d("getIdentifier() called with: provider = [%s], readingFrom = [%d], readingTo = [%d], readingFrom2 = [%d], readingTo2 = [%d], dateFrom = [%s], dateTo = [%s], prices = [%s]",
                provider, readingFrom, readingTo, readingFrom2, readingTo2, dateFrom, dateTo, prices);

        final Long id = getId(provider, readingFrom, readingTo, readingFrom2, readingTo2, dateFrom, dateTo, prices);
        final String prefix = provider == Provider.PGNIG ? provider.toString()
                : (readingTo2 > 0 ? provider.toString() + "12" : provider.toString() + "11");
        return prefix + "_" + Dates.format(dateTo, "ddMMyyyy") + id;
    }

    private Long getId(Provider provider, int readingFrom, int readingTo, int readingFrom2, int readingTo2, LocalDate dateFrom, LocalDate dateTo, Prices prices) {
        String key = getKey(provider, readingFrom, readingTo, readingFrom2, readingTo2, dateFrom, dateTo, prices);
        Long foundId = registry.get(key);
        if (foundId == null) {
            Analytics.warning("SavedBillsRegistry is missing ID", "missing for key", key);
            foundId = (long) (1000 + new Random().nextInt(9000));//1000-9999
        }
        return foundId;
    }

    private String getKey(Provider provider, int readingFrom, int readingTo, int readingFrom2, int readingTo2, LocalDate dateFrom, LocalDate dateTo, Prices prices) {
        //noinspection StringBufferReplaceableByString
        return new StringBuilder()
                .append(provider.toString()).append("_")
                .append(readingFrom).append("_")
                .append(readingTo).append("_")
                .append(readingFrom2).append("_")
                .append(readingTo2).append("_")
                .append(dateFrom).append("_")
                .append(dateTo).append("_")
                .append(getKey(prices))
                .toString();
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private String getKey(Prices prices) {
        if (prices instanceof IPgePrices) {
            IPgePrices p = (IPgePrices) prices;
            return new StringBuilder()
                    .append(p.getZaEnergieCzynna()).append("_")
                    .append(p.getOplataHandlowa()).append("_")
                    .append(p.getSkladnikJakosciowy()).append("_")
                    .append(p.getOplataSieciowa()).append("_")
                    .append(p.getOplataPrzejsciowa()).append("_")
                    .append(p.getOplataStalaZaPrzesyl()).append("_")
                    .append(p.getOplataAbonamentowa()).append("_")
                    .append(p.getZaEnergieCzynnaDzien()).append("_")
                    .append(p.getZaEnergieCzynnaNoc()).append("_")
                    .append(p.getOplataSieciowaDzien()).append("_")
                    .append(p.getOplataSieciowaNoc()).toString();
        } else if (prices instanceof IPgnigPrices) {
            IPgnigPrices p = (IPgnigPrices) prices;
            return new StringBuilder()
                    .append(p.getOplataAbonamentowa()).append("_")
                    .append(p.getOplataHandlowa()).append("_")
                    .append(p.getPaliwoGazowe()).append("_")
                    .append(p.getDystrybucyjnaStala()).append("_")
                    .append(p.getDystrybucyjnaZmienna()).append("_")
                    .append(p.getWspolczynnikKonwersji()).toString();
        } else if (prices instanceof ITauronPrices) {
            ITauronPrices p = (ITauronPrices) prices;
            return new StringBuilder()
                    .append(p.getEnergiaElektrycznaCzynna()).append("_")
                    .append(p.getOplataHandlowa()).append("_")
                    .append(p.getOplataDystrybucyjnaZmienna()).append("_")
                    .append(p.getOplataDystrybucyjnaStala()).append("_")
                    .append(p.getOplataPrzejsciowa()).append("_")
                    .append(p.getOplataAbonamentowa()).append("_")
                    .append(p.getOplataDystrybucyjnaZmiennaNoc()).append("_")
                    .append(p.getOplataDystrybucyjnaZmiennaDzien()).append("_")
                    .append(p.getEnergiaElektrycznaCzynnaNoc()).append("_")
                    .append(p.getEnergiaElektrycznaCzynnaDzien()).toString();
        }
        throw new RuntimeException("Unknown type: " + prices.getClass().getSimpleName());
    }
}
