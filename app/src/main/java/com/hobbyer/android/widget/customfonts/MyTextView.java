package com.hobbyer.android.widget.customfonts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.hobbyer.android.utils.LogUtils;
import java.util.HashMap;
import java.util.Map;
import com.hobbyer.android.R;


public class MyTextView extends AppCompatTextView {
    private static final String TAG = LogUtils.makeLogTag(MyTextView.class);

    protected static Map<String, Typeface> mTypefaces;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        if (mTypefaces == null)
            mTypefaces = new HashMap<>();

        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont;
        if (a.hasValue(R.styleable.CustomTextView_customFont))
            customFont = a.getString(R.styleable.CustomTextView_customFont);
        else
            customFont = "fonts/Roboto-Regular.ttf";
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String path) {
        Typeface typeface;
        if (mTypefaces.containsKey(path)) {
            typeface = mTypefaces.get(path);
        } else {
            typeface = Typeface.createFromAsset(ctx.getAssets(), path);
            mTypefaces.put(path, typeface);
        }
        setTypeface(typeface);
    }
}
