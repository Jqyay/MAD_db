package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VerPostDao {

    // Insert a VerPost record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VerPost verPost);

    // Insert a list of VerPost records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VerPost> verPosts);

    // Update an existing VerPost record
    @Update
    void update(VerPost verPost);

    // Get all VerPost records
    @Query("SELECT * FROM verPost")
    List<VerPost> getAll();

    // Get a specific VerPost record by verPostId
    @Query("SELECT * FROM verPost WHERE verPostId = :verPostId LIMIT 1")
    VerPost getById(String verPostId);

    // Get a specific VerPost record by postId
    @Query("SELECT * FROM verPost WHERE postId = :postId LIMIT 1")
    VerPost getByPostId(String postId);

    // Get all VerPost records for a specific staff member (by staffId)
    @Query("SELECT * FROM verPost WHERE staffId = :staffId")
    List<VerPost> getByStaffId(String staffId);

    // Get all VerPost records with a specific verifiedStatus
    @Query("SELECT * FROM verPost WHERE verifiedStatus = :status")
    List<VerPost> getByStatus(String status);

    // Delete a VerPost record
    @Delete
    void delete(VerPost verPost);

    @Query("DELETE FROM verPost")
    void deleteAll();
}
