package pl.srw.billcalculator.form;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class FormValueValidatorTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private FormValueValidator.OnErrorCallback onErrorCallback;

    @Test
    @Parameters
    public void testIsValueFilled(String input, boolean expected) throws Exception {
        assertThat(FormValueValidator.isValueFilled(input, onErrorCallback), is(expected));
        verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }

    private Object parametersForTestIsValueFilled() {
        return new Object[] { new Object[]{"" , false}, new Object[]{"1", true}};
    }

    @Test
    @Parameters({"1,2|true", "2,2|false", "2,1|false"})
    public void testIsValueOrderCorrect(String prev, String curr, boolean expected) throws Exception {
        assertThat(FormValueValidator.isValueOrderCorrect(prev, curr, onErrorCallback), is(expected));
        verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }

    @Test
    @Parameters({"01/01/2015, 02/01/2015|true",
            "02/01/2015, 02/01/2015|false",
            "02/01/2015, 01/01/2015|false"})
    public void testIsDatesOrderCorrect(String fromDate, String toDate, boolean expected) throws Exception {
        assertThat(FormValueValidator.isDatesOrderCorrect(fromDate, toDate, onErrorCallback), is(expected));
        verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }
}