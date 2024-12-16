package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "mediaRead",
        primaryKeys = {"mediaId", "postId", "userId"},
        foreignKeys = {
            @ForeignKey(
                    entity = Media.class,
                    parentColumns = "mediaId",
                    childColumns = "mediaId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Post.class,
                    parentColumns= "postId",
                    childColumns = "postId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.CASCADE
            )
        }
)
public class MediaRead {
    @NonNull
    private String userId;
    @NonNull
    private String postId;
    @NonNull
    private String mediaId;

    public MediaRead() {
    }

    // Constructor
    public MediaRead(String userId, String postId, String mediaId) {
        this.userId = userId;
        this.postId = postId;
        this.mediaId = mediaId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
