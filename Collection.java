package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "collection",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "postId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Course.class,
                        parentColumns = "courseId",
                        childColumns = "courseId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Folder.class,
                        parentColumns = "folderId",
                        childColumns = "folderId",
                        onDelete = ForeignKey.SET_NULL
                )
        }
)
public class Collection {
    @PrimaryKey
    @NonNull
    private String collectionId;
    @NonNull
    private String userId;
    @Nullable
    private String postId;
    @Nullable
    private String courseId;
    @Nullable
    private String folderId;

    // Constructor
    public Collection(@NonNull String collectionId, @NonNull String userId, @Nullable String postId,
                      @Nullable String courseId, @Nullable String folderId) {
        this.collectionId = collectionId;
        this.userId = userId;
        this.postId = postId;
        this.courseId = courseId;
        this.folderId = folderId;
    }

    public Collection() {
    }
    // Getter and Setter for collectionId
    @NonNull
    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(@NonNull String collectionId) {
        this.collectionId = collectionId;
    }

    // Getter and Setter for userId
    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    // Getter and Setter for postId
    @Nullable
    public String getPostId() {
        return postId;
    }

    public void setPostId(@Nullable String postId) {
        this.postId = postId;
    }

    // Getter and Setter for courseId
    @Nullable
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(@Nullable String courseId) {
        this.courseId = courseId;
    }

    // Getter and Setter for folderId
    @Nullable
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(@Nullable String folderId) {
        this.folderId = folderId;
    }
}
