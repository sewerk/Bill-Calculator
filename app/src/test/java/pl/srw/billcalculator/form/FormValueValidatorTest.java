package pl.srw.billcalculator.form;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import pl.srw.billcalculator.R;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

/**
 * Created by kseweryn on 29.06.15.
 */
@RunWith(JUnitParamsRunner.class)
public class FormValueValidatorTest {

    private FormValueValidator sut = new FormValueValidator();

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock private FormValueValidator.OnErrorCallback onErrorCallback;

    @Test
    @Parameters
    public void testIsValueFilled(String input, boolean expected) throws Exception {
        assertThat(sut.isValueFilled(input, onErrorCallback), is(expected));
        Mockito.verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }

    private Object parametersForTestIsValueFilled() {
        return new Object[] { new Object[]{"" , false}, new Object[]{"1", true}};
    }

    @Test
    @Parameters({"1,2|true", "2,2|false", "2,1|false"})
    public void testIsValueOrderCorrect(String prev, String curr, boolean expected) throws Exception {
        assertThat(sut.isValueOrderCorrect(prev, curr, onErrorCallback), is(expected));
        Mockito.verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }

    @Test
    @Parameters({"01/01/2015, 02/01/2015|true",
                 "02/01/2015, 02/01/2015|false",
                 "02/01/2015, 01/01/2015|false"})
    public void testIsDatesOrderCorrect(String fromDate, String toDate, boolean expected) throws Exception {
        assertThat(sut.isDatesOrderCorrect(fromDate, toDate, onErrorCallback), is(expected));
        Mockito.verify(onErrorCallback, times(expected ? 0 : 1)).onError(anyInt());
    }
}