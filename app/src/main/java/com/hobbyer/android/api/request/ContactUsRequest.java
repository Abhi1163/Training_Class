package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class ContactUsRequest {

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("email")
    public String email;

    @SerializedName("subject")
    public String subject;

    @SerializedName("description")
    public String description;

    @SerializedName("issue")
    public String issue;

    @SerializedName("help")
    public String help;

    @SerializedName("different_email")
    public String different_email;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getDifferent_email() {
        return different_email;
    }

    public void setDifferent_email(String different_email) {
        this.different_email = different_email;
    }
}
