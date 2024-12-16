package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuestionOptionDao {

    // Insert a new QuestionOption
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuestionOption questionOption);

    // Insert multiple QuestionOptions
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionOption> questionOptions);

    // Update an existing QuestionOption
    @Update
    void update(QuestionOption questionOption);

    // Get all QuestionOptions for a specific questionId
    @Query("SELECT * FROM questionOption WHERE questionId = :questionId")
    List<QuestionOption> getByQuestionId(String questionId);

    // Get the correct answer for a specific questionId
    @Query("SELECT * FROM questionOption WHERE questionId = :questionId AND isCorrect = 1 LIMIT 1")
    QuestionOption getCorrectOption(String questionId);

    // Delete all options for a specific questionId
    @Query("DELETE FROM questionOption WHERE questionId = :questionId")
    void deleteByQuestionId(String questionId);

    // Delete a specific QuestionOption
    @Delete
    void delete(QuestionOption questionOption);

    @Query("DELETE FROM questionOption")
    void deleteAll();
}
