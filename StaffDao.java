package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StaffDao {

    // Insert a new staff record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Staff staff);

    // Insert a new staff record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Staff> staff);

    // Update an existing staff record
    @Update
    void update(Staff staff);

    // Query to get all staff members
    @Query("SELECT * FROM staff")
    List<Staff> getAll();

    // Query to get a specific staff member by ID
    @Query("SELECT * FROM staff WHERE staffId = :staffId")
    Staff getById(String staffId);

    // Query to get a specific staff member by email
    @Query("SELECT * FROM staff WHERE email = :email")
    Staff getByEmail(String email);

    // Query to get staff member by name
    @Query("SELECT * FROM staff WHERE name = :name")
    List<Staff> getByName(String name);

    // Delete a staff record
    @Delete
    void delete(Staff staff);

    @Query("DELETE FROM staff")
    void deleteAll();
}
