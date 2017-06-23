package pl.srw.billcalculator;

import org.junit.After;
import org.junit.Before;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
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
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return testScheduler;
            }
        });
        RxAndroidPlugins.setMainThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return testScheduler;
            }
        });
    }

    @After
    public void shutDown() throws Exception {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    protected void waitToFinish() {
        testScheduler.triggerActions();
    }
}
