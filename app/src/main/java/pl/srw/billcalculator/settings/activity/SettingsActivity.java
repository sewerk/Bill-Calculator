package pl.srw.billcalculator.settings.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn
 */
public class SettingsActivity extends BackableActivity {

    @InjectView(R.id.list) ListView list;

    private static SettingsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (presenter == null)
            presenter = new SettingsPresenter(this);

        setContentView(R.layout.preference_list);
        ButterKnife.inject(this);

        list.setAdapter(new SimpleAdapter(this, presenter.getEntries(), R.layout.preference_item, presenter.getFromColumns(), new int[]{R.id.title, R.id.summary}));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.itemClicked(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing())
            presenter = null;
    }
}
