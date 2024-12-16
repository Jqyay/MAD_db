package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserHistoryDao {

    // Insert a new UserHistory
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserHistory userHistory);

    // Insert a new UserHistory
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserHistory> userHistory);

    // Update an existing UserHistory
    @Update
    void update(UserHistory userHistory);

    // Get all user history for a specific user
    @Query("SELECT * FROM userHistory WHERE userId = :userId")
    List<UserHistory> getByUserId(String userId);

    // Get all user history for a specific post
    @Query("SELECT * FROM userHistory WHERE postId = :postId")
    List<UserHistory> getByPostId(String postId);

    // Get user history for a specific user and post
    @Query("SELECT * FROM userHistory WHERE userId = :userId AND postId = :postId")
    UserHistory getByUserPostId(String userId, String postId);

    // Get the progress of a specific user on a specific post
    @Query("SELECT progress FROM userHistory WHERE userId = :userId AND postId = :postId")
    int getProgressByUserPostId(String userId, String postId);

    // Delete a UserHistory
    @Delete
    void delete(UserHistory userHistory);

    @Query("DELETE FROM userHistory")
    void deleteAll();
}
