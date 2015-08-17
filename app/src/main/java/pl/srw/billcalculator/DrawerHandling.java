package pl.srw.billcalculator;

import pl.srw.billcalculator.type.Provider;

/**
 * Created by kseweryn on 17.08.15.
 */
public interface DrawerHandling {

    void openDrawer();

    void backToHome();

    void showForm(Provider provider);

}
