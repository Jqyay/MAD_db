package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CounselorDao {

    // Insert a single counselor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Counselor counselor);

    // Insert multiple counselors
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Counselor> counselors);

    // Update an existing counselor
    @Update
    void update(Counselor counselor);

    // Query to fetch all counselors
    @Query("SELECT * FROM counselor")
    List<Counselor> getAll();

    // Query to fetch a counselor by counselorId
    @Query("SELECT * FROM counselor WHERE counselorId = :counselorId")
    Counselor getById(String counselorId);

    // Query to fetch a counselor by email (unique)
    @Query("SELECT * FROM counselor WHERE email = :email")
    Counselor getByEmail(String email);

    // Query to fetch counselors by office
    @Query("SELECT * FROM counselor WHERE office = :office")
    List<Counselor> getByOffice(String office);

    // Delete a specific counselor
    @Delete
    void delete(Counselor counselor);

    // Query to delete all counselors
    @Query("DELETE FROM counselor")
    void deleteAll();
}