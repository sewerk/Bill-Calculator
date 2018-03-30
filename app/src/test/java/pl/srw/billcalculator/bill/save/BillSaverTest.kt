package pl.srw.billcalculator.bill.save

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Test
import pl.srw.billcalculator.data.bill.HistoryRepo
import pl.srw.billcalculator.db.Bill
import pl.srw.billcalculator.db.PgeG11Bill
import pl.srw.billcalculator.db.PgePrices
import pl.srw.billcalculator.db.PgnigBill
import pl.srw.billcalculator.db.PgnigPrices
import pl.srw.billcalculator.db.Prices
import pl.srw.billcalculator.db.TauronG11Bill
import pl.srw.billcalculator.db.TauronPrices
import pl.srw.billcalculator.type.Provider

class BillSaverTest {

    val pgeDbEntityCreator: PgeDbEntityCreator = mock {
        on { createDbPrices() } doReturn mock<PgePrices>()
        on { createDbBill(any(), any()) } doReturn mock<PgeG11Bill>()
    }
    val pgnigDbEntityCreator: PgnigDbEntityCreator = mock {
        on { createDbPrices() } doReturn mock<PgnigPrices>()
        on { createDbBill(any(), any()) } doReturn mock<PgnigBill>()
    }
    val tauronDbEntityCreator: TauronDbEntityCreator = mock {
        on { createDbPrices() } doReturn mock<TauronPrices>()
        on { createDbBill(any(), any()) } doReturn mock<TauronG11Bill>()
    }
    val historyRepo: HistoryRepo = mock {
        on { insert(any<Prices>()) } doReturn Single.just(mock())
        on { insert(any<Bill>()) } doReturn Single.just(mock())
    }

    val sut = BillSaver(pgeDbEntityCreator, pgnigDbEntityCreator, tauronDbEntityCreator, historyRepo)

    @Test
    fun `store bill and prices in order`() {
        val input = buildInput()

        sut.storeBill(input).test()

        inOrder(historyRepo) {
            verify(historyRepo).insert(any<Prices>())
            verify(historyRepo).insert(any<Bill>())
        }
    }

    @Test
    fun `store bill uses Pge entities when provider is PGE`() {
        val input = buildInput(provider = Provider.PGE)

        sut.storeBill(input).test()

        verify(pgeDbEntityCreator).createDbPrices()
        verify(pgeDbEntityCreator).createDbBill(any(), eq(input))
    }

    @Test
    fun `store bill uses Pgnig entities when provider is PGNIG`() {
        val input = buildInput(provider = Provider.PGNIG)

        sut.storeBill(input).test()

        verify(pgnigDbEntityCreator).createDbPrices()
        verify(pgnigDbEntityCreator).createDbBill(any(), eq(input))
    }
}
