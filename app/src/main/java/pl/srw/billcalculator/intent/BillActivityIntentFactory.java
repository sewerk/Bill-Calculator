package pl.srw.billcalculator.intent;

import android.content.Context;

import pl.srw.billcalculator.PgeBillActivity;
import pl.srw.billcalculator.PgnigBillActivity;
import pl.srw.billcalculator.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public final class BillActivityIntentFactory {

    private BillActivityIntentFactory() {}

    public static IntentCreator of(final Context context, final BillType billType) {
        switch (billType) {
            case PGE:
                return new IntentCreator(context, PgeBillActivity.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillActivity.class);
        }
        throw new RuntimeException("Type " + billType + " is not handled.");
    }

}
