package pl.srw.billcalculator.history.list.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.list.provider.HistoryItemValueProvider;

/**
* Created by Kamil Seweryn.
*/
public class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.iv_bill_type) ImageView imgLogo;
    @Bind(R.id.tv_for_period) TextView tvForPeriod;
    @Bind(R.id.tv_readings) TextView tvReadings;
    @Bind(R.id.tv_amount) TextView tvAmount;
    private HistoryItemValueProvider itemValuesProvider;

    public HistoryItemViewHolder(View v) {
        super(v);
        v.setOnClickListener(this);
        ButterKnife.bind(this, v);
    }

    @DebugLog
    public void bindEntry(final History item) {
        itemValuesProvider = HistoryItemValueProvider.of(item);

        imgLogo.setImageResource(itemValuesProvider.getLogoId());
        imgLogo.setTag(itemValuesProvider.getLogoId());

        tvForPeriod.setText(itemValuesProvider.getDatePeriod());
        tvReadings.setText(itemValuesProvider.getReadings());
        tvAmount.setText(itemValuesProvider.getAmount());
    }

    @Override
    public void onClick(View v) {
        itemView.getContext().startActivity(itemValuesProvider.getIntent());
        SavedBillsRegistry.getInstance().register(itemValuesProvider.getBill());
    }

    public Bill getBill() {
        return itemValuesProvider.getBill();
    }
}
