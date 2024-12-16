package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "staff",
        indices = {
            @Index(value = {"name"}, unique = true),
            @Index(value = {"email"}, unique = true)
        }
)
public class Staff {
    @PrimaryKey
    @NonNull
    private String staffId;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    public Staff() {
    }

    // Constructor
    public Staff(@NonNull String staffId, @NonNull String name, @NonNull String email, @NonNull String password) {
        this.staffId = staffId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}