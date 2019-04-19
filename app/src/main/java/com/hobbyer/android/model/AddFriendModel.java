package com.hobbyer.android.model;

public class AddFriendModel {
    private  String ProfileName;
    private String CountryName;
    private String AddButton;
    private int ProfileImage;

    public String getProfileName() {
        return ProfileName;
    }

    public void setProfileName(String profileName) {
        ProfileName = profileName;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public String getAddButton() {
        return AddButton;
    }

    public void setAddButton(String addButton) {
        AddButton = addButton;
    }

    public int getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(int profileImage) {
        ProfileImage = profileImage;
    }
}
