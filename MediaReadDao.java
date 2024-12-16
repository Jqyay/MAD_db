package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MediaReadDao {

    // Insert a new media read record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MediaRead mediaRead);

    // Insert a list of new media read record
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MediaRead> mediaRead);

    @Update
    void update(MediaRead mediaRead);

    // Query to get a media read by userId, postId, and mediaId
    @Query("SELECT * FROM mediaRead WHERE userId = :userId AND postId = :postId AND mediaId = :mediaId")
    MediaRead getByAll(String userId, String postId, String mediaId);

    // Query to get media reads by userId
    @Query("SELECT * FROM mediaRead WHERE userId = :userId")
    List<MediaRead> getByUserId(String userId);

    // Query to get media reads by postId
    @Query("SELECT * FROM mediaRead WHERE postId = :postId")
    List<MediaRead> getByPostId(String postId);

    // Query to get media reads by mediaId
    @Query("SELECT * FROM mediaRead WHERE mediaId = :mediaId")
    List<MediaRead> getByMediaId(String mediaId);

    // Delete a media read record
    @Delete
    void delete(MediaRead mediaRead);

    @Query("DELETE FROM mediaRead")
    void deleteAll();
}
