package pl.srw.billcalculator.history

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import pl.srw.billcalculator.R
import pl.srw.billcalculator.history.di.HistoryComponent
import pl.srw.mfvp.MvpFragment
import pl.srw.mfvp.di.MvpActivityScopedFragment
import javax.inject.Inject

class NewUIDialogFragment : MvpFragment(), MvpActivityScopedFragment<HistoryComponent> {

    @Inject lateinit var helpHandler: HelpHandler

    lateinit var unbinder: Unbinder

    override fun injectDependencies(historyComponent: HistoryComponent) {
        historyComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_ui, null)
        val dialog = AlertDialog.Builder(context!!)
                .setView(view)
                .create()
        dialog.setCanceledOnTouchOutside(false)

        unbinder = ButterKnife.bind(this, view)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.new_ui_ok)
    fun onButtonClicked() {
        this.dismiss()
        helpHandler.show(activity!!)
    }
}
