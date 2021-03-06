package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Filterable;
import android.widget.ListAdapter;

public class InstantAutoCompleteTextInputEditText extends AppCompatAutoCompleteTextView {

    public InstantAutoCompleteTextInputEditText(Context context) {
        super(context);
    }

    public InstantAutoCompleteTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InstantAutoCompleteTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Copied from TextInputEditText
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        final InputConnection ic = super.onCreateInputConnection(outAttrs);
        if (ic != null && outAttrs.hintText == null) {
            // If we don't have a hint and our parent is a TextInputLayout, use it's hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            final ViewParent parent = getParent().getParent(); // FIX: TextInputLayout created extra child of FrameLayout
            outAttrs.hintText = ((TextInputLayout) parent).getHint();
        }
        return ic;
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
            if (hasWindowFocus())
                showDropDown();
        }
    }

    @Override
    public <T extends ListAdapter & Filterable> void setAdapter(T adapter) {
        super.setAdapter(adapter);
        performFiltering(null, KeyEvent.KEYCODE_UNKNOWN);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOnItemClickListener((parent, view, position, id) -> {
            TextInputLayout textInputLayout = (TextInputLayout) InstantAutoCompleteTextInputEditText.this.getParent().getParent();
            textInputLayout.setError(null);
        });
    }
}
