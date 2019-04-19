package com.hobbyer.android.api.response.auth.pastprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PastUserViewContentList {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booked_user_id")
    @Expose
    private String bookedUserId;
    @SerializedName("class_schedule_id")
    @Expose
    private Integer classScheduleId;
    @SerializedName("reservation_id")
    @Expose
    private String reservationId;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("booking_start_time")
    @Expose
    private String bookingStartTime;
    @SerializedName("booking_end_time")
    @Expose
    private String bookingEndTime;
    @SerializedName("reservation_status")
    @Expose
    private String reservationStatus;
    @SerializedName("studio_id")
    @Expose
    private Integer studioId;
    @SerializedName("paid_points")
    @Expose
    private Integer paidPoints;
    @SerializedName("booked_count")
    @Expose
    private Integer bookedCount;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("complete_status")
    @Expose
    private Integer completeStatus;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("class_name")
    @Expose
    private String className;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("week_days")
    @Expose
    private Object weekDays;
    @SerializedName("instructor_name")
    @Expose
    private String instructorName;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("points")
    @Expose
    private Object points;
    @SerializedName("seats")
    @Expose
    private Integer seats;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("how_to_prepare")
    @Expose
    private String howToPrepare;
    @SerializedName("class_image")
    @Expose
    private Object classImage;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("studio_name")
    @Expose
    private String studioName;
    @SerializedName("facebook_url")
    @Expose
    private Object facebookUrl;
    @SerializedName("twitter_link")
    @Expose
    private Object twitterLink;
    @SerializedName("insta_link")
    @Expose
    private Object instaLink;
    @SerializedName("youtube_link")
    @Expose
    private Object youtubeLink;
    @SerializedName("studio_image")
    @Expose
    private String studioImage;
    @SerializedName("language")
    @Expose
    private Object language;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("postal_code")
    @Expose
    private Integer postalCode;
    @SerializedName("website_link")
    @Expose
    private Object websiteLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookedUserId() {
        return bookedUserId;
    }

    public void setBookedUserId(String bookedUserId) {
        this.bookedUserId = bookedUserId;
    }

    public Integer getClassScheduleId() {
        return classScheduleId;
    }

    public void setClassScheduleId(Integer classScheduleId) {
        this.classScheduleId = classScheduleId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(String bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public String getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public Integer getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public Integer getPaidPoints() {
        return paidPoints;
    }

    public void setPaidPoints(Integer paidPoints) {
        this.paidPoints = paidPoints;
    }

    public Integer getBookedCount() {
        return bookedCount;
    }

    public void setBookedCount(Integer bookedCount) {
        this.bookedCount = bookedCount;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(Integer completeStatus) {
        this.completeStatus = completeStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Object getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(Object weekDays) {
        this.weekDays = weekDays;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Object getPoints() {
        return points;
    }

    public void setPoints(Object points) {
        this.points = points;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getHowToPrepare() {
        return howToPrepare;
    }

    public void setHowToPrepare(String howToPrepare) {
        this.howToPrepare = howToPrepare;
    }

    public Object getClassImage() {
        return classImage;
    }

    public void setClassImage(Object classImage) {
        this.classImage = classImage;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public Object getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(Object facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Object getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(Object twitterLink) {
        this.twitterLink = twitterLink;
    }

    public Object getInstaLink() {
        return instaLink;
    }

    public void setInstaLink(Object instaLink) {
        this.instaLink = instaLink;
    }

    public Object getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(Object youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getStudioImage() {
        return studioImage;
    }

    public void setStudioImage(String studioImage) {
        this.studioImage = studioImage;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public Object getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(Object websiteLink) {
        this.websiteLink = websiteLink;
    }
}
