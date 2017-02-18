package pl.srw.billcalculator.tester;

public class HistoryTester extends Tester {

    private AppTester parent;
    private BillTester billTester = new BillTester(parent);

    public HistoryTester(AppTester parent) {
        this.parent = parent;
    }

    public BillTester openBillWithReadings(String from, String to) {
        clickText(from + " - " + to);
        return billTester;
    }
}
