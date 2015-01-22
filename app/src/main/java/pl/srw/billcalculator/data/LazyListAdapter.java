package pl.srw.billcalculator.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.dao.query.LazyList;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeBill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.util.Dates;

/**
 * Created by Kamil Seweryn.
 */
public class LazyListAdapter<T extends Bill> extends RecyclerView.Adapter<LazyListAdapter.ViewHolder> {

    private Context context;
    protected boolean dataValid;
    protected LazyList<T> lazyList;

    public LazyListAdapter(Context context, LazyList<T> lazyList) {
        this.lazyList = lazyList;
        this.dataValid = lazyList != null;
        this.context = context;
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
        // - get element from your dataset at this position
        T entity = null;
        if (dataValid && lazyList != null) {
            entity = lazyList.get(position);
        }
        // - replace the contents of the view with that element
        holder.llBillType.setBackgroundResource(getBillTypeDrawable(entity));
        holder.tvForPeriod.setText(getDatePeriod(entity));
        holder.tvReadings.setText(context.getString(R.string.readings, getReadings(entity)));
        holder.tvAmount.setText(entity.getAmountToPay().toString() + " zł");
    }

    private int getBillTypeDrawable(final T entity) {
        if (entity instanceof PgnigBill) {
            return R.drawable.pgnig_on_pge;
        } else {
            return R.drawable.pge_on_pgnig;
        }
    }

    private String getDatePeriod(final T entity) {
        return Dates.format(entity.getDateFrom()) + " - " + Dates.format(entity.getDateTo());
    }

    private Object getReadings(final T entity) {
        if (entity instanceof PgeBill) {
            PgeBill bill = (PgeBill) entity;
            return bill.getReadingTo() - bill.getReadingFrom();
        } else if (entity instanceof PgeG12Bill) {
            PgeG12Bill bill = (PgeG12Bill) entity;
            return bill.getReadingDayTo() - bill.getReadingDayFrom()
                    + bill.getReadingNightTo() - bill.getReadingNightFrom();
        } else if (entity instanceof PgnigBill) {
            PgnigBill bill = (PgnigBill) entity;
            return bill.getReadingTo() - bill.getReadingFrom();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (dataValid && lazyList != null) {
            return lazyList.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.ll_bill_summary) LinearLayout llBillType;
        @InjectView(R.id.tv_for_period) TextView tvForPeriod;
        @InjectView(R.id.tv_readings) TextView tvReadings;
        @InjectView(R.id.tv_amount) TextView tvAmount;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }

        @Override
        public void onClick(final View v) {
        //TODO: open bill
        }

        // TODO: update autocomplete on entry delete -set historyChanged
    }
}
