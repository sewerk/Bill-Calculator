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

    public static BillType mapFrom(pl.srw.billcalculator.persistence.type.BillType persistenceType) {
        switch (persistenceType) {
            case PGNIG:
                return PGNIG;
            case PGE:
            case PGE_G12:
                return PGE;
        }
        throw new RuntimeException("Unknown bill type: " + persistenceType);
    }

}
