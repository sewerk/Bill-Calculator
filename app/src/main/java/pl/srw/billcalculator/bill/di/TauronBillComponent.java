package pl.srw.billcalculator.bill.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.bill.activity.TauronBillActivity;
import pl.srw.mfvp.di.component.MvpComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface TauronBillComponent extends MvpComponent<TauronBillActivity> {
}
