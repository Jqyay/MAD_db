package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AppointmentDao {

    // Insert a single appointment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Appointment appointment);

    // Insert multiple appointments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Appointment> appointments);

    // Update an appointment
    @Update
    void update(Appointment appointment);

    // Query to get all appointments
    @Query("SELECT * FROM appointment")
    List<Appointment> getAll();

    // Query to get appointments by userId
    @Query("SELECT * FROM appointment WHERE userId = :userId")
    List<Appointment> getByUserId(String userId);

    // Query to get appointments by counselorAvailabilityId
    @Query("SELECT * FROM appointment WHERE counselorAvailabilityId = :counselorAvailabilityId")
    List<Appointment> getByCounselorAvailabilityId(String counselorAvailabilityId);

    // Query to get a specific appointment by composite key
    @Query("SELECT * FROM appointment WHERE counselorAvailabilityId = :counselorAvailabilityId AND userId = :userId")
    Appointment getByUserCounselorAvailabilityIds(String counselorAvailabilityId, String userId);

    // Query to check if an appointment is online
    @Query("SELECT isOnline FROM appointment WHERE counselorAvailabilityId = :availabilityId AND userId = :userId")
    boolean isOnline(String availabilityId, String userId);

    // Delete a specific appointment
    @Delete
    void delete(Appointment appointment);

    // Delete all appointments
    @Query("DELETE FROM appointment")
    void deleteAll();
}