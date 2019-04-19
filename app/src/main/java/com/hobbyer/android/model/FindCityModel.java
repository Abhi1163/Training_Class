package com.hobbyer.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FindCityModel implements Parcelable {
    private double latitude;
    private double longitude;
    private String city;
    private String country;


    public FindCityModel() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Creator<FindCityModel> getCREATOR() {
        return CREATOR;
    }

    protected FindCityModel(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        city = in.readString();
        country = in.readString();
    }

    public static final Creator<FindCityModel> CREATOR = new Creator<FindCityModel>() {
        @Override
        public FindCityModel createFromParcel(Parcel in) {
            return new FindCityModel(in);
        }

        @Override
        public FindCityModel[] newArray(int size) {
            return new FindCityModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(city);
        dest.writeString(country);
    }
}
