package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "achievement",
        primaryKeys = {"userId", "badgeId"},
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Badge.class,
                    parentColumns = "badgeId",
                    childColumns = "badgeId",
                    onDelete = ForeignKey.CASCADE
            )
        }
)
public class Achievement {
    @NonNull
    private String userId;
    @NonNull
    private String badgeId;

    // No-argument constructor
    public Achievement() {
        // This is required for Firestore to deserialize the object.
    }
    // Constructor
    public Achievement(@NonNull String userId, @NonNull String badgeId) {
        this.userId = userId;
        this.badgeId = badgeId;
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter for badgeId
    public String getBadgeId() {
        return badgeId;
    }

    // Setter for badgeId
    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }
}
