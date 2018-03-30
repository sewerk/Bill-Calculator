package pl.srw.billcalculator.bill.save

import pl.srw.billcalculator.bill.calculation.CalculatedEnergyBill
import pl.srw.billcalculator.bill.calculation.PgeG11CalculatedBill
import pl.srw.billcalculator.bill.calculation.PgeG12CalculatedBill
import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.db.PgeG12Bill
import pl.srw.billcalculator.db.PgePrices
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates
import pl.srw.billcalculator.util.ProviderMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PgeDbEntityCreator @Inject constructor(
    providerMapper: ProviderMapper
) : DbEntityCreator {

    private val pgePrices = providerMapper.getPrefsPrices(Provider.PGE) as pl.srw.billcalculator.settings.prices.PgePrices

    override fun createDbPrices() = pgePrices.convertToDb()

    override fun createDbBill(prices: Prices, input: NewBillInput): Bill {
        val pgePrices = prices as PgePrices
        val calculatedBill = calculateBill(pgePrices, input)
        return prepareDbBill(calculatedBill, pgePrices, input)
    }

    private fun calculateBill(prices: PgePrices, input: NewBillInput): CalculatedEnergyBill =
        if (isTwoUnitTariff(input))
            PgeG12CalculatedBill(
                input.readingDayFrom, input.readingDayTo,
                input.readingNightFrom, input.readingNightTo,
                input.dateFrom, input.dateTo, prices
            )
        else
            PgeG11CalculatedBill(
                input.readingFrom, input.readingTo,
                input.dateFrom, input.dateTo, prices
            )

    private fun prepareDbBill(calculatedBill: CalculatedEnergyBill, prices: PgePrices, input: NewBillInput): Bill =
        if (isTwoUnitTariff(input))
            PgeG12Bill(
                null, input.readingDayFrom, input.readingDayTo,
                input.readingNightFrom, input.readingNightTo,
                Dates.toDate(input.dateFrom), Dates.toDate(input.dateTo),
                calculatedBill.grossChargeSum.toDouble(), prices.id
            )
        else
            PgeG11Bill(
                null, input.readingFrom, input.readingTo,
                Dates.toDate(input.dateFrom), Dates.toDate(input.dateTo),
                calculatedBill.grossChargeSum.toDouble(), prices.id
            )
}
