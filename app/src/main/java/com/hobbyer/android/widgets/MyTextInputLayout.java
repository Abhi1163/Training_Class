package com.hobbyer.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

import com.hobbyer.android.R;


public class MyTextInputLayout extends TextInputLayout {

    private TypeFactory mFontFactory;

    public MyTextInputLayout(Context context) {
        super(context);
    }

    public MyTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public MyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {


        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MyTextView,
                0, 0);
        int typefaceType;
        try {
            typefaceType = array.getInteger(R.styleable.MyTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(getTypeFace(typefaceType));
        }

    }

    private Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(getContext());

        switch (type) {
            case ViewConstants.MONREGULAR:
                return mFontFactory.monRegular;
            case ViewConstants.MONSEMIBOLD:
                return mFontFactory.monSemibold;
            case ViewConstants.MONALREGULAR:
                return mFontFactory.monAlRegular;
            case ViewConstants.MONALSEMIBOLD:
                return mFontFactory.monAlSemi;
            default:
                return mFontFactory.monRegular;
        }
    }

}
