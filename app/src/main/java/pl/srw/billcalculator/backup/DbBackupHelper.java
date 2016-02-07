package pl.srw.billcalculator.backup;

import android.app.backup.FileBackupHelper;
import android.content.Context;

/**
 * Created by Kamil Seweryn on 07.02.2016.
 */
public class DbBackupHelper extends FileBackupHelper {

    public DbBackupHelper(Context ctx, String dbName) {
        super(ctx, "../databases/" + dbName);
    }
}
