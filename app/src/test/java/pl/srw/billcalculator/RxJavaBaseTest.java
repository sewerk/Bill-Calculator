package pl.srw.billcalculator;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.Callable;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.TestScheduler;

public class RxJavaBaseTest {

    protected static TestScheduler testScheduler;

    @BeforeClass
    public static void init() {
        testScheduler = new TestScheduler();
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return testScheduler;
            }
        });
        RxJavaPlugins.setInitIoSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return testScheduler;
            }
        });
    }

    @AfterClass
    public static void shutDown() throws Exception {
        RxJavaPlugins.reset();
        RxAndroidPlugins.reset();
    }

    protected void waitToFinish() {
        testScheduler.triggerActions();
    }
}
