package pl.srw.billcalculator.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Keep;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pl.srw.billcalculator.BuildConfig;
import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.wrapper.Analytics;
import timber.log.Timber;

@Keep
public class PrefsAndDbBackupAgent extends BackupAgentHelper {

    private final Lock lock = new ReentrantLock();

    @Override
    public void onCreate() {
        addHelper("PREFS_BACKUP_KEY", new SharedPreferencesBackupHelper(this,
                ApplicationModule.SHARED_PREFERENCES_FILE,
                getPackageName() + "_preferences"));
        addHelper("DB_BACKUP_KEY", new DbBackupHelper(this, Database.DB_NAME));
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                         ParcelFileDescriptor newState) throws IOException {
        Timber.d("onBackup");
        lock.lock();
        try {
            super.onBackup(oldState, data, newState);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState)
            throws IOException {
        Analytics.logAction(ActionType.BACKUP_RESTORE, "onRestore -> current",
                appVersionCode + "/" + BuildConfig.VERSION_CODE);

        lock.lock();
        try {
            Timber.d("onRestore in-lock");
            super.onRestore(data, appVersionCode, newState);
            if (appVersionCode < 22) {
                // migrate db to db.ver=3
                Database.close();
                Database.initialize(this.getApplicationContext());
            }
        } finally {
            lock.unlock();
        }
    }
}
