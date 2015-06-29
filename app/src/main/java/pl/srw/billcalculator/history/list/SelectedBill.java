package pl.srw.billcalculator.history.list;

import hrisey.Parcelable;
import pl.srw.billcalculator.db.Bill;
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
        this.billId = bill.getId();
        this.pricesId = bill.getPricesId();
        this.type = BillType.valueOf(bill);
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
