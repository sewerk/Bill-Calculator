package pl.srw.billcalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.dao.query.LazyList;
import pl.srw.billcalculator.BillCalculator;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.persistence.Database;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.util.Dates;
import pl.srw.billcalculator.util.Display;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    protected boolean dataValid;
    protected LazyList<History> lazyList;

    public HistoryAdapter(LazyList<History> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // get element from your dataset at this position
        History historyItem = lazyList.get(position);
        Bill entity = retrieveBill(historyItem);
        // remember bill for open operation
        holder.item = entity;
        // replace the contents of the view with that element
        holder.logo.setImageResource(getBillTypeDrawable(entity));
        holder.tvForPeriod.setText(getDatePeriod(entity));
        holder.tvReadings.setText(getReadings(entity));
        holder.tvAmount.setText(getAmount(entity) + " zł");
    }

    @Override
    public int getItemCount() {
        if (dataValid && lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

    private Bill retrieveBill(final History historyItem) {
        final Long billId = historyItem.getBillId();
        if (isItemOfType(historyItem, BillType.PGE)) {
            return Database.getSession().getPgeBillDao().load(billId);
        } else if (isItemOfType(historyItem, BillType.PGE_G12)) {
            return Database.getSession().getPgeG12BillDao().load(billId);
        } else if (isItemOfType(historyItem, BillType.PGNIG)) {
            return Database.getSession().getPgnigBillDao().load(billId);
        }
        return null;
    }

    private boolean isItemOfType(final History historyItem, final BillType billType) {
        return historyItem.getBillType().equals(billType.toString());
    }

    private int getBillTypeDrawable(final Bill entity) {
        if (entity instanceof PgnigBill) {
            return R.drawable.pgnig_on_pge;
        } else {
            return R.drawable.pge_on_pgnig;
        }
    }

    private String getAmount(final Bill entity) {
        return Display.toPay(new BigDecimal(entity.getAmountToPay().toString()));
    }

    private String getDatePeriod(final Bill entity) {
        return Dates.format(entity.getDateFrom()) + " - " + Dates.format(entity.getDateTo());
    }

    private String getReadings(final Bill entity) {
        if (entity instanceof PgeBill) {
            PgeBill bill = (PgeBill) entity;
            return "" + bill.getReadingFrom() + " - " + bill.getReadingTo();
        } else if (entity instanceof PgeG12Bill) {
            PgeG12Bill bill = (PgeG12Bill) entity;
            return "day " + bill.getReadingDayFrom() + " - " + bill.getReadingDayTo() + "\n"
                    + "night " + bill.getReadingNightFrom() + " - "  + bill.getReadingNightTo();
        } else if (entity instanceof PgnigBill) {
            PgnigBill bill = (PgnigBill) entity;
            return "" + bill.getReadingFrom() + " - " + bill.getReadingTo();
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.iv_bill_type) ImageView logo;
        @InjectView(R.id.tv_for_period) TextView tvForPeriod;
        @InjectView(R.id.tv_readings) TextView tvReadings;
        @InjectView(R.id.tv_amount) TextView tvAmount;
        private Bill item;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.inject(this, v);
        }

        @Override
        public void onClick(final View v) { //TODO open bill
            Toast.makeText(BillCalculator.context, "Open " + item.getClass().getSimpleName() + " bill", Toast.LENGTH_SHORT).show();
        }

        // TODO: update autocomplete on entry delete -set historyChanged
    }
}
