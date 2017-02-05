package pl.srw.billcalculator.wrapper;

import android.content.Context;

import pl.srw.billcalculator.BuildConfig;
import timber.log.Timber;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.ContentType;

/**
 * Wrapper class for logging errors to on-line analytics tool.
 */
public class Analytics {

    private static final boolean ENABLED = !BuildConfig.DEBUG;

    public static void initialize(Context context) {
        if (ENABLED) {
            Fabric.with(context, new Crashlytics());
            Fabric.with(context, new Answers());
        }
    }

    /**
     * Logs key-value pair to help verify critical error. Please note this
     * won't be visible until critical error occur.
     * @param key
     * @param val
     */
    public static void setInt(String key, int val) {
        if (ENABLED)
            Crashlytics.setInt(key, val);
    }

    /**
     * Logs key-value pair to help verify critical error. Please note this
     * won't be visible until critical error occur.
     * @param key
     * @param val
     */
    public static void setString(String key, String val) {
        if (ENABLED)
            Crashlytics.setString(key, val);
    }

    /**
     * Log message to help verify critical error. Please note this log
     * won't be visible until critical error occur.
     * @param message debug log
     */
    public static void log(String message) {
        Timber.d(message);
        if (ENABLED)
            Crashlytics.log(message);
    }

    /**
     * Logs warnings for unexpected situations.
     * @param message warning
     */
    public static void warning(String message) {
        Timber.w(message);
        Analytics.log(message);
    }

    /**
     * Logs caught exception.
     * @param exception
     */
    public static void error(Throwable exception) {
        Timber.e(exception.getMessage(), exception);
        Analytics.log(exception.getMessage());
        if (ENABLED)
            Crashlytics.logException(exception);
    }

    /**
     * Logs user viewed content.
     * @param contentId
     * @param args
     */
    public static void logContent(@ContentType String contentId, String... args) {
        final String message = "Content viewed: " + contentId + " " + Arrays.toString(args);
        Analytics.log(message);
        if (!ENABLED || args.length % 2 != 0) return;

        ContentViewEvent event = new ContentViewEvent();
        event.putContentId(contentId);
        for (int i = 0; i < args.length; i+=2) {
            event.putCustomAttribute(args[i], args[i + 1]);
        }
        Answers.getInstance().logContentView(event);
    }

    /**
     * Logs user took action.
     * @param action
     * @param args
     */
    public static void logAction(@ActionType String action, String... args) {
        final String message = "Action triggered: " + action + " " + Arrays.toString(args);
        Analytics.log(message);
        if (!ENABLED || args.length % 2 != 0) return;

        CustomEvent customEvent = new CustomEvent(action);
        for (int i = 0; i < args.length; i+=2) {
            customEvent.putCustomAttribute(args[i], args[i + 1]);
        }
        Answers.getInstance().logCustom(customEvent);
    }
}
