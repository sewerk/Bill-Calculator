package pl.srw.billcalculator.type;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {
    PGE(R.drawable.pge_on_pgnig, R.drawable.pge),
    PGNIG(R.drawable.pgnig_on_pge, R.drawable.pgnig);

    public int drawableId;
    public int logoDrawableId;

    BillType(final int drawable, final int logoDrawableId) {
        this.drawableId = drawable;
        this.logoDrawableId = logoDrawableId;
    }

    public static BillType mapFrom(pl.srw.billcalculator.persistence.type.BillType persistenceType) {
        switch (persistenceType) {
            case PGNIG:
                return PGNIG;
            case PGE_G11:
            case PGE_G12:
                return PGE;
        }
        throw new RuntimeException("Unknown bill type: " + persistenceType);
    }

}
