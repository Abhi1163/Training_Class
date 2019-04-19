package com.hobbyer.android.api.response.auth.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("business_name")
    @Expose
    private Object businessName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("web_url")
    @Expose
    private Object webUrl;
    @SerializedName("phone_code")
    @Expose
    private VerifyPhoneCode phoneCode;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("business_open")
    @Expose
    private Integer businessOpen;
    @SerializedName("role_id")
    @Expose
    private Integer roleId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("hear_about_us")
    @Expose
    private Object hearAboutUs;
    @SerializedName("social_type")
    @Expose
    private Object socialType;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("term")
    @Expose
    private Integer term;
    @SerializedName("alternate_phone")
    @Expose
    private Object alternatePhone;
    @SerializedName("street")
    @Expose
    private Object street;
    @SerializedName("apartment")
    @Expose
    private Object apartment;
    @SerializedName("postal_code")
    @Expose
    private Object postalCode;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("dob")
    @Expose
    private Object dob;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("emergency_contact_name")
    @Expose
    private Object emergencyContactName;
    @SerializedName("emergency_contact_phone")
    @Expose
    private Object emergencyContactPhone;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("refer_earn")
    @Expose
    private String referEarn;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getBusinessName() {
        return businessName;
    }

    public void setBusinessName(Object businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(Object webUrl) {
        this.webUrl = webUrl;
    }

    public VerifyPhoneCode getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(VerifyPhoneCode phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getBusinessOpen() {
        return businessOpen;
    }

    public void setBusinessOpen(Integer businessOpen) {
        this.businessOpen = businessOpen;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getHearAboutUs() {
        return hearAboutUs;
    }

    public void setHearAboutUs(Object hearAboutUs) {
        this.hearAboutUs = hearAboutUs;
    }

    public Object getSocialType() {
        return socialType;
    }

    public void setSocialType(Object socialType) {
        this.socialType = socialType;
    }

    public Object getSocialId() {
        return socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Object getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(Object alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getApartment() {
        return apartment;
    }

    public void setApartment(Object apartment) {
        this.apartment = apartment;
    }

    public Object getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Object postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Object getDob() {
        return dob;
    }

    public void setDob(Object dob) {
        this.dob = dob;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(Object emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public Object getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(Object emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReferEarn() {
        return referEarn;
    }

    public void setReferEarn(String referEarn) {
        this.referEarn = referEarn;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
