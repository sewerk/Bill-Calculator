package pl.srw.billcalculator.history;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.util.Animations;

/**
 * View helper class for handling FABs animation and expand state
 */
public class FabsMenuHandler {

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.fab_new_pge) FloatingActionButton fabPge;
    @Bind(R.id.fab_new_pgnig) FloatingActionButton fabPgnig;
    @Bind(R.id.fab_new_tauron) FloatingActionButton fabTauron;
    private AnimatorSet expandAnimator;
    private AnimatorSet collapseAnimator;

    @Inject
    public FabsMenuHandler() {
    }

    public void setup(Activity activity) {
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
    }

    private void expand() {
        if (isCollapsingInProgress()) {
            collapseAnimator.end();
        }
        if (expandAnimator == null) {
            expandAnimator = Animations.getExpandFabs(fab, fabPge, fabPgnig, fabTauron);
        }
        expandAnimator.start();
    }

    private void collapse() {
        if (isExpandingInProgress()) {
            expandAnimator.end();
        }
        if (collapseAnimator == null) {
            collapseAnimator = Animations.getCollapseFabs(fab, fabPge, fabPgnig, fabTauron);
        }
        collapseAnimator.start();
    }

    private boolean isExpandingInProgress() {
        return expandAnimator != null && expandAnimator.isRunning();
    }

    private boolean isCollapsingInProgress() {
        return collapseAnimator != null && collapseAnimator.isRunning();
    }
}
