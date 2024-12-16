package com.example.madguardians.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {

    // Insert a single course
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    // Insert multiple courses
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Course> courses);

    // Update an existing course
    @Update
    void update(Course course);

    // Query to get all courses
    @Query("SELECT * FROM course")
    List<Course> getAll();

    // Query to get course by courseId
    @Query("SELECT * FROM course WHERE courseId = :courseId")
    Course getById(String courseId);

    // Query to get courses by title (example of searching by title)
    @Query("SELECT * FROM course WHERE title LIKE :title")
    List<Course> getByTitle(String title);

    // Query to get courses by date
    @Query("SELECT * FROM course WHERE date = :date")
    List<Course> getByDate(String date);

    // Delete a specific course
    @Delete
    void delete(Course course);

    // Query to delete all courses
    @Query("DELETE FROM course")
    void deleteAll();
}
