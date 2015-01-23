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

    public static final String OUTPUT_DIR = "persistence/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "pl.srw.billcalculator.db");
        schema.setDefaultJavaPackageDao("pl.srw.billcalculator.db.dao");

        Entity pgePrices = addPgePrices(schema);
        addPgeBill(schema, pgePrices);

        Entity pgeG12Prices = addPgeG12Prices(schema);
        addPgeG12Bill(schema, pgeG12Prices);

        Entity pgnigPrices = addPgnigPrices(schema);
        addPgnigBill(schema, pgnigPrices);

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

    private static void addPgeBill(Schema schema, final Entity pgePrices) {
        Entity pgeBill = schema.addEntity("PgeBill");
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

    private static Entity addPgeG12Prices(final Schema schema) {
        Entity pgePrices = schema.addEntity("PgeG12Prices");

        pgePrices.addIdProperty().autoincrement();
        pgePrices.addStringProperty("cenaZaEnergieCzynnaDzien");
        pgePrices.addStringProperty("cenaZaEnergieCzynnaNoc");
        pgePrices.addStringProperty("cenaSkladnikJakosciowy");
        pgePrices.addStringProperty("cenaOplataSieciowaDzien");
        pgePrices.addStringProperty("cenaOplataSieciowaNoc");
        pgePrices.addStringProperty("cenaOplataPrzejsciowa");
        pgePrices.addStringProperty("cenaOplStalaZaPrzesyl");
        pgePrices.addStringProperty("cenaOplataAbonamentowa");

        return pgePrices;
    }

    private static void addPgeG12Bill(final Schema schema, final Entity pgeG12Prices) {
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
        pgeBill.addToOne(pgeG12Prices, pricesId);
    }

    private static Entity addPgePrices(final Schema schema) {
        Entity pgePrices = schema.addEntity("PgePrices");

        pgePrices.addIdProperty().autoincrement();
        pgePrices.addStringProperty("cenaZaEnergieCzynna");
        pgePrices.addStringProperty("cenaSkladnikJakosciowy");
        pgePrices.addStringProperty("cenaOplataSieciowa");
        pgePrices.addStringProperty("cenaOplataPrzejsciowa");
        pgePrices.addStringProperty("cenaOplStalaZaPrzesyl");
        pgePrices.addStringProperty("cenaOplataAbonamentowa");

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

        pgnigPrices.addIdProperty().autoincrement();
        pgnigPrices.addStringProperty("oplataAbonamentowa");
        pgnigPrices.addStringProperty("paliwoGazowe");
        pgnigPrices.addStringProperty("dystrybucyjnaStala");
        pgnigPrices.addStringProperty("dystrybucyjnaZmienna");
        pgnigPrices.addStringProperty("wspKonwersji");

        return pgnigPrices;
    }
}
