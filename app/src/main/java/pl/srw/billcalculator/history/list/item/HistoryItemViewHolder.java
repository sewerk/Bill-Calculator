package pl.srw.billcalculator.history.list.item;

import android.support.annotation.DrawableRes;
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
import pl.srw.billcalculator.history.list.provider.DoubleReadingsBillHistoryItemValueProviding;
import pl.srw.billcalculator.history.list.provider.HistoryItemValueProvider;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener, HistoryViewItem {

    @BindView(R.id.history_item_date_period) TextView datePeriodView;
    @BindView(R.id.history_item_logo) ImageView logoImage;
    @BindView(R.id.history_item_day_readings) TextView dayReadings;
    @BindView(R.id.history_item_night_readings) TextView nightReadings;
    @BindView(R.id.history_item_amount) TextView amountView;

    private final HistoryItemClickListener clickListener;
    private final HistoryItemSelectionAnimator logoAnimator;
    private HistoryItemValueProvider itemValuesProvider;

    public HistoryItemViewHolder(View v, final HistoryItemClickListener clickListener) {
        super(v);
        this.clickListener = clickListener;
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);
        ButterKnife.bind(this, v);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLongClick(v);
            }
        });

        logoAnimator = new HistoryItemSelectionAnimator(v.getContext(), logoImage);
    }

    @DebugLog
    public void bindEntry(final History item, boolean selected) {
        itemValuesProvider = HistoryItemValueProvider.of(item, itemView.getContext());

        setLogo(selected);

        datePeriodView.setText(itemValuesProvider.getDatePeriod());
        amountView.setText(itemValuesProvider.getAmount());

        dayReadings.setText(itemValuesProvider.getDayReadings());
        if (itemValuesProvider instanceof DoubleReadingsBillHistoryItemValueProviding) {
            DoubleReadingsBillHistoryItemValueProviding doubleItemValuesProvider
                    = (DoubleReadingsBillHistoryItemValueProviding) this.itemValuesProvider;
            nightReadings.setText(doubleItemValuesProvider.getNightReadings());
        }
    }

    @Override
    public View getView() {
        return itemView;
    }

    @Override
    public void select() {
        logoAnimator.changeTo(R.drawable.history_item_selected);
        logoImage.setSelected(true);
    }

    @Override
    public void deselect() {
        logoAnimator.changeTo(itemValuesProvider.getLogoId());
        logoImage.setSelected(false);
    }

    @Override
    public int getPositionOnList() {
        return getAdapterPosition();
    }

    @Override
    public void onClick(View v) {
        clickListener.onListItemClicked(this);
    }

    @Override
    public boolean onLongClick(View v) {
        clickListener.onListItemLongClicked(this);
        return true;
    }

    @Override
    public Bill getBill() {
        return itemValuesProvider.getBill();
    }

    private void setLogo(boolean selected) {
        @DrawableRes int logoId = selected
                ? R.drawable.history_item_selected
                : itemValuesProvider.getLogoId();

        logoImage.setImageResource(logoId);
        logoImage.setSelected(selected);
    }

}