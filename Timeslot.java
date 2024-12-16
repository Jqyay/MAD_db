package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName= "timeslot")
public class Timeslot {
    @PrimaryKey
    @NonNull
    private String timeslotId;

    @NonNull
    private int startTime;
    @NonNull
    private int endTime;

    public Timeslot() {
    }

    // Constructor
    public Timeslot(String timeslotId, @NonNull int startTime, @NonNull int endTime) {
        this.timeslotId = timeslotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public String getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(String timeslotId) {
        this.timeslotId = timeslotId;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
