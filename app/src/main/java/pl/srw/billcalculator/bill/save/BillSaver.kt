package pl.srw.billcalculator.bill.save

import io.reactivex.Completable
import io.reactivex.Single
import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.data.bill.HistoryRepo
import pl.srw.billcalculator.type.Provider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillSaver @Inject constructor(
    private val pgeDbEntityCreator: PgeDbEntityCreator,
    private val pgnigDbEntityCreator: PgnigDbEntityCreator,
    private val tauronDbEntityCreator: TauronDbEntityCreator,
    private val historyRepo: HistoryRepo
) {

    fun storeBill(input: NewBillInput): Completable {
        val creator = chooseEntityCreator(input.provider)

        return Single.fromCallable { creator.createDbPrices() }
            .flatMap { prices -> historyRepo.insert(prices) }
            .map { prices -> creator.createDbBill(prices, input) }
            .flatMap { bill -> historyRepo.insert(bill) }
            .toCompletable()
    }

    private fun chooseEntityCreator(provider: Provider): DbEntityCreator =
        when (provider) {
            Provider.PGE -> pgeDbEntityCreator
            Provider.PGNIG -> pgnigDbEntityCreator
            Provider.TAURON -> tauronDbEntityCreator
        }
}
