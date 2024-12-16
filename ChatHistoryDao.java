package com.example.madguardians.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatHistoryDao {

    // Insert a single chat message
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChatHistory chatHistory);

    // Insert multiple chat messages
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ChatHistory> chatHistories);

    // Update a chat message
    @Update
    void update(ChatHistory chatHistory);

    // Query to fetch all chat messages
    @Query("SELECT * FROM chatHistory")
    List<ChatHistory> getAll();

    // Query to fetch chat messages by sender user ID
    @Query("SELECT * FROM chatHistory WHERE senderUserId = :senderUserId")
    List<ChatHistory> getBySenderUser(String senderUserId);

    // Query to fetch chat messages by recipient user ID
    @Query("SELECT * FROM chatHistory WHERE recipientUserId = :recipientUserId")
    List<ChatHistory> getByRecipientUser(String recipientUserId);

    // Query to fetch chat messages with media
    @Query("SELECT * FROM chatHistory WHERE mediaId IS NOT NULL")
    List<ChatHistory> getWithMedia();

    // Query to fetch replies to a specific message
    @Query("SELECT * FROM chatHistory WHERE replyMessage = :replyMessageId")
    List<ChatHistory> getReplies(String replyMessageId);

    // Query to fetch a single message by its ID
    @Query("SELECT * FROM chatHistory WHERE messageId = :messageId")
    ChatHistory getById(String messageId);

    // Delete a specific chat message
    @Delete
    void delete(ChatHistory chatHistory);

    // Query to delete all chat history
    @Query("DELETE FROM chatHistory")
    void deleteAll();
}