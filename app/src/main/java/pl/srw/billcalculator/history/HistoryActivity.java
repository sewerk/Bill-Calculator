package pl.srw.billcalculator.history;

import android.app.backup.BackupManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.AnalyticsWrapper;
import pl.srw.billcalculator.R;
import pl.srw.billcalculator.event.HistoryChangedEvent;
import pl.srw.billcalculator.history.list.EmptyHistoryDataObserver;
import pl.srw.billcalculator.history.list.HistoryAdapter;
import pl.srw.billcalculator.type.ContentType;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class HistoryActivity extends BackableActivity {

    private static final String STATE_ACTION_MODE_ENABLED = "STATE_ACTION_MODE_ENABLED";
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
        AnalyticsWrapper.logContent(ContentType.HISTORY, "history size", String.valueOf(list.getAdapter().getItemCount()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.getAdapter().registerAdapterDataObserver(dataObserver);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean(STATE_ACTION_MODE_ENABLED, false)) {
            enableDeleteMode();
            getAdapter().onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_ACTION_MODE_ENABLED, isInDeleteMode());
        getAdapter().onSaveInstanceState(outState);
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

    private final ActionMode.Callback deleteMode = new ActionMode.Callback() {

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
                    getAdapter().deleteSelected();
                    actionMode.finish();
                    for (Provider provider : Provider.values())//TODO: optimise to send if needed
                        EventBus.getDefault().post(new HistoryChangedEvent(provider));
                    dataObserver.onChanged();
                    new BackupManager(HistoryActivity.this).dataChanged();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            AnalyticsWrapper.log("Exit history delete");
            getAdapter().exitSelectMode();
            HistoryActivity.this.actionMode = null;
        }
    };

    private HistoryAdapter getAdapter() {
        return (HistoryAdapter) list.getAdapter();
    }
}
