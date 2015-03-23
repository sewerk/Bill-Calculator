package pl.srw.billcalculator.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.adapter.provider.HistoryItemValueProvider;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.util.MultiSelect;
import pl.srw.billcalculator.util.SelectedBill;

/**
* Created by Kamil Seweryn.
*/
public class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @InjectView(R.id.iv_bill_type) ImageView imgLogo;
    @InjectView(R.id.tv_for_period) TextView tvForPeriod;
    @InjectView(R.id.tv_readings) TextView tvReadings;
    @InjectView(R.id.tv_amount) TextView tvAmount;
    private HistoryItemValueProvider itemValuesProvider;

    private HistoryAdapter adapter;
    private MultiSelect<Integer, SelectedBill> selection;

    public HistoryItemViewHolder(HistoryAdapter adapter, final MultiSelect<Integer, SelectedBill> selection, View v) {
        super(v);
        this.adapter = adapter;
        this.selection = selection;

        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        ButterKnife.inject(this, v);
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
    public void onClick(final View v) {
        if (adapter.getActivity().isInDeleteMode())
            toggleSelection();
        else
            v.getContext().startActivity(itemValuesProvider.getIntent());
    }

    @Override
    public boolean onLongClick(final View v) {
        if (adapter.getActivity().isInDeleteMode()) {
            return false;
        }

        adapter.getActivity().enableDeleteMode();
        toggleSelection();
        return true;
    }

    private void toggleSelection() {
        if (selection.isSelected(getLayoutPosition()))
            selection.deselect(getLayoutPosition());
        else {
            selection.select(getLayoutPosition(), new SelectedBill(itemValuesProvider.getBill()));
        }
        setLogoImage(true);
    }

    private void animateLogoChange(final int drawable) {
        final int halfDuration = adapter.getActivity().getResources().getInteger(R.integer.animation_time_half);
        imgLogo.animate()
                .rotationYBy(90f)
                .setDuration(halfDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        imgLogo.setImageResource(drawable);
                        imgLogo.animate()
                                .rotationYBy(180f)
                                .setDuration(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        imgLogo.animate()
                                                .rotationYBy(90f)
                                                .setDuration(halfDuration)
                                                .setListener(null);
                                    }
                                });
                    }
                });
    }
}
