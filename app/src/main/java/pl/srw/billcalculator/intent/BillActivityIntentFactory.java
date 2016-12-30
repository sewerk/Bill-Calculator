package pl.srw.billcalculator.intent;

import android.content.Context;
import android.content.Intent;

import pl.srw.billcalculator.bill.activity.PgeBillActivity;
import pl.srw.billcalculator.bill.activity.PgnigBillActivity;
import pl.srw.billcalculator.bill.activity.TauronBillActivity;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.db.TauronG11Bill;
import pl.srw.billcalculator.db.TauronG12Bill;
import pl.srw.billcalculator.persistence.type.BillType;
import pl.srw.billcalculator.type.EnumVariantNotHandledException;
import pl.srw.billcalculator.type.Provider;

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

    public static Intent create(Context context, Bill bill) {
        final BillType billType = BillType.valueOf(bill);
        switch (billType) {
            case PGE_G11:
                return new IntentCreator(context, PgeBillActivity.class).from((PgeG11Bill) bill);
            case PGE_G12:
                return new IntentCreator(context, PgeBillActivity.class).from((PgeG12Bill) bill);
            case PGNIG:
                return new IntentCreator(context, PgnigBillActivity.class).from((PgnigBill) bill);
            case TAURON_G11:
                return new IntentCreator(context, TauronBillActivity.class).from((TauronG11Bill) bill);
            case TAURON_G12:
                return new IntentCreator(context, TauronBillActivity.class).from((TauronG12Bill) bill);
        }
        throw new EnumVariantNotHandledException(billType);
    }

}
