package pl.srw.billcalculator.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;

import pl.srw.billcalculator.db.Bill;
import pl.srw.billcalculator.db.PgnigBill;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class BillSelectionTest {

    private BillSelection sut = new BillSelection();

    @Test
    public void shouldConfirmSelectionWhenSelected() {
        sut.select(1, new PgnigBill());
        assertThat(sut.isSelected(1), is(true));
    }

    @Test
    public void shouldDenySelectedWhenNotSelected() {
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldDenySelectedWhenDeselected() {
        sut.select(1, new PgnigBill());
        sut.deselect(1);
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldConfirmAnySelectedWhenSelected() {
        sut.select(1, new PgnigBill());
        assertThat(sut.isAnySelected(), is(true));
    }

    @Test
    public void shouldDenyAnySelectedWhenNonSelected() {
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDelesected() {
        sut.select(1, new PgnigBill());
        sut.deselect(1);
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDeselectAll() {
        sut.select(1, new PgnigBill());
        sut.deselectAll();
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldReturnItemsSelected() {
        Bill a = new PgnigBill();
        Bill b = new PgnigBill();
        Bill c = new PgnigBill();
        sut.select(1, a);
        sut.select(3, c);
        sut.select(2, b);

        final Collection<Bill> result = sut.getItems();
        assertThat(result.size(), is(3));
        assertThat(result.contains(a), is(true));
        assertThat(result.contains(b), is(true));
        assertThat(result.contains(c), is(true));
    }

    @Test
    public void shouldReturnSelectedPositionsInReversOrder() {
        sut.select(3, new PgnigBill());
        sut.select(0, new PgnigBill());
        sut.select(4, new PgnigBill());
        sut.select(2, new PgnigBill());

        final int[] result = sut.getPositionsReverseOrder();
        assertThat(result.length, is(4));
        assertThat(Arrays.toString(result), is("[4, 3, 2, 0]"));
    }
}