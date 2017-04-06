package com.app.archirayan.teamon.Widget;

import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * Created by archirayan on 05-Dec-16.
 */
public class AsteriskPasswordTransformationMethod implements TransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    /**
     * This method is called when the TextView that uses this
     * TransformationMethod gains or loses focus.
     *
     * @param view
     * @param sourceText
     * @param focused
     * @param direction
     * @param previouslyFocusedRect
     */
    @Override
    public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
