package com.example.madguardians.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MediaSetDao {

    // Insert a new MediaSet
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaSet mediaSet);

    // Insert multiple MediaSets
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MediaSet> mediaSets);

    // Update an existing MediaSet
    @Update
    void update(MediaSet mediaSet);

    // Query to fetch all MediaSets
    @Query("SELECT * FROM mediaSet")
    List<MediaSet> getAll();

    // Query to get a MediaSet by its mediaId
    @Query("SELECT * FROM mediaSet WHERE mediaSetId = :mediaSetId LIMIT 1")
    MediaSet getById(String mediaSetId);

    // Delete a MediaSet
    @Delete
    void delete(MediaSet mediaSet);

    // Delete all MediaSets
    @Query("DELETE FROM mediaSet")
    void deleteAll();
}