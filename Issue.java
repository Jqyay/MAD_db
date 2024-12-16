package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="issue")
public class Issue {
    @PrimaryKey
    @NonNull
    private String issueId;
    @NonNull
    private String type;

    public Issue() {
    }

    // Constructor
    public Issue(@NonNull String issueId, @NonNull String type) {
        this.issueId = issueId;
        this.type = type;
    }

    // Getters and Setters
    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
