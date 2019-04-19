package com.hobbyer.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.hobbyer.android.R;

public class MyTextViewBold extends AppCompatTextView {
    public MyTextViewBold(Context context) {
        super(context);
        init(null);
    }

    public MyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/MontserratAlternates-SemiBold.ttf");
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView2);
            String fontName = a.getString(R.styleable.MyTextView2_fontName);

            if (fontName != null) {
                tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
            }
            a.recycle();
        }
        setTypeface(tf);
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
