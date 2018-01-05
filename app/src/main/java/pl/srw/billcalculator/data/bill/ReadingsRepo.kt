package pl.srw.billcalculator.data.bill

import io.reactivex.Single
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.CurrentReadingType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingsRepo @Inject constructor() {

    fun getPreviousReadingsFor(type: CurrentReadingType): Single<IntArray> = Single.just(type)
            .map { Database.queryCurrentReadings(it) }
}
