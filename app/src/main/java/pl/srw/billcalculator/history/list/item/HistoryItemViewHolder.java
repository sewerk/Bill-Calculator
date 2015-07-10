package pl.srw.billcalculator.history.list.item;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.history.HasDeleteMode;
import pl.srw.billcalculator.history.list.HistoryAdapter;
import pl.srw.billcalculator.history.list.provider.HistoryItemValueProvider;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.util.MultiSelect;
import pl.srw.billcalculator.history.list.SelectedBill;

/**
* Created by Kamil Seweryn.
*/
public class HistoryItemViewHolder extends RecyclerView.ViewHolder
        implements SwipeDetectionTouchListener.SwipeExecutor {

    @InjectView(R.id.iv_bill_type) ImageView imgLogo;
    @InjectView(R.id.tv_for_period) TextView tvForPeriod;
    @InjectView(R.id.tv_readings) TextView tvReadings;
    @InjectView(R.id.tv_amount) TextView tvAmount;
    private HistoryItemValueProvider itemValuesProvider;
    private final AnimatorSet changeLogoAnimator;

    private final HistoryAdapter adapter;
    private final MultiSelect<Integer, SelectedBill> selection;

    public HistoryItemViewHolder(HistoryAdapter adapter, final MultiSelect<Integer, SelectedBill> selection, View v) {
        super(v);
        this.adapter = adapter;
        this.selection = selection;

        v.setOnTouchListener(new SwipeDetectionTouchListener(adapter.getActivity(), this));
        ButterKnife.inject(this, v);

        final Animator outAnimator = AnimatorInflater.loadAnimator(adapter.getActivity(), R.animator.card_flip_right_out);
        final ObjectAnimator changeAnimator = ObjectAnimator.ofInt(imgLogo, "imageResource", 0, 0).setDuration(0);
        final Animator inAnimator = AnimatorInflater.loadAnimator(adapter.getActivity(), R.animator.card_flip_left_in);
        changeLogoAnimator = new AnimatorSet();
        changeLogoAnimator.playSequentially(outAnimator, changeAnimator, inAnimator);
        changeLogoAnimator.setTarget(imgLogo);
    }

    @DebugLog
    public void bindEntry(final History item) {
        itemValuesProvider = HistoryItemValueProvider.of(item);

        setLogoImage(false);
        tvForPeriod.setText(itemValuesProvider.getDatePeriod());
        tvReadings.setText(itemValuesProvider.getReadings());
        tvAmount.setText(itemValuesProvider.getAmount());
    }

    private void setLogoImage(final boolean animateChange) {
        final int drawable;
        if (selection.isSelected(getLayoutPosition())) drawable = R.drawable.selected;
        else drawable = itemValuesProvider.getLogoId();

        if (animateChange)
            animateLogoChange(drawable);
        else
            imgLogo.setImageResource(drawable);
        imgLogo.setTag(drawable);
    }

    @Override
    public void onTap() {
        if (getActivity().isInDeleteMode())
            toggleSelection();
        else {
            itemView.getContext().startActivity(itemValuesProvider.getIntent());
            SavedBillsRegistry.getInstance().register(itemValuesProvider.getBill());
        }
    }

    private HasDeleteMode getActivity() {
        return (HasDeleteMode) adapter.getActivity();
    }

    @Override
    public void onLongPress() {
        if (getActivity().isInDeleteMode()) {
            onTap();
            return;
        }

        getActivity().enableDeleteMode();
        toggleSelection();
    }

    @Override
    public void onSwipeDetected(SwipeDetectionTouchListener.Direction direction) {
        onLongPress();
    }

    private void toggleSelection() {
        if (selection.isSelected(getLayoutPosition())) {
            AnalyticsWrapper.log("Selecting " + getLayoutPosition() + "-th item");
            selection.deselect(getLayoutPosition());
        } else {
            AnalyticsWrapper.log("Deselecting " + getLayoutPosition() + "-th item");
            selection.select(getLayoutPosition(), new SelectedBill(itemValuesProvider.getBill()));
        }
        setLogoImage(true);
    }

    private void animateLogoChange(final int drawable) {
        ((ObjectAnimator) changeLogoAnimator.getChildAnimations().get(1)).setIntValues(drawable, drawable);
        changeLogoAnimator.start();
    }
}
