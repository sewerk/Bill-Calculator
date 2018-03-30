package pl.srw.billcalculator.bill.save

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill
import pl.srw.billcalculator.bill.calculation.TauronG11CalculatedBill
import pl.srw.billcalculator.bill.calculation.TauronG12CalculatedBill
import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.db.TauronG11Bill
import pl.srw.billcalculator.db.TauronG12Bill
import pl.srw.billcalculator.db.TauronPrices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TauronDbEntityCreator @Inject constructor(
    providerMapper: ProviderMapper
) : DbEntityCreator {

    private val tauronPrices = providerMapper.getPrefsPrices(Provider.TAURON) as pl.srw.billcalculator.settings.prices.TauronPrices

    override fun createDbPrices() = tauronPrices.convertToDb()

    override fun createDbBill(prices: Prices, input: NewBillInput): Bill {
        val tauronPrices = prices as TauronPrices
        val calculatedBill = calculateBill(tauronPrices, input)
        return prepareDbBill(calculatedBill, prices, input)
    }

    private fun calculateBill(prices: TauronPrices, input: NewBillInput): CalculatedEnergyBill =
        if (isTwoUnitTariff(input))
            TauronG12CalculatedBill(
                input.readingDayFrom, input.readingDayTo,
                input.readingNightFrom, input.readingNightTo,
                input.dateFrom, input.dateTo, prices
            )
        else
            TauronG11CalculatedBill(
                input.readingFrom, input.readingTo,
                input.dateFrom, input.dateTo, prices
            )

    private fun prepareDbBill(calculatedBill: CalculatedEnergyBill, prices: TauronPrices, input: NewBillInput): Bill =
        if (isTwoUnitTariff(input))
            TauronG12Bill(
                null, input.readingDayFrom, input.readingDayTo,
                input.readingNightFrom, input.readingNightTo,
                Dates.toDate(input.dateFrom), Dates.toDate(input.dateTo),
                calculatedBill.grossChargeSum.toDouble(), prices.id
            ) else
            TauronG11Bill(
                null, input.readingFrom, input.readingTo,
                Dates.toDate(input.dateFrom), Dates.toDate(input.dateTo),
                calculatedBill.grossChargeSum.toDouble(), prices.id
            )
}
