package pl.srw.billcalculator.intent;

import android.content.Context;

import pl.srw.billcalculator.bill.service.PgeBillStoringService;
import pl.srw.billcalculator.bill.service.PgnigBillStoringService;
import pl.srw.billcalculator.bill.service.TauronBillStoringService;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public class BillStoringServiceIntentFactory {

    public static IntentCreator of(final Context context, final Provider billType) {
        switch (billType) {
            case PGE: // TODO: move to mapper?
                return new IntentCreator(context, PgeBillStoringService.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillStoringService.class);
            case TAURON:
                return new IntentCreator(context, TauronBillStoringService.class);
        }
        throw new EnumVariantNotHandledException(billType);
    }
}
