package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MediaDao {

    // Insert a new Media
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Media media);

    // Insert multiple Medias
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Media> medias);

    // Update an existing Media
    @Update
    void update(Media media);

    // Query to fetch all Medias
    @Query("SELECT * FROM media")
    List<Media> getAll();

    // Query to get a Media by its mediaId
    @Query("SELECT * FROM media WHERE mediaId = :mediaId LIMIT 1")
    Media getById(String mediaId);

    // Query to fetch Medias by their mediaSetId
    @Query("SELECT * FROM media WHERE mediaSetId = :mediaSetId")
    List<Media> getBySetId(String mediaSetId);

    // Delete a Media
    @Delete
    void delete(Media media);

    // Delete all Media
    @Query("DELETE FROM media")
    void deleteAll();
}