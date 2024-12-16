package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "domainInterested",
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
                )
        }
)
public class DomainInterested {
    @NonNull
    private String userId;

    @NonNull
    private String domainId;

    public DomainInterested() {
    }

    // Constructor
    public DomainInterested(String userId, String domainId) {
        this.userId = userId;
        this.domainId = domainId;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for domainId
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}
