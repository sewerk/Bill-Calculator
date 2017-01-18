/*
 * Copyright (c) 2016 Nick Mobile as an unpublished work. Neither
 * this material nor any portion thereof may be copied or distributed
 * without the express written consent of Nick Mobile.
 * This material also contains proprietary and confidential information
 * of 2016 Nick Mobile and its suppliers, and may not be used by or
 * disclosed to any person, in whole or in part, without the prior written
 * consent of 2016 Nick Mobile.
 */
package pl.srw.billcalculator.form.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.ListAdapter;

import hugo.weaving.DebugLog;

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

    @DebugLog
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
        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextInputLayout textInputLayout = (TextInputLayout) InstantAutoCompleteTextInputEditText.this.getParent().getParent();
                textInputLayout.setError(null);
            }
        });
    }
}
