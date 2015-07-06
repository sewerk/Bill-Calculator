package pl.srw.billcalculator.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by kseweryn on 30.06.15.
 */
public class StringsTest {

    @Test
    public void testToArrayFromCollection() throws Exception {
        Collection<Integer> intCollection = Arrays.asList(1, 2);
        assertThat(Strings.toArray(intCollection), is(new String[]{"1", "2"}));
    }

    @Test
    public void testToArrayFromArray() throws Exception {
        int[] intArray = new int[] {1, 2, 3};
        assertThat(Strings.toArray(intArray), is(new String[]{"1","2","3"}));
    }
}