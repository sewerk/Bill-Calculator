package pl.srw.billcalculator.intent;

import android.content.Context;

import pl.srw.billcalculator.type.BillType;
import pl.srw.billcalculator.worker.PgnigBillStoringService;

/**
 * Created by Kamil Seweryn.
 */
public class BillStoringServiceIntentFactory {

    public static IntentCreator of(final Context context, final BillType billType) {
        switch (billType) {
//TODO            case PGE:
//                return new IntentCreator(context, PgeBillActivity.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillStoringService.class);
        }
        throw new RuntimeException("Type " + billType + " is not handled.");
    }
}
