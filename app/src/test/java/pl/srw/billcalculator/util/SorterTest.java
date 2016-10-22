package pl.srw.billcalculator.util;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class SorterTest {

    @Test
    public void revertOrder() throws Exception {
        // GIVEN
        int[] in = new int[]{ 1, 4, 2, 3};

        // WHEN
        final int[] result = Sorter.revert(in);

        // THEN
        assertThat(Arrays.toString(result), CoreMatchers.is("[3, 2, 4, 1]"));
    }

    @Test
    public void returnSameIfEmptyArray() throws Exception {
        // GIVEN
        int[] in = new int[0];

        // WHEN
        final int[] result = Sorter.revert(in);

        // THEN
        assertArrayEquals(in, result);
    }
}