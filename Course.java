package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "course",
        foreignKeys = {
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "post1",
                        onDelete = ForeignKey.SET_NULL
                        //shown as "empty post" upon deletion
                ),
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "post2",
                        onDelete = ForeignKey.SET_NULL
                        //shown as "empty post" upon deletion
                ),
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "post3",
                        onDelete = ForeignKey.SET_NULL
                        //shown as "empty post" upon deletion
                ),
                @ForeignKey(
                        entity = Folder.class,
                        parentColumns = "folderId",
                        childColumns = "folderId",
                        onDelete = ForeignKey.SET_NULL
                )
        }
)
public class Course {
    @PrimaryKey
    @NonNull
    private String courseId;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String coverImage;
    @Nullable
    private String post1;
    @Nullable
    private String post2;
    @Nullable
    private String post3;
    @Nullable
    private String folderId;
    @NonNull
    private String date;

    public Course() {
    }

    // Constructor
    public Course(@NonNull String courseId, @NonNull String title, @NonNull String description,
                  @NonNull String coverImage, @Nullable String post1, @Nullable String post2,
                  @Nullable String post3, @Nullable String folderId, @NonNull String date) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.post1 = post1;
        this.post2 = post2;
        this.post3 = post3;
        this.folderId = folderId;
        this.date = date;
    }

    // Getter and Setter for courseId
    @NonNull
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(@NonNull String courseId) {
        this.courseId = courseId;
    }

    // Getter and Setter for title
    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    // Getter and Setter for description
    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    // Getter and Setter for coverImage
    @NonNull
    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(@NonNull String coverImage) {
        this.coverImage = coverImage;
    }

    // Getter and Setter for post1
    @Nullable
    public String getPost1() {
        return post1;
    }

    public void setPost1(@Nullable String post1) {
        this.post1 = post1;
    }

    // Getter and Setter for post2
    @Nullable
    public String getPost2() {
        return post2;
    }

    public void setPost2(@Nullable String post2) {
        this.post2 = post2;
    }

    // Getter and Setter for post3
    @Nullable
    public String getPost3() {
        return post3;
    }

    public void setPost3(@Nullable String post3) {
        this.post3 = post3;
    }

    // Getter and Setter for folderId
    @Nullable
    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(@Nullable String folderId) {
        this.folderId = folderId;
    }

    // Getter and Setter for date
    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }
}
