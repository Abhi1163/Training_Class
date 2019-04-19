package com.hobbyer.android.view.activities.filter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityLocationBinding;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.activities.faq.ActivityFAQ;
import com.hobbyer.android.viewmodel.activity.filterviewmodel.FilterViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity implements TextWatcher {
    private Handler mhandler;
    private Context context;
    private List<Address> listAddresses;
    private String Adress;
    private Double latitude=0.0;
    private Double longitude=0.0;
    private ActivityLocationBinding binding;
    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Geocoder g = new Geocoder(context);
            try {
                listAddresses = g.getFromLocationName(binding.tvplacename.getText().toString(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Address address = listAddresses.get(0);
                Adress = listAddresses.get(0).getAddressLine(0);
                if(address.hasLatitude()&&address.hasLongitude()){
                longitude=address.getLongitude();
                 latitude=address.getLatitude();
                 String tst= address.getAddressLine(0);
                 binding.tvplacename.setText(tst);
                    String text = binding.tvplacename.getText().toString();

                    Intent intent = new Intent();
                    intent.putExtra("address", text);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("latitude", latitude);

                    setResult(RESULT_OK, intent);


                   /* Bundle bundle = new Bundle();
                    bundle.putString("Address", text);
                    bundle.putDouble("longitude",longitude);
                    bundle.putDouble("latitude",latitude);
                    Intent intent = new Intent(LocationActivity.this, FilterActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);*/
                    finish();


                 }
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);




            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        context = LocationActivity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(LocationActivity.this, context.getResources().getColor(R.color.colorPrimary));
        }
        binding.tvplacename.addTextChangedListener(this);


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String text = binding.tvplacename.getText().toString();

        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new PlaceAsyncTask(text).execute();


            }
        }, 500);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
  /*      String text = binding.tvplacename.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("Address", text);
        bundle.putDouble("longitude",longitude);
        bundle.putDouble("latitude",latitude);
        Intent intent = new Intent(LocationActivity.this, FilterActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);*/


    }

    private class PlaceAsyncTask extends AsyncTask<Void, Void, StringBuilder> {
        private static final String API_KEY = "AIzaSyB_gJo-P9RK9USnIQWsDjwyke4AtURb_fA";
        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";
        private String text;
        private HttpURLConnection conn;
        private StringBuilder jsonresult;
        private ArrayList<String> arrayList = new ArrayList<>();

        public PlaceAsyncTask(String text) {
            this.text = text;
        }


        @Override
        protected StringBuilder doInBackground(Void... voids) {

            try {
                jsonresult = new StringBuilder();
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?input=" + URLEncoder.encode(text, "UTF-8"));
                sb.append("&types=geocode");
                sb.append("&key=" + API_KEY);
                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonresult.append(buff, 0, read);
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

            }
            return jsonresult;
        }

        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {
            super.onPostExecute(stringBuilder);
            if (stringBuilder != null) {
                arrayList.clear();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonresult.toString());
                    JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                    arrayList = new ArrayList<String>(predsJsonArray.length());
                    for (int i = 0; i < predsJsonArray.length(); i++) {
                        arrayList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    }
                    PlacesAutoCompleteAdapter mAdapter = new PlacesAutoCompleteAdapter(context, R.layout.support_simple_spinner_dropdown_item, arrayList);
                    //activity.getBinding().etlocation.setAdapter(mAdapter);
                    binding.tvplacename.setAdapter(mAdapter);
                    binding.tvplacename.setOnItemClickListener(listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class PlacesAutoCompleteAdapter extends ArrayAdapter {

        private final ArrayList<String> list;
        private final Context mContext;
        private final int mResource;

        public PlacesAutoCompleteAdapter(Context context, int resource, ArrayList<String> list) {
            super(context, resource, list);
            mContext = context;
            mResource = resource;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(mResource, parent, false);
            }

            TextView strName = (TextView) convertView;
            strName.setText(list.get(position));

            return convertView;
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Assign the data to the FilterResults
                        filterResults.values = list;
                        filterResults.count = list.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }


    }

}
