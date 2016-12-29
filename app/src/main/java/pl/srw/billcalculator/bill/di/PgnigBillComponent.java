package pl.srw.billcalculator.bill.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.bill.activity.PgnigBillActivity;
import pl.srw.mfvp.di.component.MvpActivityScopeComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface PgnigBillComponent extends MvpActivityScopeComponent<PgnigBillActivity> {
}
