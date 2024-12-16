package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mediaSet")
public class MediaSet {
    @PrimaryKey
    @NonNull
    private String mediaSetId; // A unique ID for the set of media.

    // Constructor
    public MediaSet(@NonNull String mediaSetId) {
        this.mediaSetId = mediaSetId;
    }

    public MediaSet() {
    }

    // Getter and Setter
    public String getMediaSetId() {
        return mediaSetId;
    }

    public void setMediaSetId(String mediaSetId) {
        this.mediaSetId = mediaSetId;
    }
}