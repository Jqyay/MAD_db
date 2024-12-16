package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "counselor",
        indices = {
            @Index(value = {"email"}, unique = true),
            @Index(value = {"contactNo"}, unique = true)
        }
)
public class Counselor {
    @PrimaryKey
    @NonNull
    private String counselorId;

    @NonNull
    private String name;
    @NonNull
    private String office;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    @ColumnInfo(defaultValue = "url link of the default profilepic")
    private String profilePic;
    @NonNull
    private String contactNo;
    // Constructor
    public Counselor(@NonNull String counselorId, @NonNull String name, @NonNull String office,
                     @NonNull String email, @NonNull String password, @NonNull String profilePic,
                     @NonNull String contactNo) {
        this.counselorId = counselorId;
        this.name = name;
        this.office = office;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.contactNo = contactNo;
    }
    public Counselor() {
    }

    // Getter and Setter for counselorId
    @NonNull
    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(@NonNull String counselorId) {
        this.counselorId = counselorId;
    }

    // Getter and Setter for name
    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    // Getter and Setter for office
    @NonNull
    public String getOffice() {
        return office;
    }

    public void setOffice(@NonNull String office) {
        this.office = office;
    }

    // Getter and Setter for email
    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    // Getter and Setter for password
    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    // Getter and Setter for profilePic
    @NonNull
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(@NonNull String profilePic) {
        this.profilePic = profilePic;
    }



    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
