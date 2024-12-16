package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizResultDao {

    // Insert a new QuizResult
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizResult quizResult);

    // Insert multiple QuizResults
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizResult> quizResults);

    // Update an existing QuizResult
    @Update
    void update(QuizResult quizResult);

    // Get a QuizResult by quizResultId
    @Query("SELECT * FROM quizResult WHERE quizResultId = :quizResultId")
    QuizResult getById(String quizResultId);

    // Get all QuizResults for a specific user
    @Query("SELECT * FROM quizResult WHERE userId = :userId")
    List<QuizResult> getByUserId(String userId);

    // Get all QuizResults for a specific question
    @Query("SELECT * FROM quizResult WHERE questionId = :questionId")
    List<QuizResult> getByQuestionId(String questionId);

    // Get all QuizResults for a specific user and question
    @Query("SELECT * FROM quizResult WHERE userId = :userId AND questionId = :questionId")
    List<QuizResult> getByUserQuestionId(String userId, String questionId);

    // Delete a QuizResult
    @Delete
    void delete(QuizResult quizResult);

    // Delete all QuizResults for a specific user
    @Query("DELETE FROM quizResult WHERE userId = :userId")
    void deleteByUserId(String userId);

    @Query("DELETE FROM quizResult")
    void deleteAll();
}
