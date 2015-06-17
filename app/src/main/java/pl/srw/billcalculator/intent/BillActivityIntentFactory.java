package pl.srw.billcalculator.intent;

import android.content.Context;

import pl.srw.billcalculator.bill.PgeBillActivity;
import pl.srw.billcalculator.bill.PgnigBillActivity;
import pl.srw.billcalculator.bill.TauronBillActivity;
import pl.srw.billcalculator.type.Provider;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;

/**
 * Created by Kamil Seweryn.
 */
public final class BillActivityIntentFactory {

    private BillActivityIntentFactory() {}

    public static IntentCreator of(final Context context, final Provider billType) {
        switch (billType) {
            case PGE:
                return new IntentCreator(context, PgeBillActivity.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillActivity.class);
            case TAURON:
                return new IntentCreator(context, TauronBillActivity.class);
        }
        throw new EnumVariantNotHandledException(billType);
    }

}
