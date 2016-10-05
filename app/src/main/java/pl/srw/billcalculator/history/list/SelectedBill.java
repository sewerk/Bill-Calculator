package pl.srw.billcalculator.history.list;

import android.os.Parcel;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
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

    protected SelectedBill(Parcel in) {
        type = BillType.values()[in.readInt()];
        billId = in.readLong();
        pricesId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type.ordinal());
        dest.writeLong(billId);
        dest.writeLong(pricesId);
    }

    @Override
    public int describeContents() {
        return 0; // TODO: check if 0 is ok
    }

    public static final Creator<SelectedBill> CREATOR = new Creator<SelectedBill>() {
        @Override
        public SelectedBill createFromParcel(Parcel in) {
            return new SelectedBill(in);
        }

        @Override
        public SelectedBill[] newArray(int size) {
            return new SelectedBill[size];
        }
    };
}
