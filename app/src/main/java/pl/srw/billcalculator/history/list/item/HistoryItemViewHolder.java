package pl.srw.billcalculator.history.list.item;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.Bind;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.bill.SavedBillsRegistry;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.list.provider.HistoryItemValueProvider;
import pl.srw.billcalculator.util.strategy.Transitions;

/**
* Created by Kamil Seweryn.
*/
public class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.tv_for_period) TextView tvForPeriod;
    @Bind(R.id.iv_bill_type) ImageView imgLogo;
    @Bind(R.id.history_item_readings_zone_1) TextView tvZone1;
    @Bind(R.id.history_item_readings_from_1) TextView tvFrom1;
    @Bind(R.id.history_item_readings_to_1) TextView tvTo1;
    @Bind(R.id.history_item_readings_row_2) View llNightRow;
    @Bind(R.id.history_item_readings_from_2) TextView tvFrom2;
    @Bind(R.id.history_item_readings_to_2) TextView tvTo2;
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
        tvAmount.setText(itemValuesProvider.getAmount());

        final int[] readings = itemValuesProvider.getReadings();
        tvFrom1.setText(String.valueOf(readings[0]));
        tvTo1.setText(String.valueOf(readings[1]));
        if (readings.length == 4) {
            tvZone1.setText(R.string.history_item_day_zone);
            llNightRow.setVisibility(View.VISIBLE);
            tvFrom2.setText(String.valueOf(readings[2]));
            tvTo2.setText(String.valueOf(readings[3]));
        }
    }

    @Override
    public void onClick(View v) {
        Transitions.getInstance().startActivity((Activity) itemView.getContext(), itemValuesProvider.getIntent(), v);
        SavedBillsRegistry.getInstance().register(itemValuesProvider.getBill());
    }

    public Bill getBill() {
        return itemValuesProvider.getBill();
    }
}
