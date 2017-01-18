package pl.srw.billcalculator.bill.activity

import android.content.pm.PackageManager
import android.view.View
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.*
import pl.srw.billcalculator.bill.activity.print.Printer
import pl.srw.billcalculator.invokeHiddenMethod
import pl.srw.billcalculator.setState
import java.io.File

class BillPresenterTest {

    val temporaryDir = TemporaryFolder()
        @Rule get

    val printer = mock(Printer::class.java)
    val view = mock(BillPresenter.BillView::class.java)

    val sut = BillPresenter(printer, "/path")

    @Before
    fun setUp() {
        sut.setState("view", view)
        sut.setState("printDir", temporaryDir.newFolder())
        sut.setup("id")
        `when`(view.getContentView()).thenReturn(mock(View::class.java))
    }

    @Test
    fun `checks storage permission when print icon clicked`() {
        sut.onPrintClicked()

        verify(view).hasPermission(STORAGE_PERMISSION)
    }

    @Test
    fun `request storage permission when print icon clicked and permission not approved and should not show explanation`() {
        `when`(view.hasPermission(STORAGE_PERMISSION)).thenReturn(false)
        `when`(view.shouldShowExplanation(STORAGE_PERMISSION)).thenReturn(false)

        sut.onPrintClicked()

        verify(view).requestPermission(STORAGE_PERMISSION)
    }

    @Test
    fun `check should show explanation when print icon clicked and permission not approved`() {
        `when`(view.hasPermission(STORAGE_PERMISSION)).thenReturn(false)

        sut.onPrintClicked()

        verify(view).shouldShowExplanation(STORAGE_PERMISSION)
    }

    @Test
    fun `show explanation when print icon clicked and permission not approved and should show explanation`() {
        `when`(view.hasPermission(STORAGE_PERMISSION)).thenReturn(false)
        `when`(view.shouldShowExplanation(STORAGE_PERMISSION)).thenReturn(true)

        sut.onPrintClicked()

        verify(view).showExplanationForRequestPermission(STORAGE_PERMISSION)
    }

    @Test
    fun `perform print when print icon clicked and permission approved`() {
        `when`(view.hasPermission(STORAGE_PERMISSION)).thenReturn(true)

        sut.onPrintClicked()

        verify(printer).printToPdf(any(View::class.java), any(File::class.java))
    }

    @Test
    fun `perform print when requested permission granted`() {
        sut.processRequestPermissionResponse(intArrayOf(PackageManager.PERMISSION_GRANTED))

        verify(printer).printToPdf(any(View::class.java), any(File::class.java))
    }

    @Test
    fun `show explanation when requested permission denied`() {
        sut.processRequestPermissionResponse(intArrayOf(PackageManager.PERMISSION_DENIED))

        verify(view).showExplanationForRequestPermission(STORAGE_PERMISSION)
    }

    @Test
    fun `show print in progress icon on first view bind when printing is in progress`() {
        `when`(printer.isPrintInProgress(any(File::class.java))).thenReturn(true)

        sut_onFirstBind()

        verify(view).setPrintInProgressIcon()
    }

    @Test
    fun `show print in progress icon on view rebind when printing is in progress`() {
        `when`(printer.isPrintInProgress(any(File::class.java))).thenReturn(true)

        sut_onNewViewRestoreState()

        verify(view).setPrintInProgressIcon()
    }

    @Test
    fun `dont show print in progress icon on first view bind when printing not in progress`() {
        `when`(printer.isPrintInProgress(any(File::class.java))).thenReturn(false)

        sut_onFirstBind()

        verify(view, never()).setPrintInProgressIcon()
    }

    @Test
    fun `dont show print in progress icon on view rebind when printing not in progress`() {
        `when`(printer.isPrintInProgress(any(File::class.java))).thenReturn(false)

        sut_onNewViewRestoreState()

        verify(view, never()).setPrintInProgressIcon()
    }

    @Test
    fun `show print in progress icon when printing started`() {
        sut.onPrintStarted(mock(File::class.java))

        verify(view).setPrintInProgressIcon()
    }

    @Test
    fun `show print ready icon when printing finished`() {
        sut.onPrintoutReady(mock(File::class.java))

        verify(view).setPrintReadyIcon()
    }

    @Test
    fun `show print ready icon when print failed`() {
        sut.onPrintFailed(mock(Throwable::class.java))

        verify(view).setPrintReadyIcon()
    }

    @Test
    fun `open file when printing finished`() {
        val file = mock(File::class.java)

        sut.onPrintoutReady(file)

        verify(view).openFile(file, BillActivity.MIME_APPLICATION_PDF)
    }

    @Test
    fun `show message when printing failed`() {
        sut.onPrintFailed(mock(Throwable::class.java))

        verify(view).showMessage(anyInt())
    }

    @Test
    fun `listen to printer events on first view bind`() {
        sut_onFirstBind()

        verify(printer).register(sut)
    }

    @Test
    fun `dont listen to printer events on view rebind`() {
        sut_onNewViewRestoreState()

        verify(printer, never()).register(sut)
    }

    @Test
    fun `stop listening to printer events on destroy`() {
        sut.onFinish()

        verify(printer).unregister(sut)
    }

    @Test
    fun `show message when print icon clicked and has storage permission but file cannot be created for printout`() {
        `when`(view.hasPermission(STORAGE_PERMISSION)).thenReturn(true)
        sut.setState("printDir", mock(File::class.java))

        sut.onPrintClicked()

        verify(view).showMessage(anyInt())
    }

    private fun sut_onFirstBind() {
        sut.invokeHiddenMethod("onFirstBind")
    }

    private fun sut_onNewViewRestoreState() {
        sut.invokeHiddenMethod("onNewViewRestoreState")
    }
}

