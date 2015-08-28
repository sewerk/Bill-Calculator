package pl.srw.billcalculator.settings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnItemClick;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn
 */
public class SettingsActivity extends BackableActivity implements SettingsViewing {

    @Bind(R.id.list) ListView list;
    private SettingsPresenting presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_list);
        presenter = new SettingsPresenter(this);
    }

    @OnItemClick(R.id.list)
    public void onProviderClicked(int position) {
        final Intent intent = ProviderSettingsActivity.createIntent(SettingsActivity.this, presenter.getProviderAt(position));
        startActivity(intent);
    }

    @Override
    public void fillList(List<Map<String, Object>> entries, String[] columns) {
        SimpleAdapter adapter = new SimpleAdapter(this, entries, R.layout.preference_item,
                columns, new int[]{R.id.icon, R.id.title, R.id.summary});
        list.setAdapter(adapter);
    }
}
