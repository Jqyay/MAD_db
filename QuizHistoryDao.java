package com.example.madguardians.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizHistoryDao {

    // Insert a new quiz history record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizHistory quizHistory);

    // Insert a list of quiz history records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizHistory> quizHistories);

    // Update an existing quiz history record
    @Update
    void update(QuizHistory quizHistory);

    // Get a specific quiz history by quizId and userId
    @Query("SELECT * FROM quizHistory WHERE quizId = :quizId AND userId = :userId")
    LiveData<QuizHistory> getByQuizId(String quizId, String userId);

    // Get all quiz histories for a specific user
    @Query("SELECT * FROM quizHistory WHERE userId = :userId")
    LiveData<List<QuizHistory>> getAllByUserId(String userId);

    // Get all quiz histories for a specific quiz
    @Query("SELECT * FROM quizHistory WHERE quizId = :quizId")
    LiveData<List<QuizHistory>> getAllByQuizId(String quizId);

    // Get the highest score for a specific quiz by any user
    @Query("SELECT MAX(score) FROM quizHistory WHERE quizId = :quizId")
    LiveData<Integer> getHighestScoreForQuiz(String quizId);

    // Delete a specific quiz history record
    @Delete
    void delete(QuizHistory quizHistory);

    // Delete all quiz history records
    @Query("DELETE FROM quizHistory")
    void deleteAll();
}
