package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CollectionDao {

    // Insert a single collection
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Collection collection);

    // Insert multiple collections
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Collection> collections);

    // Update a collection
    @Update
    void update(Collection collection);

    // Query to fetch all collections
    @Query("SELECT * FROM collection")
    List<Collection> getAll();

    // Query to fetch collections by userId
    @Query("SELECT * FROM collection WHERE userId = :userId")
    List<Collection> getByUserId(String userId);

    // Query to fetch collections by postId
    @Query("SELECT * FROM collection WHERE postId = :postId")
    List<Collection> getByPostId(String postId);

    // Query to fetch collections by courseId
    @Query("SELECT * FROM collection WHERE courseId = :courseId")
    List<Collection> getByCourseId(String courseId);

    // Query to fetch collections by folderId
    @Query("SELECT * FROM collection WHERE folderId = :folderId")
    List<Collection> getByFolderId(String folderId);

    // Query to fetch a collection by its collectionId
    @Query("SELECT * FROM collection WHERE collectionId = :collectionId")
    Collection getById(String collectionId);

    // Delete a specific collection
    @Delete
    void delete(Collection collection);

    // Query to delete all collections
    @Query("DELETE FROM collection")
    void deleteAll();
}