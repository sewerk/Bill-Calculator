package pl.srw.billcalculator.tester.interactor;

import android.support.annotation.IdRes;
import android.support.test.espresso.PerformException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.util.HumanReadables;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Allows to check RecyclerView item
 */
public class RecyclerViewInteraction {
    private ViewInteraction viewInteraction;
    private final int position;

    public RecyclerViewInteraction(ViewInteraction rcInteraction, int position) {
        this.viewInteraction = rcInteraction;
        this.position = position;
    }

    public RecyclerViewInteraction checkView(final @IdRes int childViewId, final ViewAssertion itemViewAssertion) {
        viewInteraction.check((view, ex) -> {
            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.ViewHolder viewHolderForPosition = recyclerView.findViewHolderForLayoutPosition(position);
            if (viewHolderForPosition == null) {
                throw (new PerformException.Builder())
                        .withActionDescription(toString())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new IllegalStateException("No view holder at position: " + position))
                        .build();
            } else {
                View viewAtPosition = viewHolderForPosition.itemView.findViewById(childViewId);
                itemViewAssertion.check(viewAtPosition, ex);
            }
        });
        return this;
    }
}
