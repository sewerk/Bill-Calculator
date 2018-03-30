package pl.srw.billcalculator.bill.save

import pl.srw.billcalculator.bill.calculation.PgnigCalculatedBill
import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.PgnigBill
import pl.srw.billcalculator.db.PgnigPrices
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PgnigDbEntityCreator @Inject constructor(
    providerMapper: ProviderMapper
) : DbEntityCreator {

    private val pgnigPrices = providerMapper.getPrefsPrices(Provider.PGNIG) as pl.srw.billcalculator.settings.prices.PgnigPrices

    override fun createDbPrices() = pgnigPrices.convertToDb()

    override fun createDbBill(prices: Prices, input: NewBillInput): Bill {
        val pgnigPrices = prices as PgnigPrices
        val calculatedBill = calculateBill(pgnigPrices, input)
        return prepareDbBill(calculatedBill, pgnigPrices, input)
    }

    private fun calculateBill(prices: PgnigPrices, input: NewBillInput) =
        PgnigCalculatedBill(input.readingFrom, input.readingTo, input.dateFrom, input.dateTo, prices)

    private fun prepareDbBill(calculatedBill: PgnigCalculatedBill, prices: PgnigPrices, input: NewBillInput) =
        PgnigBill(
            null, input.readingFrom, input.readingTo,
            Dates.toDate(input.dateFrom), Dates.toDate(input.dateTo),
            calculatedBill.grossChargeSum.toDouble(), prices.id
        )
}
