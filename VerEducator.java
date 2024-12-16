package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "verEducator",
        primaryKeys = {"userId", "domainId"},
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Domain.class,
                        parentColumns = "domainId",
                        childColumns = "domainId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Staff.class,
                        parentColumns = "staffId",
                        childColumns = "staffId"
                        //no action done upon the deletion of staff
                ),
                @ForeignKey(
                        entity = MediaSet.class,
                        parentColumns = "mediaSetId",
                        childColumns = "imageSetId",
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = MediaSet.class,
                        parentColumns = "mediaSetId",
                        childColumns = "fileSetId",
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
            @Index(value = {"imageSetId"}, unique = true),
            @Index(value = {"fileSetId"}, unique = true)
        }
        )
public class VerEducator {
    @NonNull
    private String userId;
    @Nullable
    private String imageSetId;
    @Nullable
    private String fileSetId;
    @NonNull
    private String domainId;
    @NonNull
    private String staffId;
    @NonNull
    @ColumnInfo(defaultValue = "pending")
    private String verifiedStatus;

    @NonNull
    public String timestamp;
    public VerEducator() {
    }

    // Constructor

    public VerEducator(@NonNull String userId, @Nullable String imageSetId,
                       @Nullable String fileSetId, @NonNull String domainId,
                       @NonNull String staffId, @NonNull String verifiedStatus,
                       @NonNull String timestamp) {
        this.userId = userId;
        this.imageSetId = imageSetId;
        this.fileSetId = fileSetId;
        this.domainId = domainId;
        this.staffId = staffId;
        this.verifiedStatus = verifiedStatus;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Nullable
    public String getImageSetId() {
        return imageSetId;
    }

    public void setImageSetId(@Nullable String imageSetId) {
        this.imageSetId = imageSetId;
    }

    @Nullable
    public String getFileSetId() {
        return fileSetId;
    }

    public void setFileSetId(@Nullable String fileSetId) {
        this.fileSetId = fileSetId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @NonNull
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(@NonNull String staffId) {
        this.staffId = staffId;
    }

    @NonNull
    public String getVerifiedStatus() {
        return verifiedStatus;
    }

    public void setVerifiedStatus(@NonNull String verifiedStatus) {
        this.verifiedStatus = verifiedStatus;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }
}
