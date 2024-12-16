package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizOldDao {

    // Insert a new QuizOld record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizOld quizOld);

    // Insert a list of QuizOld records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuizOld> quizOlds);

    // Update an existing QuizOld record
    @Update
    void update(QuizOld quizOld);

    // Query to get all QuizOld records
    @Query("SELECT * FROM quizOld")
    List<QuizOld> getAll();

    // Query to get a specific QuizOld record by quizId and postId
    @Query("SELECT * FROM quizOld WHERE quizId = :quizId AND postId = :postId")
    QuizOld getByQuizPostId(String quizId, String postId);

    // Query to get all QuizOld records for a specific quizId
    @Query("SELECT * FROM quizOld WHERE quizId = :quizId")
    List<QuizOld> getByQuizId(String quizId);

    // Query to get all QuizOld records for a specific postId
    @Query("SELECT * FROM quizOld WHERE postId = :postId")
    List<QuizOld> getByPostId(String postId);

    // Delete a specific QuizOld record
    @Delete
    void delete(QuizOld quizOld);

    @Query("DELETE FROM quizOld")
    void deleteAll();
}

