package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizDao {

    // Insert a new Quiz record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quiz quizId);

    // Insert a list of Quiz records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Quiz> quizIds);

    // Update an existing Quiz record
    @Update
    void update(Quiz quizId);

    // Query to get all Quiz records
    @Query("SELECT * FROM quiz")
    List<Quiz> getAll();

    // Delete a specific Quiz record
    @Delete
    void delete(Quiz quizId);

    @Query("DELETE FROM quiz")
    void deleteAll();
}
