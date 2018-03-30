package pl.srw.billcalculator;

import org.junit.After;
import org.junit.Before;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

/**
 * Unit test base class which provides test scheduler instance for testing Rx calls.
 * To get results from async calls within test call {@link this#waitToFinish()}
 */
public class RxJavaBaseTest {

    protected TestScheduler testScheduler;

    @Before
    public void init() {
        testScheduler = new TestScheduler();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> testScheduler);
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> testScheduler);
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> testScheduler);
    }

    @After
    public void shutDown() {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    protected void waitToFinish() {
        testScheduler.triggerActions();
    }
}
