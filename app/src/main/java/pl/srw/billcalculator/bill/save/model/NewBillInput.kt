package pl.srw.billcalculator.bill.save.model

import org.threeten.bp.LocalDate
import pl.srw.billcalculator.form.FormVM
import pl.srw.billcalculator.form.fragment.FormFragment
import pl.srw.billcalculator.type.Provider
import pl.srw.billcalculator.util.Dates

data class NewBillInput(
    val provider: Provider,
    val readingFrom: Int,
    val readingTo: Int,
    val readingDayFrom: Int,
    val readingDayTo: Int,
    val readingNightFrom: Int,
    val readingNightTo: Int,
    val dateFrom: LocalDate,
    val dateTo: LocalDate
) {
    companion object {
        fun from(vm: FormVM, singleReadings: Boolean): NewBillInput =
            if (singleReadings) NewBillInput(
                vm.provider,
                vm.readingFrom.toInt(),
                vm.readingTo.toInt(),
                0,
                0,
                0,
                0,
                Dates.parse(vm.dateFrom, FormFragment.DATE_PATTERN),
                Dates.parse(vm.dateTo, FormFragment.DATE_PATTERN)
            )
            else NewBillInput(
                vm.provider,
                0,
                0,
                vm.readingDayFrom.toInt(),
                vm.readingDayTo.toInt(),
                vm.readingNightFrom.toInt(),
                vm.readingNightTo.toInt(),
                Dates.parse(vm.dateFrom, FormFragment.DATE_PATTERN),
                Dates.parse(vm.dateTo, FormFragment.DATE_PATTERN)
            )
    }
}
