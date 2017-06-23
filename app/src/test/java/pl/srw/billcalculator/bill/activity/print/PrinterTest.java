package pl.srw.billcalculator.bill.activity.print;

import android.graphics.Bitmap;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;

import pl.srw.billcalculator.RxJavaBaseTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PrinterTest extends RxJavaBaseTest {

    @InjectMocks
    private Printer sut;

    @Mock View view;
    @Mock Bitmap bitmap;
    @Mock File file;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        sut = spy(sut);
        doReturn(bitmap).when(sut).buildBitmapFrom(view);
        doReturn(file).when(sut).writeToImage(any(Bitmap.class), any(File.class));
        doReturn(file).when(sut).imageToPdf(file, file);
    }

    @Test
    public void isPrintInProgress_whenPrintNotTriggered_returnsFalse() throws Exception {
        // WHEN
        boolean result = sut.isPrintInProgress(file);

        // THEN
        assertFalse(result);
    }

    @Test
    public void isPrintInProgress_whenPrintStarted_returnsTrue() throws Exception {
        // GIVEN
        sut.printToPdf(view, file);

        // WHEN
        boolean result = sut.isPrintInProgress(file);

        // THEN
        assertTrue(result);
    }

    @Test
    public void isPrintInProgress_whenPrintFinished_returnsFalse() throws Exception {
        // GIVEN
        sut.printToPdf(view, file);
        waitToFinish();

        // WHEN
        boolean result = sut.isPrintInProgress(file);

        // THEN
        assertFalse(result);
    }

    @Test
    public void printToPdf_whenListenerSet_notifiesPrintStarted() throws Exception {
        // GIVEN
        Printer.Listener listener = mock(Printer.Listener.class);
        sut.register(listener);

        // WHEN
        sut.printToPdf(view, file);

        // THEN
        verify(listener).onPrintStarted(file);
    }

    @Test
    public void printToPdf_whenPrintFinished_notifiesPrintReady() throws Exception {
        // GIVEN
        Printer.Listener listener = mock(Printer.Listener.class);
        sut.register(listener);

        // WHEN
        sut.printToPdf(view, file);
        waitToFinish();

        // THEN
        verify(listener).onPrintoutReady(file);
    }

    @Test
    public void printToPdf_whenPrintFailed_notifiesPrintFailed() throws Exception {
        // GIVEN
        RuntimeException exception = new RuntimeException();
        doThrow(exception).when(sut).buildBitmapFrom(view);
        Printer.Listener listener = mock(Printer.Listener.class);
        sut.register(listener);

        // WHEN
        sut.printToPdf(view, file);
        waitToFinish();

        // THEN
        verify(listener).onPrintFailed(exception);
    }

    @Test
    public void printToPdf_whenListenerUnregistered_andPrintFinished_doesNotNotify() throws Exception {
        // GIVEN
        Printer.Listener listener = mock(Printer.Listener.class);
        sut.register(listener);
        sut.unregister(listener);

        // WHEN
        sut.printToPdf(view, file);
        waitToFinish();

        // THEN
        verifyNoMoreInteractions(listener);
    }
}