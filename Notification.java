package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification",
        foreignKeys ={
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE
            )
        }
)
public class Notification {
    @PrimaryKey
    @NonNull
    private String notificationId;
    @NonNull
    private String userId;
    @NonNull
    private String message;
    @Nullable
    private String deliveredTime;
    @Nullable
    private String readTime;

    public Notification() {
    }

    // Constructor
    public Notification(String notificationId, @NonNull String userId, @NonNull String message,
                        @Nullable String deliveredTime, @Nullable String readTime) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.message = message;
        this.deliveredTime = deliveredTime;
        this.readTime = readTime;
    }

    // Getters
    public String getNotificationId() {
        return notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }

    public String getReadTime() {
        return readTime;
    }

    // Setters
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDeliveredTime(String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
