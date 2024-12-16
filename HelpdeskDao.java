package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HelpdeskDao {

    // Insert a new helpdesk entry
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Helpdesk helpdesk);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Helpdesk> helpdeskList);

    // Update an existing helpdesk entry
    @Update
    void update(Helpdesk helpdesk);

    // Query to get a helpdesk by its ID
    @Query("SELECT * FROM helpdesk WHERE helpdeskId = :helpdeskId")
    Helpdesk getById(String helpdeskId);

    // Query to get all helpdesk entries
    @Query("SELECT * FROM helpdesk")
    List<Helpdesk> getAll();

    // Query to get helpdesk entries by userId
    @Query("SELECT * FROM helpdesk WHERE userId = :userId")
    List<Helpdesk> getByUserId(String userId);

    // Query to get helpdesk entries by issueId
    @Query("SELECT * FROM helpdesk WHERE issueId = :issueId")
    List<Helpdesk> getByIssueId(String issueId);

    // Query to get helpdesk entries by helpdesk status
    @Query("SELECT * FROM helpdesk WHERE helpdeskStatus = :status")
    List<Helpdesk> getByStatus(String status);

    // Query to update the helpdesk status by helpdeskId
    @Query("UPDATE helpdesk SET helpdeskStatus = :status WHERE helpdeskId = :helpdeskId")
    void updateStatus(String helpdeskId, String status);

    // Delete a helpdesk entry
    @Delete
    void delete(Helpdesk helpdesk);

    @Query("DELETE FROM helpdesk")
    void deleteAll();
}