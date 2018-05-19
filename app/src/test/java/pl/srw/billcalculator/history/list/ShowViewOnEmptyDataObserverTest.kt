package pl.srw.billcalculator.history.list

import android.view.View
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.mockito.Mockito.verify
import pl.srw.billcalculator.db.History

class ShowViewOnEmptyDataObserverTest {

    val view: View = mock()

    val sut = ShowViewOnEmptyDataObserver(view)

    @Test
    fun onChanged_whenAdapterHasNoItems_makesViewVisible() {
        // GIVEN
        val data = emptyList<History>()

        // WHEN
        sut.onChanged(data)

        // THEN
        verify(view).visibility = View.VISIBLE
    }

    @Test
    fun onChanged_whenAdapterHasItems_makesViewInvisible() {
        // GIVEN
        val data = listOf<History>(mock(), mock())

        // WHEN
        sut.onChanged(data)

        // THEN
        verify(view).visibility = View.INVISIBLE
    }
}
