package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "badge",
        indices = {
        @Index(value= {"badgeName"}, unique = true),
        @Index(value= {"badgeImage"}, unique = true)
        }
        )
public class Badge {
    @PrimaryKey
    @NonNull
    private String badgeId;
    @NonNull
    private String badgeName;
    @NonNull
    private String badgeImage;

    public Badge() {
    }

    // Constructor
    public Badge(@NonNull String badgeId, @NonNull String badgeName, @NonNull String badgeImage) {
        this.badgeId = badgeId;
        this.badgeName = badgeName;
        this.badgeImage = badgeImage;
    }

    // Getter for badgeId
    @NonNull
    public String getBadgeId() {
        return badgeId;
    }

    // Setter for badgeId
    public void setBadgeId(@NonNull String badgeId) {
        this.badgeId = badgeId;
    }

    // Getter for badgeName
    @NonNull
    public String getBadgeName() {
        return badgeName;
    }

    // Setter for badgeName
    public void setBadgeName(@NonNull String badgeName) {
        this.badgeName = badgeName;
    }

    // Getter for badgeImage
    @NonNull
    public String getBadgeImage() {
        return badgeImage;
    }

    // Setter for badgeImage
    public void setBadgeImage(@NonNull String badgeImage) {
        this.badgeImage = badgeImage;
    }
}
