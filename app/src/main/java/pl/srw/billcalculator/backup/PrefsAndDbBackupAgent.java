package pl.srw.billcalculator.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Keep;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import pl.srw.billcalculator.di.ApplicationModule;
import pl.srw.billcalculator.persistence.Database;

/**
 * Created by Kamil Seweryn on 07.02.2016.
 */
@Keep
public class PrefsAndDbBackupAgent extends BackupAgentHelper {

    private static final String TAG = "PrefsAndDbBackupAgent";

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
        Log.d(TAG, "onBackup");
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
        Log.d(TAG, "onRestore appVersionCode = " + appVersionCode);

        lock.lock();
        try {
            Log.d(TAG, "onRestore in-lock");
            super.onRestore(data, appVersionCode, newState);
        } finally {
            lock.unlock();
        }
    }
}
