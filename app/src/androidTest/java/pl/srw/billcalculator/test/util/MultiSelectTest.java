package pl.srw.billcalculator.test.util;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import pl.srw.billcalculator.util.MultiSelect;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
public class MultiSelectTest {

    private MultiSelect<Integer, String> sut;

    @Before
    public void setUp() {
        sut = new MultiSelect<>();
    }

    @Test
    public void shouldConfirmSelectionWhenSelected() {
        sut.select(1, "Item");
        assertThat(sut.isSelected(1), is(true));
    }

    @Test
    public void shouldDenySelectedWhenNotSelected() {
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldDenySelectedWhenDeselected() {
        sut.select(1, "Item");
        sut.deselect(1);
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldConfirmAnySelectedWhenSelected() {
        sut.select(1, "Item");
        assertThat(sut.isAnySelected(), is(true));
    }

    @Test
    public void shouldDenyAnySelectedWhenNonSelected() {
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDelesected() {
        sut.select(1, "Item");
        sut.deselect(1);
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDeselectAll() {
        sut.select(1, "Item");
        sut.deselectAll();
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldReturnItemsSelected() {
        sut.select(1, "A");
        sut.select(3, "C");
        sut.select(2, "B");

        final Collection<String> result = sut.getItems();
        assertThat(result.size(), is(3));
        assertThat(result.contains("A"), is(true));
        assertThat(result.contains("B"), is(true));
        assertThat(result.contains("C"), is(true));
    }

    @Test
    public void shouldReturnSelectedPositionsInReversOrder() {
        sut.select(3, "C");
        sut.select(0, "A");
        sut.select(4, "D");
        sut.select(2, "B");

        final Collection<Integer> result = sut.getPositionsReverseOrder();
        assertThat(result.size(), is(4));
        assertThat(result.toString(), is("[4, 3, 2, 0]"));
    }
}
