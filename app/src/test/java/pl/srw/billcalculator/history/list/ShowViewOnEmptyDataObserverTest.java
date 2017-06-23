package pl.srw.billcalculator.history.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShowViewOnEmptyDataObserverTest {

    @InjectMocks
    ShowViewOnEmptyDataObserver sut;

    @Mock View view;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onChanged_whenAdapterHasNoItems_makesViewVisible() throws Exception {
        // GIVEN
        RecyclerView.Adapter adapter = mock(RecyclerView.Adapter.class);
        when(adapter.getItemCount()).thenReturn(0);

        // WHEN
        sut.onChanged(adapter);

        // THEN
        verify(view).setVisibility(View.VISIBLE);
    }

    @Test
    public void onChanged_whenAdapterHasItems_makesViewInvisible() throws Exception {
        // GIVEN
        RecyclerView.Adapter adapter = mock(RecyclerView.Adapter.class);
        when(adapter.getItemCount()).thenReturn(2);

        // WHEN
        sut.onChanged(adapter);

        // THEN
        verify(view).setVisibility(View.INVISIBLE);
    }
}