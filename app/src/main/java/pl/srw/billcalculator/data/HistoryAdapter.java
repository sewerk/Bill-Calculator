package pl.srw.billcalculator.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;

    public HistoryAdapter(Context context) {
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
        // - replace the contents of the view with that element
        holder.llBillType.setBackgroundResource(R.drawable.pgnig_on_pge);
        holder.tvForPeriod.setText("01/01/2014 - 31/12/2015");//TODO
        holder.tvReadings.setText(context.getString(R.string.readings, 22353));
        holder.tvAmount.setText("23456 zł");
    }

    @Override
    public int getItemCount() {
        return 3;
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

        }

        // TODO: update autocomplete on entry delete -set historyChanged
    }
}
