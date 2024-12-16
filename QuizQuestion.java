package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "quizQuestion",
        foreignKeys = {
                @ForeignKey(
                        entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class QuizQuestion {
    @PrimaryKey
    @NonNull
    private String questionId;

    @NonNull
    private String quizId;
    @NonNull
    private String question;

    @NonNull
    private int questionNo;

    public QuizQuestion() {
    }

    // Constructor
    public QuizQuestion(String questionId, @NonNull String quizId, @NonNull String question, int questionNo) {
        this.questionId = questionId;
        this.quizId = quizId;
        this.question = question;
        this.questionNo = questionNo;
    }

    // Getters and Setters
    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }
}
