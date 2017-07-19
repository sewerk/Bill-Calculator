package pl.srw.billcalculator.history

import pl.srw.billcalculator.type.Provider

/**
 * Controls FAB menu view
 */
class FabsMenuPresenter(val view: View) {

    fun baseFabClicked() {
        if (view.isExpanded() && !view.isCollapsingInProgress()) collapseAndHideDim()
        else expandAndShowDim()
    }

    fun fabClicked(provider: Provider) {
        view.openForm(provider)
        collapseAndHideDim()
    }

    fun outsideClicked() {
        collapseAndHideDim()
    }

    fun handleBackPressed(): Boolean {
        if (view.isExpanded() && !view.isCollapsingInProgress()) {
            collapseAndHideDim()
            return true
        } else return false
    }

    private fun expandAndShowDim() {
        view.expand()
        view.showDim()
    }

    private fun collapseAndHideDim() {
        view.collapse()
        view.hideDim()
    }

    interface View {
        fun isExpanded(): Boolean
        fun isCollapsingInProgress(): Boolean
        fun expand()
        fun collapse()
        fun openForm(provider: Provider)
        fun showDim()
        fun hideDim()
    }
}
