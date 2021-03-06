package pl.srw.billcalculator.util.analytics;

import android.content.Context;

import java.util.Arrays;

import timber.log.Timber;

/**
 * Wrapper class for logging errors to on-line analytics tool.
 */
public class Analytics {

    private static boolean ENABLED = false;

    public static void enable(Context context) {
//        Fabric.with(context, new Crashlytics(), new Answers());
//        ENABLED = true;
    }
    /**
     * Logs user viewed content. This will be used to see statistics
     */
    public static void contentView(@ContentType String contentId, Object... args) {
        Timber.i("Content viewed: " + contentId + " " + Arrays.toString(args));
//        if (!ENABLED) return;

//        ContentViewEvent event = new ContentViewEvent();
//        event.putContentId(contentId);
//        for (int i = 0; i < getEvenLength(contentId, args); i+=2) {
//            event.putCustomAttribute(args[i].toString(), args[i + 1].toString());
//        }
//        Answers.getInstance().logContentView(event);
    }

    /**
     * Logs event happened. This will be used to see statistics
     */
    public static void event(@EventType String type, Object... args) {
        Timber.i("Event: " + type + " " + Arrays.toString(args));
//        if (!ENABLED) return;

//        CustomEvent customEvent = new CustomEvent(type);
//        for (int i = 0; i < getEvenLength(type, args); i+=2) {
//            customEvent.putCustomAttribute(args[i].toString(), args[i + 1].toString());
//        }
//        Answers.getInstance().logCustom(customEvent);
    }

    /**
     * Logs unexpected event happened. This will be used to see statistics
     */
    public static void warning(String event) {
        event(EventType.UNEXPECTED, "case", event);
    }

    /**
     * Logs unexpected event happened. This will be used to see statistics
     */
    public static void warning(String event, String key, Object value) {
        event(EventType.UNEXPECTED, "case", event, key, value);
    }

    private static int getEvenLength(String id, Object[] args) {
        if (args.length % 2 != 0) {
            warning("Args length not even", "Args not even for", id);
            return args.length - 1;
        }
        return args.length;
    }
}
