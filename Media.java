package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "media",
        foreignKeys = @ForeignKey(
                entity = MediaSet.class,
                parentColumns = "mediaSetId",
                childColumns = "mediaSetId",
                onDelete = ForeignKey.CASCADE
        ))

//for the same uploading action, same mediaSetId,
public class Media {
    @PrimaryKey
    @NonNull
    private String mediaId;
    @NonNull
    private String mediaSetId;

    @NonNull
    private String url;

    public Media() {
    }

    // Constructor
    public Media(@NonNull String mediaId, @NonNull String mediaSetId, @NonNull String url) {
        this.mediaId = mediaId;
        this.mediaSetId = mediaSetId;
        this.url = url;
    }

    // Getters and Setters
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(String mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
