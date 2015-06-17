package pl.srw.billcalculator.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class HistoryChangedEvent {

    private final Provider forProvider;
}
