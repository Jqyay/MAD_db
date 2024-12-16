package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "comment",
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "userId",
                    onDelete = ForeignKey.SET_NULL
                    //shown as "user no longer exists" upon deletion
            ),
            @ForeignKey(
                    entity = Post.class,
                    parentColumns = "postId",
                    childColumns = "postId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Comment.class,
                    parentColumns = "commentId",
                    childColumns = "rootComment",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "replyUserId",
                    onDelete = ForeignKey.SET_NULL
                    //shown as "user no longer exists" upon deletion
            )
        }
)
public class Comment {
    @PrimaryKey
    @NonNull
    private String commentId;
    @Nullable
    private String userId;
    @NonNull
    private String postId;
    @NonNull
    private String comment;
    @Nullable
    private String rootComment;
    @Nullable
    private String replyUserId;
    @NonNull
    @ColumnInfo(defaultValue = "0")
    private int isRead;
    @NonNull
    private String timestamp;

    // Constructor
    public Comment(@NonNull String commentId, @Nullable String userId, @NonNull String postId,
                   @NonNull String comment, @Nullable String rootComment, @Nullable String replyUserId,
                   int isRead, @NonNull String timestamp) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.comment = comment;
        this.rootComment = rootComment;
        this.replyUserId = replyUserId;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }
    public Comment() {
    }

    // Getter and Setter for commentId
    @NonNull
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(@NonNull String commentId) {
        this.commentId = commentId;
    }

    // Getter and Setter for userId
    @Nullable
    public String getUserId() {
        return userId;
    }

    public void setUserId(@Nullable String userId) {
        this.userId = userId;
    }

    // Getter and Setter for postId
    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    // Getter and Setter for comment text
    @NonNull
    public String getComment() {
        return comment;
    }

    public void setComment(@NonNull String comment) {
        this.comment = comment;
    }

    // Getter and Setter for rootComment
    @Nullable
    public String getRootComment() {
        return rootComment;
    }

    public void setRootComment(@Nullable String rootComment) {
        this.rootComment = rootComment;
    }

    // Getter and Setter for replyUserId
    @Nullable
    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(@Nullable String replyUserId) {
        this.replyUserId = replyUserId;
    }

    // Getter and Setter for isRead
    public int isRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    // Getter and Setter for timestamp
    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull String timestamp) {
        this.timestamp = timestamp;
    }
}
