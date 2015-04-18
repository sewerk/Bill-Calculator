package pl.srw.billcalculator.type;

import pl.srw.billcalculator.R;

/**
 * Created by Kamil Seweryn.
 */
public enum BillType {
    PGE(R.drawable.pge_small),
    PGNIG(R.drawable.pgnig_small);

    public final int logoSmallDrawableId;

    BillType(final int logoDrawableId) {
        this.logoSmallDrawableId = logoDrawableId;
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
