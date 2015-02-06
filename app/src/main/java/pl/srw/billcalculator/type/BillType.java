package pl.srw.billcalculator.type;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {
    PGE(R.drawable.pge_on_pgnig),
    PGNIG(R.drawable.pgnig_on_pge);

    public int drawableId;

    BillType(final int drawable) {
        this.drawableId = drawable;
    }
}
