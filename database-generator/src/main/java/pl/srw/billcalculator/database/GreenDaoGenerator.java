package pl.srw.billcalculator.database;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Kamil Seweryn.
 */
public class GreenDaoGenerator {

    private static final String OUTPUT_DIR = "persistence/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "pl.srw.billcalculator.db");
        schema.setDefaultJavaPackageDao("pl.srw.billcalculator.db.dao");

        Entity pgePrices = addPgePrices(schema);
        addPgeG11Bill(schema, pgePrices);

        addPgeG12Bill(schema, pgePrices);

        Entity pgnigPrices = addPgnigPrices(schema);
        addPgnigBill(schema, pgnigPrices);

        Entity tauronPrices = addTauronPrices(schema);
        addTauronG11Bill(schema, tauronPrices);

        addHistory(schema);

        File output = new File(args.length == 1 ? args[0] : OUTPUT_DIR);
        new DaoGenerator().generateAll(schema, output.getAbsolutePath());
    }

    private static void addHistory(final Schema schema) {
        Entity history = schema.addEntity("History");

        history.addIdProperty().autoincrement();
        history.addDateProperty("dateFrom");
        history.addStringProperty("billType");
        history.addLongProperty("billId");
    }

    private static void addPgeG11Bill(Schema schema, final Entity pgePrices) {
        Entity pgeBill = schema.addEntity("PgeG11Bill");
        pgeBill.implementsInterface("Bill");

        pgeBill.addIdProperty().autoincrement();
        pgeBill.addIntProperty("readingFrom");
        pgeBill.addIntProperty("readingTo");

        pgeBill.addDateProperty("dateFrom");
        pgeBill.addDateProperty("dateTo");

        pgeBill.addDoubleProperty("amountToPay");

        Property pricesId = pgeBill.addLongProperty("pricesId").getProperty();
        pgeBill.addToOne(pgePrices, pricesId);
    }

    private static void addPgeG12Bill(final Schema schema, final Entity pgePrices) {
        Entity pgeBill = schema.addEntity("PgeG12Bill");
        pgeBill.implementsInterface("Bill");

        pgeBill.addIdProperty().autoincrement();
        pgeBill.addIntProperty("readingDayFrom");
        pgeBill.addIntProperty("readingDayTo");
        pgeBill.addIntProperty("readingNightFrom");
        pgeBill.addIntProperty("readingNightTo");

        pgeBill.addDateProperty("dateFrom");
        pgeBill.addDateProperty("dateTo");

        pgeBill.addDoubleProperty("amountToPay");

        Property pricesId = pgeBill.addLongProperty("pricesId").getProperty();
        pgeBill.addToOne(pgePrices, pricesId);
    }

    private static Entity addPgePrices(final Schema schema) {
        Entity pgePrices = schema.addEntity("PgePrices");
        pgePrices.implementsInterface("pl.srw.billcalculator.pojo.IPgePrices");
        pgePrices.implementsSerializable();

        pgePrices.addIdProperty().autoincrement();
        pgePrices.addStringProperty("zaEnergieCzynna");
        pgePrices.addStringProperty("skladnikJakosciowy");
        pgePrices.addStringProperty("oplataSieciowa");
        pgePrices.addStringProperty("oplataPrzejsciowa");
        pgePrices.addStringProperty("oplataStalaZaPrzesyl");
        pgePrices.addStringProperty("oplataAbonamentowa");

        pgePrices.addStringProperty("zaEnergieCzynnaDzien");
        pgePrices.addStringProperty("zaEnergieCzynnaNoc");
        pgePrices.addStringProperty("oplataSieciowaDzien");
        pgePrices.addStringProperty("oplataSieciowaNoc");

        return pgePrices;
    }

    private static void addPgnigBill(final Schema schema, final Entity pgnigPrices) {
        Entity pgnigBill = schema.addEntity("PgnigBill");
        pgnigBill.implementsInterface("Bill");

        pgnigBill.addIdProperty();
        pgnigBill.addIntProperty("readingFrom");
        pgnigBill.addIntProperty("readingTo");

        pgnigBill.addDateProperty("dateFrom");
        pgnigBill.addDateProperty("dateTo");

        pgnigBill.addDoubleProperty("amountToPay");

        Property pricesId = pgnigBill.addLongProperty("pricesId").getProperty();
        pgnigBill.addToOne(pgnigPrices, pricesId);
    }

    private static Entity addPgnigPrices(final Schema schema) {
        Entity pgnigPrices = schema.addEntity("PgnigPrices");
        pgnigPrices.implementsInterface("pl.srw.billcalculator.pojo.IPgnigPrices");
        pgnigPrices.implementsSerializable();

        pgnigPrices.addIdProperty().autoincrement();
        pgnigPrices.addStringProperty("oplataAbonamentowa");
        pgnigPrices.addStringProperty("paliwoGazowe");
        pgnigPrices.addStringProperty("dystrybucyjnaStala");
        pgnigPrices.addStringProperty("dystrybucyjnaZmienna");
        pgnigPrices.addStringProperty("wspolczynnikKonwersji");

        return pgnigPrices;
    }

    private static void addTauronG11Bill(final Schema schema, final Entity tauronPrices) {
        Entity bill = schema.addEntity("TauronG11Bill");
        bill.implementsInterface("Bill");

        bill.addIdProperty().autoincrement();
        bill.addIntProperty("readingFrom");
        bill.addIntProperty("readingTo");

        bill.addDateProperty("dateFrom");
        bill.addDateProperty("dateTo");

        bill.addDoubleProperty("amountToPay");

        Property pricesId = bill.addLongProperty("pricesId").getProperty();
        bill.addToOne(tauronPrices, pricesId);
    }

    private static Entity addTauronPrices(final Schema schema) {
        Entity prices = schema.addEntity("TauronPrices");
        prices.implementsInterface("pl.srw.billcalculator.pojo.ITauronPrices");
        prices.implementsSerializable();

        prices.addIdProperty().autoincrement();
        prices.addStringProperty("energiaElektrycznaCzynna");
        prices.addStringProperty("oplataDystrybucyjnaZmienna");
        prices.addStringProperty("oplataDystrybucyjnaStala");
        prices.addStringProperty("oplataPrzejsciowa");
        prices.addStringProperty("oplataAbonamentowa");

        prices.addStringProperty("energiaElektrycznaCzynnaDzien");
        prices.addStringProperty("energiaElektrycznaCzynnaNoc");
        prices.addStringProperty("oplataDystrybucyjnaZmiennaDzien");
        prices.addStringProperty("oplataDystrybucyjnaZmiennaNoc");

        return prices;
    }

}
