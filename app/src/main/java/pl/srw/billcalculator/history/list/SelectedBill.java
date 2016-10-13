package pl.srw.billcalculator.history.list;

import android.os.Parcel;
import android.os.Parcelable;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.persistence.type.BillType;

/**
 * Created by Kamil Seweryn.
 */
public class SelectedBill implements Parcelable {

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
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : BillType.values()[tmpType];
        this.billId = in.readLong();
        this.pricesId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeLong(this.billId);
        dest.writeLong(this.pricesId);
    }

    @Override
    public int describeContents() {
        return 0;
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
