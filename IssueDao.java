package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IssueDao {

    // Insert a new issue
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Issue issue);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Issue> issueList);

    // Update an existing issue
    @Update
    void update(Issue issue);

    // Query to get an issue by its ID
    @Query("SELECT * FROM issue WHERE issueId = :issueId")
    Issue getById(String issueId);

    // Query to get all issues
    @Query("SELECT * FROM issue")
    List<Issue> getAll();

    // Query to get issues by type
    @Query("SELECT * FROM issue WHERE type = :type")
    List<Issue> getByType(String type);

    // Delete an issue
    @Delete
    void delete(Issue issue);

    @Query("DELETE FROM issue")
    void deleteAll();
}
