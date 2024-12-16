package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName ="quizHistory",
        foreignKeys = {
                @ForeignKey(
                        entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
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
public class QuizHistory {
    @PrimaryKey
    @NonNull
    private String quizId;

    @NonNull
    private String userId;

    @NonNull
    private int score;
    @NonNull
    private String timestamp;

    public QuizHistory() {
    }

    // Constructor
    public QuizHistory(@NonNull String quizId, @NonNull String userId, int score, @NonNull String timestamp) {
        this.quizId = quizId;
        this.userId = userId;
        this.score = score;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
