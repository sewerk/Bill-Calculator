package pl.srw.billcalculator.settings.activity;

import android.support.annotation.StringRes;

import java.util.List;
import java.util.Map;

/**
 * Created by kseweryn on 10.06.15.
 */
public interface SettingsViewing {

    void fillList(List<Map<String, Object>> entries, String[] columns);

    String getString(@StringRes int strRes);
}
