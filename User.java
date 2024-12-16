package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "user",
        indices = {
            @Index(value = {"email"}, unique = true),
            @Index(value = {"phoneNo"}, unique = true)
        }
        )
public class User {
    @PrimaryKey
    @NonNull
    private String userId;

    @NonNull
    @ColumnInfo(defaultValue= "bookworm")
    private String name;

    @NonNull
    private String email;

    @Nullable
    private String phoneNo;

    @NonNull
    private String password;

    @NonNull
    @ColumnInfo(defaultValue = "url link of default profile pic")
    private String profilePic;

    @NonNull
    private String lastLogin;

    @NonNull
    private int strikeLoginDays;

    public User() {
    }

    // Constructor
    public User(@NonNull String userId, @NonNull String name, @NonNull String email,
                @Nullable String phoneNo, @NonNull String password,
                @NonNull String profilePic, @NonNull String lastLogin,
                int strikeLoginDays) {
        this.userId = userId;//user "TEMP" for all users first
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.password = password;
        this.profilePic = profilePic;
        this.lastLogin = lastLogin;
        this.strikeLoginDays = strikeLoginDays;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getStrikeLoginDays() {
        return strikeLoginDays;
    }

    public void setStrikeLoginDays(int strikeLoginDays) {
        this.strikeLoginDays = strikeLoginDays;
    }
}
