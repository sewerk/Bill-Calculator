package pl.srw.billcalculator.bill.task;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;

import hugo.weaving.DebugLog;

/**
 * Created by kseweryn on 02.07.15.
 */

public class TaskManager {

    private static final TaskManager INSTANCE = new TaskManager();
    private final SimpleArrayMap<String, AsyncTask> tasks;

    private TaskManager() {
        tasks = new SimpleArrayMap<>();
    }

    public static TaskManager getInstance() {
        return INSTANCE;
    }

    @DebugLog
    public void register(String key, AsyncTask task) {
        tasks.put(key, task);
    }

    @DebugLog
    public AsyncTask findBy(String key) {
        return tasks.get(key);
    }

    public void unregister(String key) {
        tasks.remove(key);
    }
}
