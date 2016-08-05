package pl.srw.billcalculator.pojo;

import pl.srw.billcalculator.db.Prices;

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
