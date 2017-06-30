package pl.srw.billcalculator.bill.activity

import android.Manifest
import android.content.pm.PackageManager
import android.support.annotation.StringRes
import android.view.View
import pl.srw.billcalculator.R
import pl.srw.billcalculator.bill.activity.print.Printer
import pl.srw.billcalculator.type.ActionType
import pl.srw.billcalculator.wrapper.Analytics
import pl.srw.mfvp.di.scope.RetainActivityScope
import pl.srw.mfvp.presenter.MvpPresenter
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.properties.Delegates

const val STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

@RetainActivityScope
class BillPresenter @Inject constructor(val printer: Printer, printDirPath: String) : MvpPresenter<BillPresenter.BillView>(), Printer.Listener {

    val printDir = File(printDirPath)

    var billIdentifier by Delegates.notNull<String>()

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
        printer.unregister(this)
    }

    fun onPrintClicked() {
        present { view ->
            if (view.hasPermission(STORAGE_PERMISSION))
                performPrint()
            else if (view.shouldShowExplanation(STORAGE_PERMISSION))
                view.showExplanationForRequestPermission(STORAGE_PERMISSION)
            else
                view.requestPermission(STORAGE_PERMISSION) }
    }

    fun processRequestPermissionResponse(grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.d("requested permission: GRANTED")
            performPrint()
        } else {
            Timber.d("requested permission: DENIED")
            present { it.showExplanationForRequestPermission(STORAGE_PERMISSION) }
        }
    }

    private fun performPrint() {
        Timber.d("printing... permission granted")
        val targetFile: File? = getTargetFile()
        if (targetFile != null) {
            Analytics.logAction(ActionType.PRINT,
                    "print file", targetFile.name,
                    "print file exist", targetFile.exists())
            if (targetFile.exists())
                present { it.openFile(targetFile, BillActivity.MIME_APPLICATION_PDF) }
            else
                present { printer.printToPdf(it.getContentView(), targetFile) }
        } else
            present { it.showMessage(R.string.error_permission_storage_missing) }
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
            Analytics.error(exception, "print failed")
        }
    }

    private fun getTargetFile(): File? {
        if (!printDir.exists())
            if (!printDir.mkdirs()) {
                Analytics.warning("mkdir failed")
                return null
            }
        return File(printDir.absolutePath, billIdentifier + ".pdf")
    }

    private fun setPrintIcon() {
        if (isPrintTaskRunning())
            present { it.setPrintInProgressIcon() }
    }

    private fun isPrintTaskRunning() = printer.isPrintInProgress(File(printDir.absolutePath, billIdentifier + ".pdf"))

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