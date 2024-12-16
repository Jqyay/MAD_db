package com.example.madguardians.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "chatHistory",
        foreignKeys = {
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "senderUserId",
                    onDelete = ForeignKey.SET_NULL
            ),
            @ForeignKey(
                    entity = User.class,
                    parentColumns = "userId",
                    childColumns = "recipientUserId",
                    onDelete = ForeignKey.SET_NULL
            ),
            @ForeignKey(
                    entity = Counselor.class,
                    parentColumns = "counselorId",
                    childColumns = "senderCounselorId",
                    onDelete = ForeignKey.SET_NULL
            ),
            @ForeignKey(
                    entity = Counselor.class,
                    parentColumns = "counselorId",
                    childColumns = "recipientCounselorId",
                    onDelete = ForeignKey.SET_NULL
            ),
            @ForeignKey(
                    entity = MediaSet.class,
                    parentColumns = "mediaSetId",
                    childColumns = "mediaId",
                    onDelete = ForeignKey.SET_NULL
            ),
            @ForeignKey(
                    entity = ChatHistory.class,
                    parentColumns = "messageId",
                    childColumns = "replyMessage",
                    onDelete = ForeignKey.SET_NULL
            )
        }
)
public class ChatHistory {


    @PrimaryKey
    @NonNull
    private String messageId;
    @Nullable
    private String senderUserId;
    @Nullable
    private String recipientUserId;
    @Nullable
    private String senderCounselorId;
    @Nullable
    private String recipientCounselorId;
    @NonNull
    private String message;
    @Nullable
    private String mediaId;
    @Nullable
    private String replyMessage;

    @Nullable
    private String deliveredTime;
    @Nullable
    private String readTime;

    public ChatHistory() {
    }
    // Constructor
    public ChatHistory(@NonNull String messageId, @Nullable String senderUserId,
                       @Nullable String recipientUserId, @Nullable String senderCounselorId,
                       @Nullable String recipientCounselorId,
                       @NonNull String message, @Nullable String mediaId,
                       @Nullable String replyMessage,
                       @Nullable String deliveredTime, @Nullable String readTime) {
        this.messageId = messageId;
        this.senderUserId = senderUserId;
        this.recipientUserId = recipientUserId;
        this.senderCounselorId = senderCounselorId;
        this.recipientCounselorId = recipientCounselorId;
        this.message = message;
        this.mediaId = mediaId;
        this.replyMessage = replyMessage;
        this.deliveredTime = deliveredTime;
        this.readTime = readTime;
    }

    // Getter and Setter for messageId
    @NonNull
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(@NonNull String messageId) {
        this.messageId = messageId;
    }

    // Getter and Setter for senderUserId
    @Nullable
    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(@Nullable String senderUserId) {
        this.senderUserId = senderUserId;
    }

    // Getter and Setter for recipientUserId
    @Nullable
    public String getRecipientUserId() {
        return recipientUserId;
    }

    public void setRecipientUserId(@Nullable String recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    // Getter and Setter for senderCounselorId
    @Nullable
    public String getSenderCounselorId() {
        return senderCounselorId;
    }

    public void setSenderCounselorId(@Nullable String senderCounselorId) {
        this.senderCounselorId = senderCounselorId;
    }

    // Getter and Setter for recipientUserId
    @Nullable
    public String getRecipientCounselorId() {
        return recipientCounselorId;
    }

    public void setRecipientCounselorId(@Nullable String recipientCounselorId) {
        this.recipientCounselorId = recipientCounselorId;
    }

    // Getter and Setter for message
    @NonNull
    public String getMessage() {
        return message;
    }

    public void setMessage(@NonNull String message) {
        this.message = message;
    }

    // Getter and Setter for mediaId
    @Nullable
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(@Nullable String mediaId) {
        this.mediaId = mediaId;
    }

    // Getter and Setter for replyMessage
    @Nullable
    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(@Nullable String replyMessage) {
        this.replyMessage = replyMessage;
    }

    // Getter and Setter for deliveredTime
    @Nullable
    public String getDeliveredTime() {
        return deliveredTime;
    }

    public void setDeliveredTime(@Nullable String deliveredTime) {
        this.deliveredTime = deliveredTime;
    }

    // Getter and Setter for readTime
    @Nullable
    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(@Nullable String readTime) {
        this.readTime = readTime;
    }
}
