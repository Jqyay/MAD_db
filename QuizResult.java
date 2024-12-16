package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "quizResult",
        foreignKeys = {
                @ForeignKey(
                        entity = QuizQuestion.class,
                        parentColumns = "questionId",
                        childColumns = "questionId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = QuestionOption.class,
                        parentColumns = "choice",
                        childColumns = "userAns",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class QuizResult {
    @PrimaryKey
    @NonNull
    private String quizResultId;

    @NonNull
    private String questionId;

    @NonNull
    private String userId;

    @NonNull
    private String userAns;

    @NonNull
    private String timestamp;

    public QuizResult() {
    }

    // Constructor
    public QuizResult(String quizResultId, @NonNull String questionId, @NonNull String userId, @NonNull String userAns, @NonNull String timestamp) {
        this.quizResultId = quizResultId;
        this.questionId = questionId;
        this.userId = userId;
        this.userAns = userAns;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getQuizResultId() {
        return quizResultId;
    }

    public void setQuizResultId(String quizResultId) {
        this.quizResultId = quizResultId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAns() {
        return userAns;
    }

    public void setUserAns(String userAns) {
        this.userAns = userAns;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
