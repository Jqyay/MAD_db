package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    // Insert a new notification
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notification notification);

    // Insert multiple notifications
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Notification> notifications);

    // Update an existing notification
    @Update
    void update(Notification notification);

    // Query to fetch all notifications for a specific user
    @Query("SELECT * FROM notification WHERE userId = :userId")
    List<Notification> getByUserId(String userId);

    // Query to get a specific notification by its ID
    @Query("SELECT * FROM notification WHERE notificationId = :notificationId LIMIT 1")
    Notification getByNotificationId(String notificationId);

    // Query to fetch unread notifications for a user
    @Query("SELECT * FROM notification WHERE userId = :userId AND readTime IS NULL")
    List<Notification> getUnreadNotificationsByUserId(String userId);

    // Delete all notifications for a specific user
    @Query("DELETE FROM notification WHERE userId = :userId")
    void delete(String userId);

    // Delete a notification
    @Delete
    void delete(Notification notification);

    // Delete all notifications
    @Query("DELETE FROM notification")
    void deleteAll();

}
