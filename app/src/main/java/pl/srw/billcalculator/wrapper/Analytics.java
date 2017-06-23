package pl.srw.billcalculator.wrapper;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import pl.srw.billcalculator.BuildConfig;
import pl.srw.billcalculator.type.ActionType;
import pl.srw.billcalculator.type.ContentType;
import timber.log.Timber;

/**
 * Wrapper class for logging errors to on-line analytics tool.
 */
public class Analytics {

    private static boolean ENABLED = false;

    public static void initialize(Context context) {
        ENABLED = !BuildConfig.DEBUG;
        if (ENABLED) {
            Fabric.with(context, new Crashlytics(), new Answers());
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
     * @param message
     */
    public static void error(Throwable exception, String message) {
        Timber.e(exception, message);
        Analytics.log(exception.getMessage());
        if (ENABLED)
            Crashlytics.logException(exception);
    }

    /**
     * Logs user viewed content.
     * @param contentId
     * @param args
     */
    public static void logContent(@ContentType String contentId, Object... args) {
        final String message = "Content viewed: " + contentId + " " + Arrays.toString(args);
        Analytics.log(message);
        if (!ENABLED || args.length % 2 != 0) return;

        ContentViewEvent event = new ContentViewEvent();
        event.putContentId(contentId);
        for (int i = 0; i < args.length; i+=2) {
            event.putCustomAttribute(args[i].toString(), args[i + 1].toString());
        }
        Answers.getInstance().logContentView(event);
    }

    /**
     * Logs user took action.
     * @param action
     * @param args
     */
    public static void logAction(@ActionType String action, Object... args) {
        final String message = "Action triggered: " + action + " " + Arrays.toString(args);
        Analytics.log(message);
        if (!ENABLED || args.length % 2 != 0) return;

        CustomEvent customEvent = new CustomEvent(action);
        for (int i = 0; i < args.length; i+=2) {
            customEvent.putCustomAttribute(args[i].toString(), args[i + 1].toString());
        }
        Answers.getInstance().logCustom(customEvent);
    }
}
