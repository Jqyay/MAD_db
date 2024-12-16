package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "questionOption",
        primaryKeys = {"questionId", "choice"},
        foreignKeys = {
                @ForeignKey(
                        entity = QuizQuestion.class,
                        parentColumns = "questionId",
                        childColumns = "questionId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {@Index(value = "choice", unique = true)}
)
public class QuestionOption {
    @NonNull
    private String questionId;
    @NonNull
    private String choice;
    @NonNull
    @ColumnInfo(defaultValue = "0")
    private int isCorrect;

    public QuestionOption() {
    }

    // Constructor
    public QuestionOption(@NonNull String questionId, @NonNull String choice, int isCorrect) {
        this.questionId = questionId;
        this.choice = choice;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public int isCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }
}
