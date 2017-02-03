package pl.srw.billcalculator.tester;

import android.support.test.espresso.ViewInteraction;

import pl.srw.billcalculator.R;
import pl.srw.billcalculator.type.Provider;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

public class FormTester extends Tester {

    public FormTester openForm(Provider provider) {
        openDrawer();
        switch (provider) {
            case PGE:
                clickDrawerMenu("energy from PGE");
                break;
            case PGNIG:
                clickDrawerMenu("gas from PGNiG");
                break;
            case TAURON:
                clickDrawerMenu("energy from Tauron");
                break;
        }
        return this;
    }

    public ViewInteraction autoCompleteContains(String text) {
        return onView(withId(R.id.autocomplete_text))
                .inRoot(not(isDialog()))//withDecorView(not(is(testRule.getActivity().getWindow().getDecorView())))
                .check(matches(withText(text)));
    }

    public void autoCompleteDisplaysInOrder(String... text) {
        for (int i = 0, textLength = text.length; i < textLength; i++) {
            onData(anything()).atPosition(i)
                    .inRoot(not(isDialog()))
                    .check(matches(withText(text[i])));
        }
    }

    public void putIntoReadingFrom(String text) {
        typeInto(R.id.form_entry_reading_from_input, text);
    }

    public FormTester closeForm() {
        clickView(R.id.close_button);
        return this;
    }
}
