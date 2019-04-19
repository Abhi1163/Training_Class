package com.hobbyer.android.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserFaceBookModel implements Parcelable {

    public String userName;
    public String userEmail;
    public String profilePic;
    public String gender;

    public static final Parcelable.Creator<UserFaceBookModel> CREATOR = new Parcelable.Creator<UserFaceBookModel>() {

        @Override
        public UserFaceBookModel createFromParcel(Parcel parcel) {
            return new UserFaceBookModel(parcel);
        }

        @Override
        public UserFaceBookModel[] newArray(int size) {
            return new UserFaceBookModel[size];
        }
    };

    public UserFaceBookModel() {

    }

    private UserFaceBookModel(Parcel parcel) {
        userName = parcel.readString();
        userEmail = parcel.readString();
        profilePic = parcel.readString();
        gender = parcel.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(userName);
        parcel.writeString(userEmail);
        parcel.writeString(profilePic);
        parcel.writeString(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
