package pl.srw.billcalculator.intent;

import android.content.Context;

import pl.srw.billcalculator.bill.activity.PgeBillActivity;
import pl.srw.billcalculator.bill.activity.PgnigBillActivity;
import pl.srw.billcalculator.bill.activity.TauronBillActivity;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;

/**
 * Created by Kamil Seweryn.
 */
public final class BillActivityIntentFactory {

    private BillActivityIntentFactory() {}

    public static IntentCreator of(final Context context, final Provider billType) {
        switch (billType) {
            case PGE: // TODO: move to mapper?
                return new IntentCreator(context, PgeBillActivity.class);
            case PGNIG:
                return new IntentCreator(context, PgnigBillActivity.class);
            case TAURON:
                return new IntentCreator(context, TauronBillActivity.class);
        }
        throw new EnumVariantNotHandledException(billType);
    }

}
