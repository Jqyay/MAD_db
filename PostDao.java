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
public interface PostDao {

    // Insert a new post into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    // Insert a new post into the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> post);

    // Update an existing post
    @Update
    void update(Post post);

    // Get all posts
    @Query("SELECT * FROM post")
    LiveData<List<Post>> getAll();

    // Get a specific post by its ID
    @Query("SELECT * FROM post WHERE postId = :postId")
    LiveData<Post> getById(String postId);

    // Get posts by user ID
    @Query("SELECT * FROM post WHERE userId = :userId")
    LiveData<List<Post>> getByUserId(String userId);

    // Get posts by domain ID
    @Query("SELECT * FROM post WHERE domainId = :domainId")
    LiveData<List<Post>> getByDomainId(String domainId);

    // Get posts within a specific date range
    @Query("SELECT * FROM post WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Post>> getByDateRange(String startDate, String endDate);

    // Delete a specific post
    @Delete
    void delete(Post post);

    @Query("DELETE FROM post")
    void deleteAll();
}
