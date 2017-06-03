package pl.srw.billcalculator.history;

import android.animation.AnimatorSet;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.util.Animations;

/**
 * View helper class for handling FABs animation and expand state
 */
public class FabsMenuHandler {

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fab_new_pge) FloatingActionButton fabPge;
    @BindView(R.id.fab_new_pgnig) FloatingActionButton fabPgnig;
    @BindView(R.id.fab_new_tauron) FloatingActionButton fabTauron;
    @BindView(R.id.fabs_dim_view) View dim;
    @BindView(R.id.app_bar) View toolbar;
    private AnimatorSet expandAnimator;
    private AnimatorSet collapseAnimator;
    private HistoryPresenter activityPresenter;

    @Inject
    public FabsMenuHandler() {
    }

    public void setup(DrawerActivity activity) {
        ButterKnife.bind(this, activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabPge.getVisibility() == View.GONE || isCollapsingInProgress())
                    expand();
                else
                    collapse();
            }
        });
        activityPresenter = activity.presenter;
    }

    @OnClick({R.id.fab_new_pge, R.id.fab_new_pgnig, R.id.fab_new_tauron})
    public void onNewBillButtonClicked(View view) {
        final Provider provider = Provider.getByViewId(view.getId());
        activityPresenter.newBillClicked(provider);
        collapse();// TODO: move to presenter
    }

    @OnClick(R.id.fabs_dim_view)
    public void onDimClicked() {
        collapse();
    }

    private void expand() {
        if (isCollapsingInProgress()) {
            collapseAnimator.end();
        }
        if (expandAnimator == null) {
            expandAnimator = Animations.getExpandFabs(fab, fabPge, fabPgnig, fabTauron);
        }
        expandAnimator.start();
        dim.setVisibility(View.VISIBLE);
        toolbar.setEnabled(false);
    }

    private void collapse() {
        if (isExpandingInProgress()) {
            expandAnimator.end();
        }
        if (collapseAnimator == null) {
            collapseAnimator = Animations.getCollapseFabs(fab, fabPge, fabPgnig, fabTauron);
        }
        collapseAnimator.start();
        dim.setVisibility(View.GONE);
        toolbar.setEnabled(true);
    }

    private boolean isExpandingInProgress() {
        return expandAnimator != null && expandAnimator.isRunning();
    }

    private boolean isCollapsingInProgress() {
        return collapseAnimator != null && collapseAnimator.isRunning();
    }
}
