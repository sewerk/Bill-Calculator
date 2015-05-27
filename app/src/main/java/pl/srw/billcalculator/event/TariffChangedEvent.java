package pl.srw.billcalculator.event;

import lombok.Value;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 27.05.15.
 */
@Value(staticConstructor = "of")
public class TariffChangedEvent {

    Provider provider;

}
