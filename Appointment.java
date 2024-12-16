package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "appointment",
        primaryKeys = {"counselorAvailabilityId", "userId"},
        foreignKeys = {
            @ForeignKey(
                    entity= CounselorAvailability.class,
                    parentColumns = "counselorAvailabilityId",
                    childColumns = "counselorAvailabilityId",
                    onDelete = ForeignKey.RESTRICT
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete= ForeignKey.CASCADE
            )
        },
        indices = {@Index(value ={"counselorAvailabilityId"}, unique = true)}
)
public class Appointment {
    @NonNull
    private String counselorAvailabilityId;
    @NonNull
    private String userId;

    @NonNull
    @ColumnInfo(defaultValue = "1")
    private int isOnline;

    // Constructor
    public Appointment(@NonNull String counselorAvailabilityId, @NonNull String userId, int isOnline) {
        this.counselorAvailabilityId = counselorAvailabilityId;
        this.userId = userId;
        this.isOnline = isOnline;
    }

    public Appointment() {
    }

    // Getter for counselorAvailabilityId
    @NonNull
    public String getCounselorAvailabilityId() {
        return counselorAvailabilityId;
    }

    // Setter for counselorAvailabilityId
    public void setCounselorAvailabilityId(@NonNull String counselorAvailabilityId) {
        this.counselorAvailabilityId = counselorAvailabilityId;
    }

    // Getter for userId
    @NonNull
    public String getUserId() {
        return userId;
    }

    // Setter for userId
    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    // Getter for isOnline
    public int isOnline() {
        return isOnline;
    }

    // Setter for isOnline
    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }
}
