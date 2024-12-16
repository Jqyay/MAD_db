package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DomainInterestedDao {

    // Insert a new DomainInterested record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DomainInterested domainInterested);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DomainInterested> list);  // Insert a list of posts into the database

    @Update
    void update(DomainInterested domainInterested);

    // Query to find DomainInterested by userId and domainId (primary key)
    @Query("SELECT * FROM domainInterested WHERE userId = :userId AND domainId = :domainId")
    DomainInterested getByIds(String userId, String domainId);

    // Query to get all records for a specific userId
    @Query("SELECT * FROM domainInterested WHERE userId = :userId")
    List<DomainInterested> getAllByUser(String userId);

    // Query to get all records for a specific domainId
    @Query("SELECT * FROM domainInterested WHERE domainId = :domainId")
    List<DomainInterested> getAllUsersByDomain(String domainId);

    // Delete a DomainInterested record
    @Delete
    void delete(DomainInterested domainInterested);

    @Query("DELETE FROM domainInterested")
    void deleteAll();
}