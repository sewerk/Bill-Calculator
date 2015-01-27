package pl.srw.billcalculator;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.adapter.EmptyHistoryDataObserver;
import pl.srw.billcalculator.adapter.HistoryAdapter;
import pl.srw.billcalculator.persistence.Database;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryActivity extends Activity {

    @InjectView(R.id.rv_history_list) RecyclerView list;
    @InjectView(R.id.tv_emptyHistory) TextView tvEmptyHistory;
    private EmptyHistoryDataObserver dataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.inject(this);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new HistoryAdapter(this, Database.getHistory()));

        dataObserver = new EmptyHistoryDataObserver(list.getAdapter(), tvEmptyHistory);
        dataObserver.onChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.getAdapter().registerAdapterDataObserver(dataObserver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        list.getAdapter().unregisterAdapterDataObserver(dataObserver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
