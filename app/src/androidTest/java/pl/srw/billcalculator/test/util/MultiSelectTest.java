package pl.srw.billcalculator.test.util;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collection;

import pl.srw.billcalculator.util.MultiSelect;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
/**
 * Created by Kamil Seweryn.
 */
@RunWith(AndroidJUnit4.class)
public class MultiSelectTest {

    private MultiSelect<Bundle> sut;

    @Before
    public void setUp() {
        sut = new MultiSelect<>();
    }

    @Test
    public void shouldConfirmSelectionWhenSelected() {
        sut.select(1, new Bundle());
        assertThat(sut.isSelected(1), is(true));
    }

    @Test
    public void shouldDenySelectedWhenNotSelected() {
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldDenySelectedWhenDeselected() {
        sut.select(1, new Bundle());
        sut.deselect(1);
        assertThat(sut.isSelected(1), is(false));
    }

    @Test
    public void shouldConfirmAnySelectedWhenSelected() {
        sut.select(1, new Bundle());
        assertThat(sut.isAnySelected(), is(true));
    }

    @Test
    public void shouldDenyAnySelectedWhenNonSelected() {
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDelesected() {
        sut.select(1, new Bundle());
        sut.deselect(1);
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldDenyAnySelectedWhenDeselectAll() {
        sut.select(1, new Bundle());
        sut.deselectAll();
        assertThat(sut.isAnySelected(), is(false));
    }

    @Test
    public void shouldReturnItemsSelected() {
        Bundle a = new Bundle();
        Bundle b = new Bundle();
        Bundle c = new Bundle();
        sut.select(1, a);
        sut.select(3, c);
        sut.select(2, b);

        final Collection<Bundle> result = sut.getItems();
        assertThat(result.size(), is(3));
        assertThat(result.contains(a), is(true));
        assertThat(result.contains(b), is(true));
        assertThat(result.contains(c), is(true));
    }

    @Test
    public void shouldReturnSelectedPositionsInReversOrder() {
        sut.select(3, new Bundle());
        sut.select(0, new Bundle());
        sut.select(4, new Bundle());
        sut.select(2, new Bundle());

        final int[] result = sut.getPositionsReverseOrder();
        assertThat(result.length, is(4));
        assertThat(Arrays.toString(result), is("[4, 3, 2, 0]"));
    }
}
