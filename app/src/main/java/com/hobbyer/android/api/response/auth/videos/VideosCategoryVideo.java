package com.hobbyer.android.api.response.auth.videos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VideosCategoryVideo  implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("class_id")
    @Expose
    private String classId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("studio_id")
    @Expose
    private Integer studioId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("video_name")
    @Expose
    private String videoName;

    @SerializedName("video_url")
    @Expose
    private String videoUrl;





    @SerializedName("instructor")
    @Expose
    private String instructor;

    @SerializedName("equipment_needed")
    @Expose
    private String equipment_needed;

    @SerializedName("body_focus")
    @Expose
    private String body_focus;

    @SerializedName("how_to_prepare")
    @Expose
    private String how_to_prepare;


    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("liked_status")
    @Expose
    private String liked_status;

    public VideosCategoryVideo() {

    }


    protected VideosCategoryVideo(Parcel in) {
        id = in.readString();
        classId = in.readString();
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readInt();
        }
        if (in.readByte() == 0) {
            studioId = null;
        } else {
            studioId = in.readInt();
        }
        description = in.readString();
        videoName = in.readString();
        videoUrl = in.readString();
        instructor = in.readString();
        equipment_needed = in.readString();
        body_focus = in.readString();
        how_to_prepare = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        liked_status = in.readString();
    }

    public static final Creator<VideosCategoryVideo> CREATOR = new Creator<VideosCategoryVideo>() {
        @Override
        public VideosCategoryVideo createFromParcel(Parcel in) {
            return new VideosCategoryVideo(in);
        }

        @Override
        public VideosCategoryVideo[] newArray(int size) {
            return new VideosCategoryVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStudioId() {
        return studioId;
    }

    public void setStudioId(Integer studioId) {
        this.studioId = studioId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getEquipment_needed() {
        return equipment_needed;
    }

    public void setEquipment_needed(String equipment_needed) {
        this.equipment_needed = equipment_needed;
    }

    public String getBody_focus() {
        return body_focus;
    }

    public void setBody_focus(String body_focus) {
        this.body_focus = body_focus;
    }

    public String getHow_to_prepare() {
        return how_to_prepare;
    }

    public void setHow_to_prepare(String how_to_prepare) {
        this.how_to_prepare = how_to_prepare;
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

    public String getLiked_status() {
        return liked_status;
    }

    public void setLiked_status(String liked_status) {
        this.liked_status = liked_status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(classId);
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(categoryId);
        }
        if (studioId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(studioId);
        }
        dest.writeString(description);
        dest.writeString(videoName);
        dest.writeString(videoUrl);
        dest.writeString(instructor);
        dest.writeString(equipment_needed);
        dest.writeString(body_focus);
        dest.writeString(how_to_prepare);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(liked_status);
    }
}
