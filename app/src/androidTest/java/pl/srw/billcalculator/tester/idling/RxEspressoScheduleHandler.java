package pl.srw.billcalculator.tester.idling;


import android.support.annotation.NonNull;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;

import io.reactivex.functions.Function;

/**
 * RxJava connector to Espresso idling resources
 */
public class RxEspressoScheduleHandler implements Function<Runnable, Runnable> {

    private final CountingIdlingResource countingIdlingResource = new CountingIdlingResource("rxJava");

    @Override
    public Runnable apply(@NonNull final Runnable runnable) throws Exception {
        return new Runnable() {
            @Override
            public void run() {
                countingIdlingResource.increment();

                try {
                    runnable.run();
                } finally {
                    countingIdlingResource.decrement();
                }
            }
        };
    }

    public IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }
}