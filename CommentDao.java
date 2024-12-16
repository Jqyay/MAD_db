package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommentDao {

    // Insert a single comment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    // Insert multiple comments
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Comment> comments);

    // Update an existing comment
    @Update
    void update(Comment comment);

    // Query to fetch all comments
    @Query("SELECT * FROM comment")
    List<Comment> getAll();

    // Query to fetch comments by postId
    @Query("SELECT * FROM comment WHERE postId = :postId")
    List<Comment> getByPostId(String postId);

    // Query to fetch comments by userId
    @Query("SELECT * FROM comment WHERE userId = :userId")
    List<Comment> getByUserId(String userId);

    // Query to fetch a comment by its commentId
    @Query("SELECT * FROM comment WHERE commentId = :commentId")
    Comment getById(String commentId);

    // Query to fetch root comments (comments without parent)
    @Query("SELECT * FROM comment WHERE rootComment IS NULL")
    List<Comment> getRootComments();

    // Query to fetch comments by replyUserId
    @Query("SELECT * FROM comment WHERE replyUserId = :replyUserId")
    List<Comment> getByReplyUserId(String replyUserId);

    // Query to fetch unread comments
    @Query("SELECT * FROM comment WHERE isRead = 0")
    List<Comment> getUnreadComments();

    // Delete a specific comment
    @Delete
    void delete(Comment comment);

    // Query to delete all comments
    @Query("DELETE FROM comment")
    void deleteAll();
}
