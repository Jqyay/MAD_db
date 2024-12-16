package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "verPost",
        foreignKeys = {
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "postId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Staff.class,
                        parentColumns = "staffId",
                        childColumns = "staffId"
                        //no action done upon deletion of staff
                )
        },
        indices = {
                @Index(value = {"postId"}, unique = true)
        }
)
public class VerPost {

    @PrimaryKey
    @NonNull
    private String verPostId;
    @NonNull
    private String postId;
    @Nullable
    private String staffId;
    @NonNull
    @ColumnInfo(defaultValue = "pending")
    private String verifiedStatus;

    @NonNull
    private String timestamp;

    public VerPost() {
    }

    // Constructor
    public VerPost(@NonNull String verPostId, @NonNull String postId,
                   @NonNull String staffId, @NonNull String verifiedStatus,
                   @NonNull String timestamp) {
        this.verPostId = verPostId;
        this.postId = postId;
        this.staffId = staffId;
        this.verifiedStatus = verifiedStatus;
        this.timestamp = timestamp;
    }

    // Getter and Setter for verPostId
    public String getVerPostId() {
        return verPostId;
    }

    public void setVerPostId(String verPostId) {
        this.verPostId = verPostId;
    }

    // Getter and Setter for postId
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    // Getter and Setter for staffId
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    // Getter and Setter for verifiedStatus
    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }
}
