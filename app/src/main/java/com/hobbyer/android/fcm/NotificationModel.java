package com.hobbyer.android.fcm;

public class NotificationModel {

    public NotificationMessageModel message_information;

    public String message;
    private String notificationCount;
    private String notificationType;

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationMessageModel getMessage_information() {
        return message_information;
    }

    public void setMessage_information(NotificationMessageModel message_information) {
        this.message_information = message_information;
    }

    public NotificationModel(String notificationCount) {
        this.notificationCount = notificationCount;

    }

    public class NotificationMessageModel {
        public String dateTime;
        public String quickblox_id;
        public String receiver_id;
        public String type;
        public String user_id;
        public String profile_image;
    }
}
