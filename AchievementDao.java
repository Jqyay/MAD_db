package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AchievementDao {

    // Insert a single achievement
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Achievement achievement);

    // Insert multiple achievements
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Achievement> achievements);

    // Update an achievement
    @Update
    void update(Achievement achievement);

    // Query to get all achievements
    @Query("SELECT * FROM achievement")
    List<Achievement> getAll();

    // Query to get achievements by userId
    @Query("SELECT * FROM achievement WHERE userId = :userId")
    List<Achievement> getByUserId(String userId);

    // Query to get achievements by badgeId
    @Query("SELECT * FROM achievement WHERE badgeId = :badgeId")
    List<Achievement> getByBadgeId(String badgeId);

    // Delete a specific achievement
    @Delete
    void delete(Achievement achievement);

    // Delete all achievements
    @Query("DELETE FROM achievement")
    void deleteAll();
}