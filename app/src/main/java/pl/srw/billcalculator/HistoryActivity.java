package pl.srw.billcalculator;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.adapter.EmptyHistoryDataObserver;
import pl.srw.billcalculator.adapter.HistoryAdapter;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryActivity extends BackableActivity {

    @InjectView(R.id.rv_history_list) RecyclerView list;
    @InjectView(R.id.tv_emptyHistory) TextView tvEmptyHistory;
    private EmptyHistoryDataObserver dataObserver;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        ButterKnife.inject(this);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new HistoryAdapter(this));

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

    public void enableDeleteMode() {
        if (!isInDeleteMode())
            actionMode = startActionMode(deleteMode);
    }

    public boolean isInDeleteMode() {
        return actionMode != null;
    }

    private ActionMode.Callback deleteMode = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            getMenuInflater().inflate(R.menu.history_delete_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete:
                    ((HistoryAdapter) list.getAdapter()).deleteSelected();
                    setResult(MainActivity.HISTORY_RESPONSE_MARK_CHANGED);
                    actionMode.finish();
                    dataObserver.onChanged();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            ((HistoryAdapter) list.getAdapter()).exitSelectMode();
            HistoryActivity.this.actionMode = null;
        }
    };
}
