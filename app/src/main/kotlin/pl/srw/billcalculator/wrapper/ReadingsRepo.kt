package pl.srw.billcalculator.wrapper

import io.reactivex.Single
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.CurrentReadingType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ReadingsRepo @Inject constructor() { // TODO make non-open class and fun

    open fun getPreviousReadingsFor(type: CurrentReadingType): Single<IntArray> = Single.just(type)
            .map { Database.queryCurrentReadings(it) }
}