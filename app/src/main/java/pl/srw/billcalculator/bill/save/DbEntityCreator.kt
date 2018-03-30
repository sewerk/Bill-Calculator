package pl.srw.billcalculator.bill.save

import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.Prices

interface DbEntityCreator {
    fun createDbPrices(): Prices
    fun createDbBill(prices: Prices, input: NewBillInput): Bill

    fun isTwoUnitTariff(input: NewBillInput) = input.readingDayTo > 0
}
