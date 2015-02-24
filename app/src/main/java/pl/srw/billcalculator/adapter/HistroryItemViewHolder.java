package pl.srw.billcalculator.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.History;

/**
* Created by Kamil Seweryn.
*/
public class HistroryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @InjectView(R.id.iv_bill_type) ImageView imgLogo;
    @InjectView(R.id.tv_for_period) TextView tvForPeriod;
    @InjectView(R.id.tv_readings) TextView tvReadings;
    @InjectView(R.id.tv_amount) TextView tvAmount;
    private HistoryItemValueProvider intentProvider;

    public HistroryItemViewHolder(View v) {
        super(v);
        v.setOnClickListener(this);
        ButterKnife.inject(this, v);
    }

    @Override
    public void onClick(final View v) {
        v.getContext().startActivity(intentProvider.getIntent());
    }

    public void bindEntry(final History item) {
        final HistoryItemValueProvider historyItemValueProvider = HistoryItemValueProvider.of(item);

        imgLogo.setImageResource(historyItemValueProvider.getLogoId());
        tvForPeriod.setText(historyItemValueProvider.getDatePeriod());
        tvReadings.setText(historyItemValueProvider.getReadings());
        tvAmount.setText(historyItemValueProvider.getAmount());

        intentProvider = historyItemValueProvider;
    }

    // TODO: update autocomplete on entry delete -set historyChanged
}
