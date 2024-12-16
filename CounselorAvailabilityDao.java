package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CounselorAvailabilityDao {

    // Insert a single counselor availability
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CounselorAvailability counselorAvailability);

    // Insert multiple counselor availabilities
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CounselorAvailability> counselorAvailabilities);

    // Update an existing counselor availability
    @Update
    void update(CounselorAvailability counselorAvailability);

    // Query to fetch all counselor availabilities
    @Query("SELECT * FROM counselorAvailability")
    List<CounselorAvailability> getAll();

    // Query to fetch counselor availability by counselorId
    @Query("SELECT * FROM counselorAvailability WHERE counselorId = :counselorId")
    List<CounselorAvailability> getByCounselorId(String counselorId);

    // Query to fetch counselor availability by counselorAvailabilityId
    @Query("SELECT * FROM counselorAvailability WHERE counselorAvailabilityId = :counselorAvailabilityId")
    CounselorAvailability getByCounselorAvailabilityId(String counselorAvailabilityId);

    // Query to fetch all available slots (isBooked = false)
    @Query("SELECT * FROM counselorAvailability WHERE isBooked = 0")
    List<CounselorAvailability> getIsNotBooked();

    // Query to fetch counselor availability by date
    @Query("SELECT * FROM counselorAvailability WHERE date = :date")
    List<CounselorAvailability> getByDate(String date);

    // Delete a specific counselor availability
    @Delete
    void delete(CounselorAvailability counselorAvailability);

    // Query to delete all counselor availabilities
    @Query("DELETE FROM counselorAvailability")
    void deleteAll();
}