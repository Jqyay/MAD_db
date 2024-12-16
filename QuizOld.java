package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "quizOld",
        primaryKeys = {"quizId", "postId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Post.class,
                        parentColumns = "postId",
                        childColumns = "postId",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class QuizOld {
    @NonNull
    private String quizId;
    @NonNull
    private String postId;

    public QuizOld() {
    }

    // Constructor
    public QuizOld(@NonNull String quizId, @NonNull String postId) {
        this.quizId = quizId;
        this.postId = postId;
    }

    // Getter and Setter for quizId
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    // Getter and Setter for postId
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}