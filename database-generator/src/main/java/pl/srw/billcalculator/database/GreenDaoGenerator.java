package pl.srw.billcalculator.database;

import java.io.File;
import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Kamil Seweryn.
 */
public class GreenDaoGenerator {

    public static final String OUTPUT_DIR = "persistence/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "pl.srw.billcalculator");
        
        addPgeBill(schema);
        //TODO: add pgnig

        File output = new File(args.length == 1 ? args[0] : OUTPUT_DIR);
        new DaoGenerator().generateAll(schema, output.getAbsolutePath());
    }

    private static void addPgeBill(Schema schema) {
        Entity pgeBill = schema.addEntity("PgeBill");
        pgeBill.addIdProperty().autoincrement();
        pgeBill.addIntProperty("readingFrom");
        pgeBill.addIntProperty("readingTo");
        pgeBill.addIntProperty("readingDayFrom");
        pgeBill.addIntProperty("readingDayTo");
        pgeBill.addIntProperty("readingNightFrom");
        pgeBill.addIntProperty("readingNightTo");
        
        pgeBill.addDateProperty("dateFrom");
        pgeBill.addDateProperty("dateTo");

        pgeBill.addStringProperty("cenaZaEnergieCzynna");
        pgeBill.addStringProperty("cenaSkladnikJakosciowy");
        pgeBill.addStringProperty("cenaOplataSieciowa");
        pgeBill.addStringProperty("cenaOplataPrzejsciowa");
        pgeBill.addStringProperty("cenaOplStalaZaPrzesyl");
        pgeBill.addStringProperty("cenaOplataAbonamentowa");
        pgeBill.addStringProperty("cenaZaEnergieCzynnaDzien");
        pgeBill.addStringProperty("cenaZaEnergieCzynnaNoc");
        pgeBill.addStringProperty("cenaOplataSieciowaDzien");
        pgeBill.addStringProperty("cenaOplataSieciowaNoc");
    }
}
