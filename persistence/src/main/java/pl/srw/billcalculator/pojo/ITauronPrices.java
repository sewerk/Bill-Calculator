package pl.srw.billcalculator.pojo;

import pl.srw.billcalculator.db.Prices;

/**
 * Created by kseweryn on 13.04.15.
 */
public interface ITauronPrices extends Prices {

    String getEnergiaElektrycznaCzynna();
    String getOplataDystrybucyjnaZmienna();
    String getOplataDystrybucyjnaStala();
    String getOplataPrzejsciowa();
    String getOplataAbonamentowa();

    String getOplataDystrybucyjnaZmiennaNoc();
    String getOplataDystrybucyjnaZmiennaDzien();
    String getEnergiaElektrycznaCzynnaNoc();
    String getEnergiaElektrycznaCzynnaDzien();
}