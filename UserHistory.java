package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "userHistory",
        primaryKeys = {"postId", "userId"},
        foreignKeys = {
            @ForeignKey(
                    entity = Post.class,
                    parentColumns = "postId",
                    childColumns = "postId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE
            )
        }
)
public class UserHistory {
    @NonNull
    private String postId;
    @NonNull
    private String userId;
    @ColumnInfo(defaultValue = "1")
    @NonNull
    private int progress;
    @NonNull
    private String time;

    public UserHistory() {
    }

    // Constructor
    public UserHistory(String postId, String userId, int progress, @NonNull String time) {
        this.postId = postId;
        this.userId = userId;
        this.progress = progress;
        this.time = time;
    }

    // Getters and Setters

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
