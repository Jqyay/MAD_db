package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TimeslotDao {

    // Insert a new Timeslot
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Timeslot timeslot);

    // Insert a new Timeslot
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Timeslot> timeslot);

    // Update an existing Timeslot
    @Update
    void update(Timeslot timeslot);

    // Get all Timeslots
    @Query("SELECT * FROM timeslot")
    List<Timeslot> getAll();

    // Get a specific Timeslot by its ID
    @Query("SELECT * FROM timeslot WHERE timeslotId = :timeslotId")
    Timeslot getById(String timeslotId);

    // Delete a Timeslot
    @Delete
    void delete(Timeslot timeslot);

    @Query("DELETE FROM timeslot")
    void deleteAll();
}
