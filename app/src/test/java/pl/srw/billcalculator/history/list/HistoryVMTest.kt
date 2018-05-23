package pl.srw.billcalculator.history.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import pl.srw.billcalculator.data.ApplicationRepo
import pl.srw.billcalculator.data.bill.HistoryRepo
import pl.srw.billcalculator.db.History

class HistoryVMTest {

    @get:Rule val rule: TestRule = InstantTaskExecutorRule()

    val historyData = MutableLiveData<List<History>>()
    val applicationRepo: ApplicationRepo = mock()
    val historyRepo: HistoryRepo = mock {
        on { getAll() } doReturn historyData
    }

    val sut = HistoryVM(applicationRepo, historyRepo)

    @Test fun `retrieve history data on construction`() {
        verify(historyRepo).getAll()
    }

    @Test
    fun `share history data when retirved from repository`() {
        val observer: Observer<List<History>> = mock()
        sut.data.observeForever(observer)
        val data = emptyList<History>()

        historyData.value = data

        verify(observer).onChanged(data)
    }
}
