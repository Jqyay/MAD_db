package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizQuestionDao {

    // Insert a new QuizQuestion record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizQuestion quizQuestion);

    // Insert a list of QuizQuestion records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizQuestion> quizQuestions);

    // Update an existing QuizQuestion record
    @Update
    void update(QuizQuestion quizQuestion);

    // Query to get all QuizQuestion records
    @Query("SELECT * FROM quizQuestion")
    List<QuizQuestion> getAll();

    // Query to get a specific QuizQuestion record by quizId
    @Query("SELECT * FROM quizQuestion WHERE quizId = :quizId")
    List<QuizQuestion> getByQuizId(String quizId);

    // Query to get a specific QuizQuestion record by questionId
    @Query("SELECT * FROM quizQuestion WHERE questionId = :questionId")
    QuizQuestion getByQuestionId(String questionId);

    // Query to get a specific QuizQuestion record by questionNo
    @Query("SELECT * FROM quizQuestion WHERE questionNo = :questionNo")
    QuizQuestion getByQuestionNo(int questionNo);

    // Delete a specific QuizQuestion record
    @Delete
    void delete(QuizQuestion quizQuestion);

    @Query("DELETE FROM quizQuestion")
    void deleteAll();
}