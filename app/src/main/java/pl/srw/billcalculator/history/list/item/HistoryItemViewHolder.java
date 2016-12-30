package pl.srw.billcalculator.history.list.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.list.provider.HistoryItemValueProvider;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.my_bills_item_date_period) TextView datePeriodView;
    @BindView(R.id.my_bills_item_logo) ImageView logoImage;
    @BindView(R.id.my_bills_item_day_readings) TextView dayReadings;
    @BindView(R.id.my_bills_item_night_readings) TextView nightReadings;
    @BindView(R.id.my_bills_item_amount) TextView amountView;

    private final HistoryItemClickListener clickListener;
    private HistoryItemValueProvider itemValuesProvider;

    public HistoryItemViewHolder(View v, final HistoryItemClickListener clickListener) {
        super(v);
        this.clickListener = clickListener;
        v.setOnClickListener(this);
        ButterKnife.bind(this, v);
    }

    @DebugLog
    public void bindEntry(final History item) {
        itemValuesProvider = HistoryItemValueProvider.of(item, itemView.getContext());

        logoImage.setImageResource(itemValuesProvider.getLogoId());
        logoImage.setTag(itemValuesProvider.getLogoId());

        datePeriodView.setText(itemValuesProvider.getDatePeriod());
        amountView.setText(itemValuesProvider.getAmount());

        dayReadings.setText(itemValuesProvider.getDayReadings());
        if (itemValuesProvider.hasDoubleReadings()) {
            nightReadings.setText(itemValuesProvider.getNightReadings());
        }
    }

    @Override
    public void onClick(View v) {
        clickListener.onListItemClicked(getBill());
    }

    public Bill getBill() {
        return itemValuesProvider.getBill();
    }
}