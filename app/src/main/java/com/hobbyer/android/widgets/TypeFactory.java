package com.hobbyer.android.widgets;

import android.content.Context;
import android.graphics.Typeface;

class TypeFactory {

    Typeface monRegular;
    Typeface monSemibold;
    Typeface monAlRegular;
    Typeface monAlSemi;
    Typeface monExtra;
    Typeface monBold;
    Typeface monMedium;

    public TypeFactory(Context context) {

        String REGULAR = "fonts/Montserrat-Regular.ttf";
        monRegular = Typeface.createFromAsset(context.getAssets(), REGULAR);

        String SEMIBOLD = "fonts/Montserrat-SemiBold.otf";
        monSemibold = Typeface.createFromAsset(context.getAssets(), SEMIBOLD);

        String LIGHT = "fonts/MontserratAlternates-Regular.ttf";
        monAlRegular = Typeface.createFromAsset(context.getAssets(), LIGHT);

        String MEDIUM = "fonts/MontserratAlternates-SemiBold.otf";
        monAlSemi = Typeface.createFromAsset(context.getAssets(), MEDIUM);

        String BLACK = "fonts/Montserrat-ExtraBold.otf";
        monExtra = Typeface.createFromAsset(context.getAssets(), BLACK);

        String THIN = "fonts/Montserrat-ExtraBold.otf";
        monAlSemi = Typeface.createFromAsset(context.getAssets(), THIN);

        String BOLD = "fonts/MontserratAlternates-Bold.otf";
        monBold = Typeface.createFromAsset(context.getAssets(), BOLD);
        String Medium = "fonts/MontserratAlternates-Medium.otf";
        monMedium = Typeface.createFromAsset(context.getAssets(), Medium);




    }
}
