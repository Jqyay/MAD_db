package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BadgeDao {

    // Insert a single badge
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Badge badge);

    // Insert multiple badges
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Badge> badges);

    // Update a badge
    @Update
    void update(Badge badge);

    // Query to fetch all badges
    @Query("SELECT * FROM badge")
    List<Badge> getAll();

    // Query to fetch a badge by its ID
    @Query("SELECT * FROM badge WHERE badgeId = :badgeId")
    Badge getById(String badgeId);

    // Query to fetch a badge by its name
    @Query("SELECT * FROM badge WHERE badgeName = :badgeName")
    Badge getByName(String badgeName);

    // Delete a specific badge
    @Delete
    void delete(Badge badge);

    // Delete all badges
    @Query("DELETE FROM badge")
    void deleteAll();
}
