package pl.srw.billcalculator.testutils;

import pl.srw.billcalculator.di.DaggerTestApplicationComponent;
import pl.srw.billcalculator.di.TestApplicationComponent;
import pl.srw.billcalculator.wrapper.Dependencies;

/**
 * Setup test component to access production instances
 */
public class TestDependencies {

    private static TestApplicationComponent component = DaggerTestApplicationComponent.builder()
            .applicationModule(Dependencies.getApplicationComponent().getApplicationModule())
            .build();

    public static TestApplicationComponent getTestApplicationComponent() {
        Dependencies.setApplicationComponent(component);
        return component;
    }
}
