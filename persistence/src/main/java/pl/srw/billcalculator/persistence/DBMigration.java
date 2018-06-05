package pl.srw.billcalculator.persistence;

import pl.srw.billcalculator.db.dao.TauronG11BillDao;
import pl.srw.billcalculator.db.dao.TauronG12BillDao;
import pl.srw.billcalculator.db.dao.TauronPricesDao;
import timber.log.Timber;

/**
 * Migration done on db schema:
 * ver.1: PGE and PGNiG bill and prices tables
 * ver.2: Tauron bill and prices tables
 * ver.3: oplata_oze column added to PGE and Tauron
 * var.4: oplata_handlowa added to PGE, PGNIG and Tauron
 */
class DBMigration {

    static void migrate(final org.greenrobot.greendao.database.Database db, final int oldVersion, final int newVersion) {
        Timber.i("Upgrading schema from version %d to %d", oldVersion, newVersion);
        switch (oldVersion) {
            case 1:
                migrate_1_4(db);
                break; // conflict with 2-4
            case 2:
                migrate_2_3(db);
            case 3:
                migrate_3_4(db);
        }
    }

    private static void migrate_1_4(final org.greenrobot.greendao.database.Database db) {
        TauronPricesDao.createTable(db, true); // this will handle partially the migrate_2_4
        TauronG11BillDao.createTable(db, true);
        TauronG12BillDao.createTable(db, true);

        // more migrate_2_3
        addOplataOzeColumnTo(db, "PGE_PRICES");
        // more migrate_3_4
        addOplataHandlowaColumnTo(db, "PGE_PRICES");
        addOplataHandlowaColumnTo(db, "PGNIG_PRICES");
    }

    private static void migrate_2_3(org.greenrobot.greendao.database.Database db) {
        addOplataOzeColumnTo(db, "PGE_PRICES");
        addOplataOzeColumnTo(db, "TAURON_PRICES");
    }

    private static void migrate_3_4(org.greenrobot.greendao.database.Database db) {
        addOplataHandlowaColumnTo(db, "PGE_PRICES");
        addOplataHandlowaColumnTo(db, "PGNIG_PRICES");
        addOplataHandlowaColumnTo(db, "TAURON_PRICES");
    }

    private static void addOplataHandlowaColumnTo(org.greenrobot.greendao.database.Database db, String tableName) {
        addColumnTo(db, tableName, "OPLATA_HANDLOWA");
    }

    private static void addOplataOzeColumnTo(org.greenrobot.greendao.database.Database db, String tableName) {
        addColumnTo(db, tableName, "OPLATA_OZE");
    }

    private static void addColumnTo(org.greenrobot.greendao.database.Database db, String tableName, final String columnName) {
        db.execSQL("ALTER TABLE " + tableName + " ADD COLUMN '" + columnName + "' TEXT DEFAULT '0.00';");
    }
}
