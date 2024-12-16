package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "quiz")
public class Quiz{
    @PrimaryKey
    @NonNull
    private String quizId; // A unique ID for the set of media.

    public Quiz() {
    }

    // Constructor
    public Quiz(@NonNull String quizId) {
        this.quizId = quizId;
    }

    // Getter and Setter
    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }
}
