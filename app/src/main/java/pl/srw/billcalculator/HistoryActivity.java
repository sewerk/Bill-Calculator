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
import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.adapter.EmptyHistoryDataObserver;
import pl.srw.billcalculator.adapter.HistoryAdapter;
import pl.srw.billcalculator.event.HistoryChangedEvent;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryActivity extends BackableActivity {

    private EmptyHistoryDataObserver dataObserver;
    private ActionMode actionMode;

    @InjectView(R.id.rv_history_list) RecyclerView list;
    @InjectView(R.id.tv_emptyHistory) TextView tvEmptyHistory;

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
                    actionMode.finish();
                    EventBus.getDefault().post(new HistoryChangedEvent());
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
