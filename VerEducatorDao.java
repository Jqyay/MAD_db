package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VerEducatorDao {

    // Insert a new VerEducator record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VerEducator verEducator);

    // Insert a list of VerEducator records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VerEducator> verEducators);

    // Update an existing VerEducator record
    @Update
    void update(VerEducator verEducator);

    // Retrieve a VerEducator by userId and domainId
    @Query("SELECT * FROM verEducator WHERE userId = :userId AND domainId = :domainId")
    VerEducator getByUserDomainId(String userId, String domainId);

    // Retrieve all VerEducator records
    @Query("SELECT * FROM verEducator")
    List<VerEducator> getAll();

    // Retrieve all VerEducators by domainId
    @Query("SELECT * FROM verEducator WHERE domainId = :domainId")
    List<VerEducator> getByDomainId(String domainId);

    // Retrieve all VerEducators by staffId
    @Query("SELECT * FROM verEducator WHERE staffId = :staffId")
    List<VerEducator> getByStaffId(String staffId);

    // Retrieve a VerEducator by userId
    @Query("SELECT * FROM verEducator WHERE userId = :userId")
    VerEducator getByUserId(String userId);

    // Retrieve all VerEducators with a specific verifiedStatus
    @Query("SELECT * FROM verEducator WHERE verifiedStatus = :verifiedStatus")
    List<VerEducator> getByVerifiedStatus(String verifiedStatus);

    // Delete a VerEducator record
    @Delete
    void delete(VerEducator verEducator);

    // Delete all VerEducator records
    @Query("DELETE FROM verEducator")
    void deleteAll();
}
