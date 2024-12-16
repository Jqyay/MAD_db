package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "helpdesk",
        foreignKeys = {
            @ForeignKey(
                    entity = Issue.class,
                    parentColumns = "issueId",
                    childColumns = "issueId",
                    onDelete = ForeignKey.CASCADE
            ),
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
                    entity = Comment.class,
                    parentColumns = "commentId",
                    childColumns = "commentId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Quiz.class,
                    parentColumns = "quizId",
                    childColumns = "quizId",
                    onDelete = ForeignKey.CASCADE
            ),
            @ForeignKey(
                    entity = Staff.class,
                    parentColumns = "staffId",
                    childColumns = "staffId"
                    //no action done on deletion of staff
            )
        }
)
public class Helpdesk {
    @PrimaryKey
    @NonNull
    private String helpdeskId;
    @NonNull
    private String issueId;
    @NonNull
    private String userId;
    @Nullable
    private String postId;
    @Nullable
    private String courseId;
    @Nullable
    private String commentId;
    @Nullable
    private String quizId;
    @NonNull
    private String staffId;
    @Nullable
    private String reason;
    @NonNull
    @ColumnInfo(defaultValue = "pending")
    private String helpdeskStatus;

    public Helpdesk() {
    }

    // Constructor
    public Helpdesk(@NonNull String helpdeskId, @NonNull String issueId, @NonNull String userId,
                    @Nullable String postId, @Nullable String courseId, @Nullable String commentId,
                    @Nullable String quizId, @NonNull String staffId, @Nullable String reason,
                    @NonNull String helpdeskStatus) {
        this.helpdeskId = helpdeskId;
        this.issueId = issueId;
        this.userId = userId;
        this.postId = postId;
        this.courseId = courseId;
        this.commentId = commentId;
        this.quizId = quizId;
        this.staffId = staffId;
        this.reason = reason;
        this.helpdeskStatus = helpdeskStatus;
    }

    // Getters and Setters
    public String getHelpdeskId() {
        return helpdeskId;
    }

    public void setHelpdeskId(String helpdeskId) {
        this.helpdeskId = helpdeskId;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getHelpdeskStatus() {
        return helpdeskStatus;
    }

    public void setHelpdeskStatus(String helpdeskStatus) {
        this.helpdeskStatus = helpdeskStatus;
    }
}
