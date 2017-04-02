package pl.srw.billcalculator.history.list;

import org.greenrobot.greendao.query.LazyList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.srw.billcalculator.Whitebox;
import pl.srw.billcalculator.db.History;
import pl.srw.billcalculator.history.list.item.HistoryItemClickListener;
import pl.srw.billcalculator.history.list.item.HistoryItemViewHolder;
import pl.srw.billcalculator.util.BillSelection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HistoryAdapterTest {

    @InjectMocks private HistoryAdapter sut;

    @Mock ShowViewOnEmptyDataObserver dataChangeObserver;
    @Mock HistoryItemClickListener clickListener;
    @Mock LazyList<History> lazyList;
    @Mock BillSelection selection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setData_whenDataSet_closesLazyList() throws Exception {
        // GIVEN
        Whitebox.setInternalState(sut, "lazyList", lazyList);

        // WHEN
        sut.setData(null);

        // THEN
        verify(lazyList).close();
    }

    @Test
    public void setData_setsData() throws Exception {
        // WHEN
        sut.setData(lazyList);

        // THEN
        assertNotNull(Whitebox.getInternalState(sut, "lazyList"));
    }

    @Test
    public void setData_notifiesDataChangeObserver() throws Exception {
        // WHEN
        sut.setData(null);

        // THEN
        verify(dataChangeObserver).onChanged(sut);
    }

    @Test
    public void onBindViewHolder_bindsHistoryEntry() throws Exception {
        // GIVEN
        final int position = 0;
        final History history = mock(History.class);
        when(lazyList.get(position)).thenReturn(history);
        Whitebox.setInternalState(sut, "lazyList", lazyList);
        final HistoryItemViewHolder viewHolder = mock(HistoryItemViewHolder.class);

        // WHEN
        sut.onBindViewHolder(viewHolder, position);

        // THEN
        verify(viewHolder).bindEntry(history, false);
    }

    @Test
    public void onBindViewHolder_whenBillSelected_bindsEntryWithSelectionTrue() throws Exception {
        // GIVEN
        final int position = 0;
        boolean selected = true;
        final History history = mock(History.class);
        final HistoryItemViewHolder viewHolder = mock(HistoryItemViewHolder.class);

        when(lazyList.get(position)).thenReturn(history);
        Whitebox.setInternalState(sut, "lazyList", lazyList);
        when(selection.isSelected(position)).thenReturn(selected);

        // WHEN
        sut.onBindViewHolder(viewHolder, position);

        // THEN
        verify(viewHolder).bindEntry(history, selected);
    }

    @Test
    public void getItemCount_whenListNotSet_returnsZero() throws Exception {
        // WHEN
        final int result = sut.getItemCount();

        // THEN
        assertEquals(0, result);
    }

    @Test
    public void getItemCount_whenListSet_returnsListSize() throws Exception {
        // GIVEN
        final int size = 2;
        Whitebox.setInternalState(sut, "lazyList", lazyList);
        when(lazyList.size()).thenReturn(size);

        // WHEN
        final int result = sut.getItemCount();

        // THEN
        assertEquals(size, result);
    }
}