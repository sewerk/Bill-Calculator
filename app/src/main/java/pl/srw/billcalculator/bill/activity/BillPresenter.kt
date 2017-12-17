package pl.srw.billcalculator.bill.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.annotation.StringRes
import android.view.View
import pl.srw.billcalculator.R
import pl.srw.billcalculator.bill.activity.print.Printer
import pl.srw.billcalculator.util.analytics.Analytics
import pl.srw.billcalculator.util.analytics.EventType
import pl.srw.mfvp.MvpPresenter
import pl.srw.mfvp.di.scope.RetainActivityScope
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject
import kotlin.properties.Delegates

const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

@Suppress("TooManyFunctions")
@RetainActivityScope
class BillPresenter @Inject constructor(private val printer: Printer,
                                        printDirPath: String)
    : MvpPresenter<BillPresenter.BillView>(), Printer.Listener {

    private val printDir = File(printDirPath)

    private var billIdentifier by Delegates.notNull<String>()

    fun setup(billIdentifier: String) {
        this.billIdentifier = billIdentifier
    }

    override fun onFirstBind() {
        printer.register(this)
        setPrintIcon()
    }

    override fun onNewViewRestoreState() {
        setPrintIcon()
    }

    override fun onFinish() {
        super.onFinish()
        printer.unregister()
    }

    fun onPrintClicked() {
        present { view ->
            when {
                view.hasPermission(STORAGE_PERMISSION) -> performPrint()
                view.shouldShowExplanation(STORAGE_PERMISSION) -> view.showExplanationForRequestPermission(STORAGE_PERMISSION)
                else -> view.requestPermission(STORAGE_PERMISSION)
            }
        }
    }

    fun processRequestPermissionResponse(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Analytics.event(EventType.PERMISSION, "result", "GRANTED")
            performPrint()
        } else {
            Analytics.event(EventType.PERMISSION, "result", "DENIED")
            present { it.showExplanationForRequestPermission(STORAGE_PERMISSION) }
        }
    }

    private fun performPrint() {
        Timber.d("printing... permission granted")
        val targetFile: File? = getTargetFile()
        if (targetFile != null) {
            Analytics.event(EventType.PRINT,
                    "for", fileNamePrefix(targetFile.name),
                    "print file exist", targetFile.exists())
            if (targetFile.exists())
                present { it.openFile(targetFile, BillActivity.MIME_APPLICATION_PDF) }
            else
                present { printer.printToPdf(it.getContentView(), targetFile) }
        } else {
            Timber.w("Target file for print not created")
            present { it.showMessage(R.string.error_permission_storage_missing) }
        }
    }

    override fun onPrintStarted(file: File) {
        present { it.setPrintInProgressIcon() }
    }

    override fun onPrintoutReady(file: File) {
        present { view ->
            view.openFile(file, BillActivity.MIME_APPLICATION_PDF)
            view.setPrintReadyIcon()
        }
    }

    override fun onPrintFailed(exception: Throwable) {
        present { view ->
            view.showMessage(R.string.print_failed)
            view.setPrintReadyIcon()
            Timber.e(exception, "print failed")
        }
    }

    private fun getTargetFile(): File? {
        if (!printDir.exists())
            if (!printDir.mkdirs()) {
                Timber.e(IOException("mkdir ${printDir.absolutePath} failed"))
                return null
            }
        return File(printDir.absolutePath, billIdentifier + ".pdf")
    }

    private fun setPrintIcon() {
        if (isPrintTaskRunning())
            present { it.setPrintInProgressIcon() }
    }

    private fun isPrintTaskRunning() = printer.isPrintInProgress(File(printDir.absolutePath, billIdentifier + ".pdf"))

    private fun fileNamePrefix(name: String) = name.substring(0, name.indexOf('_'))

    interface BillView {
        fun setPrintInProgressIcon()
        fun setPrintReadyIcon()
        fun hasPermission(permission: String): Boolean
        fun requestPermission(permission: String)
        fun shouldShowExplanation(permission: String): Boolean
        fun showExplanationForRequestPermission(permission: String)
        fun openFile(file: File, type : String)
        fun showMessage(@StringRes textResIdRes: Int)
        fun getContentView(): View
    }
}
