package pl.srw.billcalculator;

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
