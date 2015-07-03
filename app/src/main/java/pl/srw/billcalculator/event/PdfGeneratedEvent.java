package pl.srw.billcalculator.event;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Created by kseweryn on 01.07.15.
 */
@Value
@AllArgsConstructor(suppressConstructorProperties = true)
public class PdfGeneratedEvent {

    private final String path;
}
