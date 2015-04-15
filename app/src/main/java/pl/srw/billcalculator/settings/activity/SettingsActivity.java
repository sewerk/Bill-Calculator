package pl.srw.billcalculator.settings.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import nucleus.view.RequiresPresenter;
import pl.srw.billcalculator.BackableActivity;
import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn
 */
@RequiresPresenter(SettingsPresenter.class)
public class SettingsActivity extends BackableActivity<SettingsPresenter> {

    @InjectView(R.id.list) ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_list);
        ButterKnife.inject(this);
    }

    public void fillList(List<Map<String, String>> entries) {
        SimpleAdapter adapter = new SimpleAdapter(this, entries, R.layout.preference_item,
                SettingsPresenter.COLUMNS, new int[]{R.id.title, R.id.summary});
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().providerChoosenAt(position);
            }
        });
    }
}
