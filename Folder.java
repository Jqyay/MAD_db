package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "folder",
        foreignKeys ={
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Folder.class,
                    parentColumns = "folderId",
                    childColumns = "rootFolder",
                    onDelete = ForeignKey.SET_NULL
            )
        }
)
public class Folder {
    @PrimaryKey
    @NonNull
    private String folderId;
    @NonNull
    private String userId;
    @NonNull
    private String name;
    @Nullable
    private String rootFolder;

    public Folder() {
    }

    // Constructor
    public Folder(@NonNull String folderId, @NonNull String userId, @NonNull String name, @Nullable String rootFolder) {
        this.folderId = folderId;
        this.userId = userId;
        this.name = name;
        this.rootFolder = rootFolder;
    }

    // Getter and Setter for folderId
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for rootFolder
    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }
}
