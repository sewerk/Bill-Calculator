package pl.srw.billcalculator.history

import pl.srw.billcalculator.type.Provider

class DrawerPresenter(private val view: View) {

    fun historyClicked() {
        view.closeDrawer()
    }

    fun newBillClicked(provider: Provider) {
        view.showNewBillForm(provider)
        view.closeDrawer()
    }

    fun aboutClicked() {
        view.showAbout()
        view.closeDrawer()
    }

    fun settingsClicked() {
        view.openSettings()
        view.closeDrawer()
    }

    fun handleBackPressed(): Boolean {
        if (view.isDrawerOpen()) {
            view.closeDrawer()
            return true
        }
        return false
    }

    interface View {
        fun showNewBillForm(provider: Provider)
        fun openSettings()
        fun showAbout()
        fun closeDrawer()
        fun isDrawerOpen(): Boolean
    }
}