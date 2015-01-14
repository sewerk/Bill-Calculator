package pl.srw.billcalculator.type;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {
    PGE(R.drawable.bill_type_pge),
    PGNIG(R.drawable.bill_type_pgnig);

    public int drawableId;

    BillType(final int drawable) {
        this.drawableId = drawable;
    }
}
