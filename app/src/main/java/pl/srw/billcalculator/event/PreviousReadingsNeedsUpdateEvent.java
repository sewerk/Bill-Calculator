package pl.srw.billcalculator.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 12.06.15.
 */
@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class PreviousReadingsNeedsUpdateEvent {

    private final Provider forProvider;
}
