package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="counselorAvailability",
        foreignKeys = {
            @ForeignKey(
                    entity= Counselor.class,
                    parentColumns = "counselorId",
                    childColumns = "counselorId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity =Timeslot.class,
                    parentColumns = "timeslotId",
                    childColumns = "timeslotId",
                    onDelete = ForeignKey.RESTRICT
            )
        }
)
public class CounselorAvailability {
    @PrimaryKey
    @NonNull
    private String counselorAvailabilityId;

    @NonNull
    private String counselorId;
    @NonNull
    private String timeslotId;

    @NonNull
    private String date;
    @ColumnInfo(defaultValue = "0")
    @NonNull
    private int isBooked;

    // Constructor
    public CounselorAvailability(@NonNull String counselorAvailabilityId, @NonNull String counselorId,
                                 @NonNull String timeslotId, @NonNull String date, int isBooked) {
        this.counselorAvailabilityId = counselorAvailabilityId;
        this.counselorId = counselorId;
        this.timeslotId = timeslotId;
        this.date = date;
        this.isBooked = isBooked;
    }

    public CounselorAvailability() {
    }

    // Getter and Setter for counselorAvailabilityId
    @NonNull
    public String getCounselorAvailabilityId() {
        return counselorAvailabilityId;
    }

    public void setCounselorAvailabilityId(@NonNull String counselorAvailabilityId) {
        this.counselorAvailabilityId = counselorAvailabilityId;
    }

    // Getter and Setter for counselorId
    @NonNull
    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(@NonNull String counselorId) {
        this.counselorId = counselorId;
    }

    // Getter and Setter for timeslotId
    @NonNull
    public String getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(@NonNull String timeslotId) {
        this.timeslotId = timeslotId;
    }

    // Getter and Setter for date
    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    // Getter and Setter for isBooked
    public int isBooked() {
        return isBooked;
    }

    public void setIsBooked(int isBooked) {
        this.isBooked = isBooked;
    }
}
