package pl.srw.billcalculator.tester.rule;

import android.app.Activity;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

/**
 * Test rule with permission granting before test execution and intent testing
 * @param <T> tested activity
 */
public class PermissionsIntentsTestRule<T extends Activity> extends IntentsTestRule<T> {

    private final String[] permissions;

    public PermissionsIntentsTestRule(Class<T> clazz, String... permissions) {
        super(clazz);
        this.permissions = permissions;
    }

    @Override
    protected void beforeActivityLaunched() {
        allowPermissions();
    }

    private void allowPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                        "pm grant " + InstrumentationRegistry.getTargetContext().getPackageName()
                                + " " + permission);
            }
        }
    }
}
