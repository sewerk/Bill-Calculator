package pl.srw.billcalculator.bill.di;

import dagger.Subcomponent;
import pl.srw.billcalculator.bill.activity.PgeBillActivity;
import pl.srw.mfvp.di.MvpComponent;
import pl.srw.mfvp.di.scope.RetainActivityScope;

@RetainActivityScope
@Subcomponent
public interface PgeBillComponent extends MvpComponent<PgeBillActivity> {
}
