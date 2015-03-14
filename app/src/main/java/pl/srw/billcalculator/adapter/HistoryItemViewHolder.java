package pl.srw.billcalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.adapter.provider.HistoryItemValueProvider;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.util.MultiSelect;

/**
* Created by Kamil Seweryn.
*/
public class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @InjectView(R.id.iv_bill_type) ImageView imgLogo;
    @InjectView(R.id.tv_for_period) TextView tvForPeriod;
    @InjectView(R.id.tv_readings) TextView tvReadings;
    @InjectView(R.id.tv_amount) TextView tvAmount;
    private HistoryItemValueProvider intentProvider;

    private HistoryAdapter adapter;
    private MultiSelect<Integer, Bill> selection;

    public HistoryItemViewHolder(HistoryAdapter adapter, final MultiSelect<Integer, Bill> selection, View v) {
        super(v);
        this.adapter = adapter;
        this.selection = selection;

        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        ButterKnife.inject(this, v);
    }

    @DebugLog
    public void bindEntry(final History item) {
        intentProvider = HistoryItemValueProvider.of(item);

        setLogoImage();
        tvForPeriod.setText(intentProvider.getDatePeriod());
        tvReadings.setText(intentProvider.getReadings());
        tvAmount.setText(intentProvider.getAmount());
    }

    private void setLogoImage() {
        if (selection.isSelected(getPosition()))
            imgLogo.setImageResource(R.drawable.selected);
        else
            imgLogo.setImageResource(intentProvider.getLogoId());
    }

    @Override
    public void onClick(final View v) {
        if (adapter.getActivity().isInDeleteMode())
            toggleSelection();
        else
            v.getContext().startActivity(intentProvider.getIntent());
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
        if (selection.isSelected(getPosition()))
            selection.deselect(getPosition());
        else
            selection.select(getPosition(), intentProvider.getBill());
        setLogoImage();
    }
}
