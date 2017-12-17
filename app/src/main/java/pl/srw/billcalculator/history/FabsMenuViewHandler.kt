package pl.srw.billcalculator.history

import android.animation.AnimatorSet
import android.support.design.widget.FloatingActionButton
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import pl.srw.billcalculator.R
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Animations
import pl.srw.billcalculator.util.debugMeasure
import pl.srw.billcalculator.util.lazyUnsafe
import timber.log.Timber
import javax.inject.Inject

/**
 * Handles FABs menu view part
 */
@Suppress("TooManyFunctions")
class FabsMenuViewHandler @Inject constructor() : FabsMenuPresenter.View {

    @BindView(R.id.fab) lateinit var fab: FloatingActionButton
    @BindView(R.id.fab_new_pge) lateinit var fabPge: FloatingActionButton
    @BindView(R.id.fab_new_pgnig) lateinit var fabPgnig: FloatingActionButton
    @BindView(R.id.fab_new_tauron) lateinit var fabTauron: FloatingActionButton
    @BindView(R.id.fabs_dim_view) lateinit var dim: View
    @BindView(R.id.app_bar) lateinit var toolbar: View

    lateinit var activity: DrawerActivity
    val presenter: FabsMenuPresenter = FabsMenuPresenter(this)

    private val expandAnimator: AnimatorSet by lazyUnsafe { Animations.getExpandFabs(fab, fabPge, fabPgnig, fabTauron) }
    private val collapseAnimator: AnimatorSet by lazyUnsafe { Animations.getCollapseFabs(fab, fabPge, fabPgnig, fabTauron) }

    fun init(activity: DrawerActivity) {
        ButterKnife.bind(this, activity)
        this.activity = activity
    }

    @OnClick(R.id.fab)
    fun onBaseFabClicked() {
        presenter.baseFabClicked()
    }

    @OnClick(R.id.fab_new_pge, R.id.fab_new_pgnig, R.id.fab_new_tauron)
    fun onNewBillButtonClicked(view: View) {
        val provider = Provider.getByViewId(view.id)
        Timber.i("FAB clicked: $provider")
        presenter.fabClicked(provider)
    }

    @OnClick(R.id.fabs_dim_view)
    fun onDimClicked() {
        presenter.outsideClicked()
    }

    fun handleBackPressed(): Boolean {
        return presenter.handleBackPressed()
    }

    override fun isExpanded() = fabPge.visibility == View.VISIBLE

    override fun isCollapsingInProgress() = collapseAnimator.isRunning

    override fun expand() {
        debugMeasure("expand animation") {
            if (collapseAnimator.isRunning) {
                collapseAnimator.end()
            }
            expandAnimator.start()
        }
    }

    override fun collapse() {
        debugMeasure("collapse animation") {
            if (expandAnimator.isRunning) {
                expandAnimator.end()
            }
            collapseAnimator.start()
        }
    }

    override fun showDim() {
        dim.visibility = View.VISIBLE
        toolbar.isEnabled = false
    }

    override fun hideDim() {
        dim.visibility = View.GONE
        toolbar.isEnabled = true
    }

    override fun openForm(provider: Provider) {
        activity.showNewBillForm(provider)
    }
}
