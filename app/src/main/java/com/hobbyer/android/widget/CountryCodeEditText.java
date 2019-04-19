package com.hobbyer.android.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.hobbyer.android.R;
import com.hobbyer.android.model.CountryCodeData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class CountryCodeEditText extends AppCompatEditText {

    private static final String LOG_TAG = "CountryCodeEditText";
    /**
     * JSON files constants
     */
    private static final String JSON_KEY_COUNTRIES = "countries";
    private static final String JSON_KEY_NAME = "fullName";
    private static final String KEY_SHORTNAME = "shortName";
    private static final String JSON_KEY_LOCALE = "locale";
    private static final String JSON_KEY_TELEPHONYCODE = "telephonyCode";
    private static final String COUNTRIES_JSON_FILE = "countries.json";

    private static final String CHARSET_NAME = "UTF-8";
    private static final String EMPTY_TELEPHONY_CODE = "NA";
    private static final String PLUS = "+";

    private AlertDialog countryDialog;
    private List<CountryCodeData> allCountries, displayList;
    private String mCountryTelphonyCode;
    private CountryListAdapter countryListAdapter;
    private OnClickListener cancelListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (countryDialog != null && countryDialog.isShowing()) {
                countryDialog.dismiss();
//                hideKeyboard((Activity) context);
            }
        }
    };
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            displayList.clear();
            displayList.addAll(allCountries);
            countryDialog = initDialog();
            countryDialog.show();
        }
    };

    public CountryCodeEditText(Context context) {
        super(context);
        initializedControl();
    }

//    private void applyCustomFont(Context context, AttributeSet attrs) {
//        TypedArray array = context.getTheme().obtainStyledAttributes(
//                attrs,
//                R.styleable.CustomTextView,
//                0, 0);
//        int typefaceType;
//        try {
//            typefaceType = array.getInteger(R.styleable.CustomTextView_font_name, 0);
//        } finally {
//            array.recycle();
//        }
//        if (!isInEditMode()) {
//            setTypeface(setFont(context));
//        }
//
//    }

    public CountryCodeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        applyCustomFont(context, attrs);
        initializedControl();
    }


    public CountryCodeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializedControl();
    }

    public Typeface setFont(Context context) {
        // return Typeface.createFromAsset(context.getAssets(), "fonts/regular.ttf");

        return Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    void initializedControl() {
        this.setFocusableInTouchMode(false);
        allCountries = readMobileDataCountriesJSONFile();
        displayList = new ArrayList<>();
        displayList.addAll(allCountries);
        this.setOnClickListener(clickListener);
        setUserCurrentSimCountry();

        mCountryTelphonyCode = PLUS + allCountries.get(62).getCountryTelephonyCode();
        ;
        CountryCodeEditText.this.setText(mCountryTelphonyCode);
    }

    private AlertDialog initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        countryListAdapter = new CountryListAdapter(this.getContext(), displayList);
        View inflate = LayoutInflater.from(this.getContext()).inflate(R.layout.dialog_country_code_picker, null);
        ((EditText) inflate.findViewById(R.id.et_search)).addTextChangedListener(new textChangeListener(this));
        ((EditText) inflate.findViewById(R.id.et_search)).setHint("Search");
        ((ListView) inflate.findViewById(R.id.county_list)).setAdapter(countryListAdapter);
        inflate.findViewById(R.id.cancel_button).setOnClickListener(cancelListener);
        builder.setView(inflate);
        return builder.create();
    }

    private void setUserCurrentSimCountry() {
        TelephonyManager manager = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String countryTelephonyCode = null;
        if (manager != null && allCountries != null && allCountries.size() > 0) {
            if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                String countryIso = manager.getSimCountryIso().toLowerCase();
                if (!TextUtils.isEmpty(countryIso)) {
                    for (CountryCodeData country : allCountries) {
                        if (countryIso.equals(country.getLocale().toLowerCase())) {
                            countryTelephonyCode = country.getCountryTelephonyCode();
                            break;
                        }
                    }
                }
            }
        }

        if (TextUtils.isEmpty(countryTelephonyCode)) {
            countryTelephonyCode = PLUS + allCountries.get(62).getCountryTelephonyCode();
        } else {
            countryTelephonyCode = PLUS + countryTelephonyCode;
        }

        mCountryTelphonyCode = countryTelephonyCode;
        CountryCodeEditText.this.setText(mCountryTelphonyCode);
    }

    private List<CountryCodeData> readMobileDataCountriesJSONFile() {
        List<CountryCodeData> countryList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(loadJSONFromAsset()).getJSONArray(JSON_KEY_COUNTRIES);
            int i = 0;
            while (jSONArray != null && i < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                countryList.add(new CountryCodeData(jSONObject.getString(JSON_KEY_NAME), jSONObject.getString(KEY_SHORTNAME), jSONObject.getString(JSON_KEY_LOCALE), jSONObject.getString(JSON_KEY_TELEPHONYCODE)));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
//            LogUtils.LOGE(LOG_TAG, "Error parsing Countries.json");
        }
        Log.i(LOG_TAG, " " + Arrays.toString(countryList.toArray()));
        return countryList;
    }

    private String loadJSONFromAsset() {
        try {
            InputStream open = CountryCodeEditText.this.getContext().getAssets().open(COUNTRIES_JSON_FILE);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, CHARSET_NAME);
        } catch (IOException e) {
            return null;
        }
    }

    private class textChangeListener implements TextWatcher {
        final CountryCodeEditText editText;

        textChangeListener(CountryCodeEditText mobileEditText) {
            this.editText = mobileEditText;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void afterTextChanged(Editable editable) {
            if (editable != null) {
                String searchText = editable.toString();
                displayList.clear();
                if (!searchText.isEmpty() /*|| this.editText.allCountries == null*/) {
                    CharSequence toLowerCase = searchText.toLowerCase();
                    if (allCountries != null) {
                        for (CountryCodeData mobileDataCountry : allCountries) {
                            if (mobileDataCountry.getCountryFullName().toLowerCase().contains(toLowerCase) || mobileDataCountry.getCountryShortName().toLowerCase().contains(toLowerCase)) {
                                displayList.add(mobileDataCountry);
                            }
                        }
                    }
                } else {
                    this.editText.displayList.addAll(allCountries);
                }
                this.editText.countryListAdapter.notifyDataSetChanged();
            }
        }
    }

    private class CountryListAdapter extends BaseAdapter {
        private final List<CountryCodeData> displayList;
        private Context context;

        public CountryListAdapter(Context context, List<CountryCodeData> countries) {
            this.context = context;
            this.displayList = countries;
        }

        public int getCount() {
            return this.displayList.size();
        }

        public CountryCodeData getItem(int i) {
            return this.displayList.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.row_country_code, null);
            }
            CountryCodeData item = getItem(i);
            ((TextView) view.findViewById(R.id.tvName)).setText(item.getCountryFullName() + " (" + item.getCountryShortName() + ")");
            TextView textView = view.findViewById(R.id.tvCountryCode);
            String countryTelephonyCode = item.getCountryTelephonyCode();
            textView.setVisibility(View.VISIBLE);
            textView.setText(PLUS + countryTelephonyCode);
            view.setOnClickListener(view1 -> {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view1.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                if (countryDialog != null && countryDialog.isShowing()) {
                    countryDialog.dismiss();
                }
                CountryCodeEditText.this.setText(PLUS + countryTelephonyCode);
            });
            return view;
        }


    }
}
