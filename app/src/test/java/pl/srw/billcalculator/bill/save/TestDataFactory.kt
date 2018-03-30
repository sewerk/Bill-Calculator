package pl.srw.billcalculator.bill.save

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.bill.save.model.NewBillInput
import pl.srw.billcalculator.type.Provider

internal fun buildInput(
    provider: Provider = Provider.PGE,
    readingFrom: Int = 1,
    readingTo: Int = 2,
    readingDayFrom: Int = 11,
    readingDayTo: Int = 12,
    readingNightFrom: Int = 21,
    readingNightTo: Int = 22,
    dateFrom: LocalDate = LocalDate.now(),
    dateTo: LocalDate = dateFrom.plusMonths(1)
) = NewBillInput(provider, readingFrom, readingTo, readingDayFrom, readingDayTo, readingNightFrom, readingNightTo, dateFrom, dateTo)
