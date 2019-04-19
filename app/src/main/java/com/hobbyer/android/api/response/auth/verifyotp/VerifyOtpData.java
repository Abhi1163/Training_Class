package com.hobbyer.android.api.response.auth.verifyotp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.response.auth.UserModel;

public class VerifyOtpData {


            @SerializedName("user")
            @Expose
            private User user;

            public User getUser() {
                return user;
            }

            public void setUser(User user) {
                this.user = user;
            }






    }
   /* private UserModel user;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }*/



