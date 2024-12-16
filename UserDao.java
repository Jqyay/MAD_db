package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    // Insert a new User
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    // Insert multiple users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    // Update an existing User
    @Update
    void update(User user);

    // Query all Users
    @Query("SELECT * FROM user")
    List<User> getAll();

    // Query a User by userId
    @Query("SELECT * FROM user WHERE userId = :userId")
    User getById(String userId);

    // Query a User by email
    @Query("SELECT * FROM user WHERE email = :email")
    User getByEmail(String email);

    // Query a User by phone number
    @Query("SELECT * FROM user WHERE phoneNo = :phoneNo")
    User getByPhoneNo(int phoneNo);

    // Update the strikeLoginDays for a User
    @Query("UPDATE user SET strikeLoginDays = :strikeLoginDays WHERE userId = :userId")
    void updateStrikeLoginDays(String userId, int strikeLoginDays);

    @Query("SELECT userId FROM user ORDER BY userId DESC LIMIT 1")
    String getLastUserId();

    // Check if username exists
    @Query("SELECT COUNT(*) > 0 FROM user WHERE name = :username")
    boolean usernameExists(String username);

    // Check if username exists
    @Query("SELECT COUNT(*) > 0 FROM user WHERE email = :email")
    boolean emailExists(String email);

    // Delete a User
    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();

}