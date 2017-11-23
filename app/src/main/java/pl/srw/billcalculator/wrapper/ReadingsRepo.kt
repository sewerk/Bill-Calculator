package pl.srw.billcalculator.wrapper

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import pl.srw.billcalculator.persistence.Database
import pl.srw.billcalculator.persistence.type.CurrentReadingType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReadingsRepo @Inject constructor() {

    fun getPreviousReadingsFor(type: CurrentReadingType): LiveData<IntArray> {
        val data = MutableLiveData<IntArray>()
        data.value = Database.queryCurrentReadings(type) // TODO: do async
        return data
    }
}