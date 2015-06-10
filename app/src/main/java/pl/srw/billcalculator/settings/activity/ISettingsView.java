package pl.srw.billcalculator.settings.activity;

import android.support.annotation.StringRes;

import java.util.List;
import java.util.Map;

/**
 * Created by kseweryn on 10.06.15.
 */
public interface ISettingsView {

    void fillList(List<Map<String, String>> entries, String[] columns);

    String getString(@StringRes int strRes);
}
