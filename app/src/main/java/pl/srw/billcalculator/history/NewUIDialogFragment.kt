package pl.srw.billcalculator.history

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import pl.srw.billcalculator.R
import pl.srw.billcalculator.di.Dependencies
import javax.inject.Inject

class NewUIDialogFragment : DialogFragment() {

    @Inject lateinit var helpHandler: HelpHandler

    lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dependencies.inject(this)
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
