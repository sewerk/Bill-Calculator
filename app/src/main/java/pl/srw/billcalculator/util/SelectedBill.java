package pl.srw.billcalculator.util;

import hrisey.Parcelable;
import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgeG11Bill;
import pl.srw.billcalculator.db.PgeG12Bill;
import pl.srw.billcalculator.db.PgnigBill;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
@Parcelable
public class SelectedBill implements android.os.Parcelable {

    private final BillType type;
    private final Long billId;
    private final Long pricesId;

    public SelectedBill(final Bill bill) {
        if (bill instanceof PgeG11Bill) {
            final PgeG11Bill pgeG11Bill = (PgeG11Bill) bill;
            this.type = BillType.PGE_G11;
            this.billId = pgeG11Bill.getId();
            this.pricesId = pgeG11Bill.getPricesId();
        } else if (bill instanceof PgeG12Bill) {
            final PgeG12Bill pgeG12Bill = (PgeG12Bill) bill;
            this.type = BillType.PGE_G12;
            this.billId = pgeG12Bill.getId();
            this.pricesId = pgeG12Bill.getPricesId();
        } else if (bill instanceof PgnigBill) {
            final PgnigBill pgnigBill = (PgnigBill) bill;
            this.type = BillType.PGNIG;
            this.billId = pgnigBill.getId();
            this.pricesId = pgnigBill.getPricesId();
        } else
            throw new RuntimeException("Bill type is not handled");
    }

    public BillType getType() {
        return type;
    }

    public Long getBillId() {
        return billId;
    }

    public Long getPricesId() {
        return pricesId;
    }
}
