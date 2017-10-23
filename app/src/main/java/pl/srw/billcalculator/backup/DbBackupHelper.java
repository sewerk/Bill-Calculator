package pl.srw.billcalculator.backup;

import android.app.backup.FileBackupHelper;
import android.content.Context;

class DbBackupHelper extends FileBackupHelper {

    DbBackupHelper(Context ctx, String dbName) {
        super(ctx, "../databases/" + dbName);
    }
}
