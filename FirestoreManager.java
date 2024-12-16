package com.example.madguardians.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Field;
import java.util.function.Consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirestoreManager {
    private final FirebaseFirestore firestore;
    private final AppDatabase database;
    private ExecutorService executorService;

    public FirestoreManager(AppDatabase database) {
        this.firestore = FirebaseFirestore.getInstance();
        this.database = database;
        // Create a single-threaded executor to run database operations in the background
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void onLogin (User user, OnCompleteListener<Boolean> callback){
        firestore.collection("user")
                .whereEqualTo("userId", user.getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onComplete(Tasks.forResult(true));
                    }

                    else{
                        Log.e("User Login", "User is not found: " + user.getUserId());
                        callback.onComplete(Tasks.forResult(false));
                    }
                });
    }

    public void clearTables() {
        database.userDao().deleteAll();
        database.domainDao().deleteAll();
        database.domainInterestedDao().deleteAll();
        database.verEducatorDao().deleteAll();
        database.quizHistoryDao().deleteAll();
        database.quizResultDao().deleteAll();
        database.collectionDao().deleteAll();
        database.folderDao().deleteAll();
        database.userHistoryDao().deleteAll();
        database.mediaReadDao().deleteAll();
        database.appointmentDao().deleteAll();
        database.achievementDao().deleteAll();
        database.chatHistoryDao().deleteAll();
        database.notificationDao().deleteAll();
        database.verPostDao().deleteAll();
        database.mediaSetDao().deleteAll();
        database.mediaDao().deleteAll();
        database.questionOptionDao().deleteAll();
        database.quizQuestionDao().deleteAll();
        database.quizOldDao().deleteAll();
        database.quizDao().deleteAll();
        database.postDao().deleteAll();
        database.courseDao().deleteAll();
        database.commentDao().deleteAll();
        database.issueDao().deleteAll();
        database.timeslotDao().deleteAll();
        database.counselorAvailabilityDao().deleteAll();
        database.badgeDao().deleteAll();
        database.helpdeskDao().deleteAll();
        database.counselorDao().deleteAll();
        database.staffDao().deleteAll();
    }

    public void onLoginSyncUser(String userId) {
        // Perform the database operations asynchronously
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                // Call the potentially long-running operations here
                try {
                    clearTables();
                    syncUserPartial(userId);
                    syncUserFull();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onLoginSyncStaff() {
        clearTables();
        syncUserFull();
        LiveData<List<User>> allUserLiveData =
                syncAll("user", User.class);
        LiveData<List<DomainInterested>> allDomainInterestedLiveData =
                syncAll("domainInterested", DomainInterested.class);
        LiveData<List<VerEducator>> allVerEducatorLiveData =
                syncAll("verEducator", VerEducator.class);
        LiveData<List<QuizHistory>> allQuizHistoryLiveData =
                syncAll("quizHistory", QuizHistory.class);
        LiveData<List<QuizResult>> allQuizResultLiveData =
                syncAll("quizResult", QuizResult.class);
        LiveData<List<Collection>> allCollectionLiveData =
                syncAll("collection", Collection.class);
        LiveData<List<Folder>> allFolderLiveData =
                syncAll("folder", Folder.class);
        LiveData<List<UserHistory>> allUserHistoryLiveData =
                syncAll("userHistory", UserHistory.class);
        LiveData<List<MediaRead>> allMediaReadLiveData =
                syncAll("mediaRead", MediaRead.class);
        LiveData<List<Appointment>> allAppointmentLiveData =
                syncAll("appointment", Appointment.class);
        LiveData<List<Achievement>> allAchievementLiveData =
                syncAll("achievement", Achievement.class);
        LiveData<List<ChatHistory>> allChatHistoryLiveData =
                syncAll("chatHistory", ChatHistory.class);
        LiveData<List<Notification>> allNotificationLiveData =
                syncAll("notification", Notification.class);
        LiveData<List<VerPost>> allVerPostLiveData =
                syncAll("verPost", VerPost.class);
        LiveData<List<Media>> allMediaLiveData =
                syncAll("media", Media.class);
        LiveData<List<MediaSet>> allMediaSetLiveData =
                syncAll("mediaSet", MediaSet.class);
        LiveData<List<Helpdesk>> allHelpdeskSetLiveData =
                syncAll("helpdesk", Helpdesk.class);
        LiveData<List<Counselor>> allCounselorLiveData =
                syncAll("counselor", Counselor.class);
        LiveData<List<Staff>> allStaffLiveData =
                syncAll("staff", Staff.class);
    }

    public void onLoginSyncCounselor(String counselorId) {
        clearTables();
        syncPartialRoleId(counselorId, "counselorId", "appointment");
        syncPartialRoleId(counselorId, "counselorId", "counselorAvailability");
        syncPartialDocumentId(counselorId, "counselor");
        syncPartialRoleId(counselorId, "counselorId", "chatHistory");
        syncPartialJoinByCounselorAvailability(counselorId, appointments -> {
            List<Appointment> appointmentss = (List<Appointment>) appointments;
            if (appointmentss != null) {
                database.appointmentDao().insertAll(appointmentss);
            } else {
                // Handle error
                Log.e("Firestore", "Error fetching media set");
            }
        });
        LiveData<List<Timeslot>> allTimeslotLiveData =
                syncAll("timeslot", Timeslot.class);
    }

    //documentId- autogenerated/attribute, to differentiate to use which method for inserting into firestore

    //From an Activity:
    //onInsertUpdate(tableName, documentId, currentChange, getApplicationContext());
    //From a Fragment:
    //onInsertUpdate(tableName, documentId, currentChange, requireContext().getApplication()

    //Object currentChange = instance of VerPost/Appointment/Comment etc class
    //VerPost ver = new VerPost()
    //onInsertUpdate("insert"/"update", "verPost", ver, context of application/fragment);

    String matchingDocumentId;
    public void onInsertUpdate(String action, String tableName, Object currentChange, Context context) {
        SupportSQLiteOpenHelper.Configuration config = SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(null) // In-memory database for temporary use
                .callback(new SupportSQLiteOpenHelper.Callback(1) {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        // Create a temporary table schema with constraints
                        System.out.println(getTemporaryExecSQL(tableName));
                        if ( getTemporaryExecSQL(tableName)!= null) {
                            for (String sql : getTemporaryExecSQL(tableName)) {
                                db.execSQL(sql);
                            }
                        }

                        // Verify if the table was created using query() method
                        Cursor cursor = db.query("SELECT name FROM sqlite_master WHERE type='table' AND name='tempe'");
                        if (cursor != null && cursor.moveToFirst()) {
                            System.out.println("Table exists: tempe");
                        } else {
                            System.out.println("Table does not exist: tempe");
                        }
                        cursor.close();
                    }

                    @Override
                    public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {
                        // No upgrades needed for temporary use
                    }
                })
                .build();

        SupportSQLiteOpenHelper helper = new FrameworkSQLiteOpenHelperFactory().create(config);
        SupportSQLiteDatabase tempDb = helper.getWritableDatabase();
        try {
            firestore.collection(tableName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            // Use reflection to map fields dynamically
                            ContentValues values = new ContentValues();

                            // Get all fields of the currentChange object's class
                            Field[] fields = currentChange.getClass().getDeclaredFields();
                            for (Field field : fields) {
                                field.setAccessible(true); // Make private fields accessible
                                String fieldName = field.getName(); // Variable name
                                Object fieldValue = document.get(fieldName); // Get corresponding value from Firestore

                                if (fieldValue != null) {
                                    if (fieldValue instanceof String) {
                                        values.put(fieldName, (String) fieldValue);
                                    } else if (fieldValue instanceof Integer) {
                                        values.put(fieldName, (Integer) fieldValue);
                                    } else if (fieldValue instanceof Boolean) {
                                        values.put(fieldName, (Boolean) fieldValue);
                                    } else if (fieldValue instanceof Double) {
                                        values.put(fieldName, (Double) fieldValue);
                                    } else if (fieldValue instanceof Long) {
                                        values.put(fieldName, (Long) fieldValue);
                                    }
                                    // Add other types as necessary
                                }
                            }
//                            SupportSQLiteDatabase tempDb = helper.getWritableDatabase();
                            if (tempDb.isOpen()) {
                                System.out.println("Database is open.");
                            } else {
                                System.out.println("Database is not open.");
                            }

                            tempDb.beginTransaction();
                            try {
                                // Perform insert or other operations
                                tempDb.insert("tempe", SQLiteDatabase.CONFLICT_FAIL, values);
                                tempDb.setTransactionSuccessful();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                tempDb.endTransaction();
                            } }
                    })
                    .addOnFailureListener(e -> Log.e("Firestore", "Error fetching collection: " + tableName, e));
//            // Get all fields of the class, including private ones
//            Field[] fields = currentChange.getClass().getDeclaredFields();
//
//            // Inserting into the database (assuming we have a DAO or similar to insert)
//            ContentValues contentValues = new ContentValues();
//
//            // Loop through each field and print its name and value
//            for (Field field : fields) {
//                field.setAccessible(true); // Make private fields accessible
//                try {
//                    Object data = field.get(currentChange);
//                    if (data instanceof String) {
//                        contentValues.put("name", (String) data);
//                    } else if (data instanceof Integer) {
//                        contentValues.put("age", (Integer) data);
//                    } else if (data instanceof Boolean) {
//                        contentValues.put("isActive", (Boolean) data);
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }


//            // Assuming you're inserting into a table with columns 'name', 'age', and 'isActive'
//            for (Object fieldValue : data) {
//                if (fieldValue instanceof String) {
//                    contentValues.put("name", (String) fieldValue);
//                } else if (fieldValue instanceof Integer) {
//                    contentValues.put("age", (Integer) fieldValue);
//                } else if (fieldValue instanceof Boolean) {
//                    contentValues.put("isActive", (Boolean) fieldValue);
//                }
//            }
            tempDb.execSQL(getInsertStatement(tableName), getVariables(tableName, currentChange));

            if (action.equalsIgnoreCase("insert")&&getDocumentIdType(tableName).equals("autogenerated")){
                // Push to Firestore if validation succeeds
                firestore.collection(tableName)
                        .add((getDomainClass(tableName)).cast(currentChange))
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Data successfully written!"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));
            }
            else if(action.equalsIgnoreCase("update")&&getDocumentIdType(tableName).equals("autogenerated")){
                firestore.collection(tableName)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                if (compareWithReflection(document, currentChange)) {
                                    // Log or use the matching document ID
                                    matchingDocumentId = document.getId();
                                    Log.d("MatchingDocId", "Match found: " + matchingDocumentId);
                                }
                            }
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error fetching collection: " + tableName, e));

                // Push to Firestore if validation succeeds
                firestore.collection(tableName).document(matchingDocumentId)
                        .set((getDomainClass(tableName)).cast(currentChange))
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Data successfully written!"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));

            }
            else{
                firestore.collection(tableName).document(getDocumentId(tableName, currentChange))
                        .set((getDomainClass(tableName)).cast(currentChange))
                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Data successfully written!"))
                        .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));

            }

            insertDataToDatabase(tableName, currentChange);

        } catch (SQLiteConstraintException e) {
            Log.e("Validation", "Validation failed: " + e.getMessage());
        }
        finally{
            if (tempDb.isOpen()) {
                tempDb.execSQL("DROP TABLE IF EXISTS tempe");
            } else {
                Log.e("DatabaseError", "Database is already closed.");
            }
        }
    }

    private boolean compareWithReflection(DocumentSnapshot documentSnapshot, Object currentChange) {
        try {
            // Iterate over all declared fields of the class
            for (Field field : currentChange.getClass().getDeclaredFields()) {
                field.setAccessible(true); // Allow access to private fields

                // Get the field name and value from the Room record
                String fieldName = field.getName();
                Object roomValue = field.get(currentChange);

                // Get the corresponding value from the Firestore document
                Object firestoreValue = documentSnapshot.get(fieldName);

                // Compare the values (null-safe)
                if (roomValue != null && !roomValue.equals(firestoreValue) ||
                        (roomValue == null && firestoreValue != null)) {
                    return false; // Field values don't match
                }
            }
            return true; // All fields match
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false; // Error during reflection
        }
    }

    public void onDelete(String tableName, Object deleteItem){
        try{
            deleteData(tableName, deleteItem);
            // Delete the post from Firestore
            if (!getDocumentIdType(tableName).equals("autogenerated")){
                firestore.collection(tableName)
                        .document(getDocumentId(tableName,deleteItem))
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Post deleted successfully");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Failed to delete post", e);
                        });
            }
            else{
                // Start building the query
                Query query = firestore.collection(tableName);

                // Reflectively get all fields of the item and add where clauses
                Field[] fields = deleteItem.getClass().getDeclaredFields();

                for (Field field : fields) {
                    field.setAccessible(true); // Make private fields accessible
                    try {
                        Object value = field.get(deleteItem); // Get field value
                        if (value != null) {
                            // Add a condition for this field
                            query = query.whereEqualTo(field.getName(), value);
                        }
                    } catch (IllegalAccessException e) {
                        Log.e("Firestore", "Error accessing field: " + field.getName(), e);
                    }
                }

                // Execute the query and delete matching documents
                query.get()
                        .addOnSuccessListener(querySnapshot -> {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document deleted: " + document.getId()))
                                        .addOnFailureListener(e -> Log.e("Firestore", "Error deleting document", e));
                            }
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error fetching documents for deletion", e));
            }
        }catch(SQLiteConstraintException e){
            Log.e("Deletion", "Unable to delete data.");
        }
    }

    private void deleteData (String tableName, Object data){
        switch (tableName) {
            case "achievement":
                database.achievementDao().delete((Achievement)data);
                break;
            case "appointment":
                database.appointmentDao().delete((Appointment)data);
                break;
            case "badge":
                database.badgeDao().delete((Badge)data);
                break;
            case "chatHistory":
                database.chatHistoryDao().delete((ChatHistory)data);
                break;
            case "collection":
                database.collectionDao().delete((Collection)data);
                break;
            case "comment":
                database.commentDao().delete((Comment)data);
                break;
            case "counselorAvailability":
                database.counselorAvailabilityDao().delete((CounselorAvailability)data);
                break;
            case "course":
                database.courseDao().delete((Course)data);
                break;
            case "counselor":
                database.counselorDao().delete((Counselor)data);
                break;
            case "domain":
                database.domainDao().delete((Domain)data);
                break;
            case "domainInterested":
                database.domainInterestedDao().delete((DomainInterested)data);
                break;
            case "folder":
                database.folderDao().delete((Folder)data);
                break;
            case "helpdesk":
                database.helpdeskDao().delete((Helpdesk)data);
                break;
            case "issue":
                database.issueDao().delete((Issue)data);
                break;
            case "media":
                database.mediaDao().delete((Media)data);
                break;
            case "mediaRead":
                database.mediaReadDao().delete((MediaRead)data);
                break;
            case "mediaSet":
                database.mediaSetDao().delete((MediaSet)data);
                break;
            case "notification":
                database.notificationDao().delete((Notification)data);
                break;
            case "post":
                database.postDao().delete((Post)data);
                break;
            case "questionOption":
                database.questionOptionDao().delete((QuestionOption)data);
                break;
            case "quiz":
                database.quizDao().delete((Quiz)data);
                break;
            case "quizHistory":
                database.quizHistoryDao().delete((QuizHistory)data);
                break;
            case "quizOld":
                database.quizOldDao().delete((QuizOld)data);
                break;
            case "quizQuestion":
                database.quizQuestionDao().delete((QuizQuestion)data);
                break;
            case "quizResult":
                database.quizResultDao().delete((QuizResult)data);
                break;
            case "staff":
                database.staffDao().delete((Staff)data);
                break;
            case "timeslot":
                database.timeslotDao().delete((Timeslot)data);
                break;
            case "user":
                database.userDao().delete((User)data);
                break;
            case "userHistory":
                database.userHistoryDao().delete((UserHistory)data);
                break;
            case "verEducator":
                database.verEducatorDao().delete((VerEducator)data);
                break;
            case "verPost":
                database.verPostDao().delete((VerPost)data);
                break;
            default:
                Log.e("FirestoreSync", "Unknown table name: " + tableName);
                break;
        }
    }

    public String getDocumentId(String tableName, Object currentChange){
        switch(tableName){
            case "user":
                return ((User)currentChange).getUserId();
            case "staff":
                return ((Staff)currentChange).getStaffId();
            case "verPost":
                return ((VerPost)currentChange).getVerPostId();
            case "domain":
                return ((Domain)currentChange).getDomainId();
            case "quiz":
                return ((Quiz)currentChange).getQuizId();
            case "quizQuestion":
                return ((QuizQuestion)currentChange).getQuestionId();
            case "quizResult":
                return ((QuizResult)currentChange).getQuizResultId();
            case "media":
                return ((Media)currentChange).getMediaId();
            case"mediaSet":
                return ((MediaSet)currentChange).getMediaSetId();
            case"post":
                return ((Post)currentChange).getPostId();
            case "course":
                return ((Course)currentChange).getCourseId();
            case "collection":
                return ((Collection)currentChange).getCollectionId();
            case "comment":
                return ((Comment)currentChange).getCommentId();
            case "folder":
                return ((Folder)currentChange).getFolderId();
            case "helpdesk":
                return ((Helpdesk)currentChange).getHelpdeskId();
            case "issue":
                return ((Issue)currentChange).getIssueId();
            case "timeslot":
                return ((Timeslot)currentChange).getTimeslotId();
            case"counselor":
                return ((Counselor)currentChange).getCounselorId();
            case"counselorAvailability":
                return ((CounselorAvailability)currentChange).getCounselorAvailabilityId();
            case "badge":
                return ((Badge)currentChange).getBadgeId();
            case "chatHistory":
                return ((ChatHistory)currentChange).getMessageId();
            case "notification":
                return ((Notification)currentChange).getNotificationId();
            default:
                return null;
        }
    }

    public String getDocumentIdType(String tableName){
        switch(tableName){
            case "user":
                return "userId";
            case "staff":
                return "staffId";
            case "verPost":
                return "verPostId";
            case "domain":
                return "domainId";
            case "quiz":
                return "quizId";
            case "quizQuestion":
                return "questionId";
            case "quizResult":
                return "quizResultId";
            case "media":
                return "mediaId";
            case"mediaSet":
                return "mediaSetId";
            case"post":
                return "postId";
            case "course":
                return "courseId";
            case "collection":
                return "collectionId";
            case "comment":
                return "commentId";
            case "folder":
                return "folderId";
            case "helpdesk":
                return "helpdeskId";
            case "issue":
                return "issueId";
            case "timeslot":
                return "timeslotId";
            case"counselor":
                return "counselorId";
            case"counselorAvailability":
                return "counselorAvailabilityId";
            case "badge":
                return "badgeId";
            case "chatHistory":
                return "messageId";
            case "notification":
                return "notificationId";
            default:
                return "autogenerated";
        }
    }


    public String[] getTemporaryExecSQL(String tableName) {
        String[] sql;
        switch (tableName) {
            case "achievement":
                sql = new String[]{
                        "CREATE TABLE e (" +
                                "userId TEXT NOT NULL, " +
                                "badgeId TEXT NOT NULL, " +
                                "PRIMARY KEY(userId, badgeId), " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(badgeId) REFERENCES badge(badgeId) ON DELETE CASCADE" +
                                ");"};
                return sql;
            case "appointment":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "counselorAvailabilityId TEXT NOT NULL UNIQUE, " +
                                "userId TEXT NOT NULL, " +
                                "isOnline INTEGER NOT NULL DEFAULT 1, " +
                                "PRIMARY KEY (counselorAvailabilityId, userId), " +
                                "FOREIGN KEY (counselorAvailabilityId) REFERENCES counselorAvailability(counselorAvailabilityId) ON DELETE RESTRICT, " +
                                "CREATE TRIGGER update_isBooked_on_appointment_insert " +
                                "AFTER INSERT ON appointment " +
                                "FOR EACH ROW " +
                                "BEGIN " +
                                "    UPDATE counselorAvailability " +
                                "    SET isBooked = 1 " +
                                "    WHERE counselorAvailabilityId = NEW.counselorAvailabilityId; " +
                                "END;"
                };
                return sql;
            case "badge":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "badgeId TEXT PRIMARY KEY, " +
                                "badgeName TEXT NOT NULL UNIQUE, " +
                                "badgeImage TEXT NOT NULL UNIQUE);"
                };
                return sql;
            case "chatHistory":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "messageId TEXT PRIMARY KEY, " +
                                "senderUserId TEXT, " +
                                "recipientUserId TEXT, " +
                                "senderCounselorId TEXT, " +
                                "recipientCounselorId TEXT, " +
                                "message TEXT NOT NULL, " +
                                "mediaId TEXT, " +
                                "replyMessage TEXT, " +
                                "deliveredTime TEXT, " +
                                "readTime TEXT, " +
                                "FOREIGN KEY (senderUserId) REFERENCES user(userId) ON DELETE SET NULL, " +
                                "FOREIGN KEY (recipientUserId) REFERENCES user(userId) ON DELETE SET NULL, " +
                                "FOREIGN KEY (senderCounselorId) REFERENCES counselor(counselorId) ON DELETE SET NULL, " +
                                "FOREIGN KEY (recipientCounselorId) REFERENCES counselor(counselorId) ON DELETE SET NULL, " +
                                "FOREIGN KEY (mediaId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL, " +
                                "FOREIGN KEY (replyMessage) REFERENCES chatHistory(messageId) ON DELETE SET NULL);"
                };
                return sql;
            case "collection":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "collectionId TEXT PRIMARY KEY, " +
                                "userId TEXT NOT NULL, " +
                                "postId TEXT, " +
                                "courseId TEXT, " +
                                "folderId TEXT, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(courseId) REFERENCES course(courseId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(folderId) REFERENCES folder(folderId) ON DELETE SET NULL," +
                                "CHECK (postId IS NOT NULL OR courseId IS NOT NULL)" +
                                ");"
                };
                return sql;
            case "comment":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "commentId TEXT PRIMARY KEY, " +
                                "userId TEXT, " +
                                "postId TEXT NOT NULL, " +
                                "comment TEXT NOT NULL, " +
                                "rootComment TEXT, " +
                                "replyUserId TEXT, " +
                                "isRead INTEGER NOT NULL DEFAULT 0, " +
                                "timestamp TEXT NOT NULL, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(rootComment) REFERENCES comment(commentId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(replyUserId) REFERENCES user(userId) ON DELETE SET NULL" +
                                ");"
                };
                return sql;
            case "counselor":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "counselorId TEXT PRIMARY KEY, " +
                                "name TEXT NOT NULL, " +
                                "office TEXT NOT NULL, " +
                                "email TEXT NOT NULL UNIQUE, " +
                                "password TEXT NOT NULL, " +
                                "profilePic TEXT NOT NULL DEFAULT 'url link of the default profilepic', " +
                                "contactNo TEXT NOT NULL UNIQUE" +
                                ");"
                };
                return sql;
            case "counselorAvailability":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "counselorAvailabilityId TEXT PRIMARY KEY, " +
                                "counselorId TEXT NOT NULL, " +
                                "timeslotId TEXT NOT NULL, " +
                                "date TEXT NOT NULL, " +
                                "isBooked INTEGER NOT NULL DEFAULT 0, " +
                                "FOREIGN KEY(counselorId) REFERENCES counselor(counselorId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(timeslotId) REFERENCES timeslot(timeslotId) ON DELETE RESTRICT" +
                                ");"
                };
                return sql;
            case "course":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "courseId TEXT PRIMARY KEY, " +
                                "title TEXT NOT NULL, " +
                                "description TEXT NOT NULL, " +
                                "coverImage TEXT NOT NULL, " +
                                "post1 TEXT, " +
                                "post2 TEXT, " +
                                "post3 TEXT, " +
                                "folderId TEXT, " +
                                "date TEXT NOT NULL, " +
                                "FOREIGN KEY(post1) REFERENCES post(postId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(post2) REFERENCES post(postId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(post3) REFERENCES post(postId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(folderId) REFERENCES folder(folderId) ON DELETE SET NULL" +
                                ");"
                };
                return sql;
            case "domain":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "domainId TEXT PRIMARY KEY, " +
                                "domainName TEXT NOT NULL UNIQUE " +
                                ");"
                };
                return sql;
            case "domainInterested":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "userId TEXT NOT NULL, " +
                                "domainId TEXT NOT NULL, " +
                                "PRIMARY KEY(userId, domainId), " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(domainId) REFERENCES domain(domainId) ON DELETE CASCADE);"
                };
                return sql;
            case "folder":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "folderId TEXT PRIMARY KEY, " +
                                "userId TEXT NOT NULL, " +
                                "name TEXT NOT NULL, " +
                                "rootFolder TEXT, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(rootFolder) REFERENCES folder(folderId) ON DELETE SET NULL" +
                                ");"
                };
                return sql;
            case "helpdesk":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "helpdeskId TEXT PRIMARY KEY, " +
                                "issueId TEXT NOT NULL, " +
                                "userId TEXT NOT NULL, " +
                                "postId TEXT, " +
                                "courseId TEXT, " +
                                "commentId TEXT, " +
                                "quizId TEXT, " +
                                "staffId TEXT NOT NULL, " +
                                "reason TEXT, " +
                                "helpdeskStatus TEXT NOT NULL DEFAULT 'pending', " +
                                "FOREIGN KEY(issueId) REFERENCES issue(issueId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(courseId) REFERENCES course(courseId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(commentId) REFERENCES comment(commentId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(quizId) REFERENCES quizQuestion(quizId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(staffId) REFERENCES staff(staffId) ON DELETE RESTRICT," +
                                "CHECK (postId IS NOT NULL OR courseId IS NOT NULL OR commentId IS NOT NULL OR quizId IS NOT NULL OR helpdeskStatus = 'deleting')" +
                                ");",
                        "CREATE TRIGGER delete_helpdesk_when_all_null " +
                                "AFTER UPDATE OF postId, courseId, commentId, quizId ON tempe " +
                                "FOR EACH ROW " +
                                "WHEN NEW.postId IS NULL AND NEW.courseId IS NULL AND NEW.commentId IS NULL AND NEW.quizId IS NULL " +
                                "BEGIN " +
                                "    DELETE FROM tempe WHERE helpdeskId = NEW.helpdeskId; " +
                                "END;"
                };
                return sql;
            case "issue":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "issueId TEXT PRIMARY KEY, " +
                                "type TEXT NOT NULL " +
                                ");"
                };
                return sql;
            case "media":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "mediaId TEXT PRIMARY KEY, " +
                                "mediaSetId TEXT NOT NULL, " +
                                "url TEXT NOT NULL, " +
                                "FOREIGN KEY(mediaSetId) REFERENCES mediaSet(mediaSetId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "mediaRead":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "mediaId TEXT NOT NULL, " +
                                "postId TEXT NOT NULL, " +
                                "userId TEXT NOT NULL, " +
                                "PRIMARY KEY(mediaId, postId, userId), " +
                                "FOREIGN KEY(mediaId) REFERENCES mediaSet(mediaId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "mediaSet":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "mediaSetId TEXT PRIMARY KEY " +
                                ");"
                };
                return sql;
            case "notification":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "notificationId TEXT PRIMARY KEY, " +
                                "userId TEXT NOT NULL, " +
                                "message TEXT NOT NULL, " +
                                "deliveredTime TEXT, " +
                                "readTime TEXT, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "post":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "postId TEXT PRIMARY KEY, " +
                                "userId TEXT NOT NULL, " +
                                "title TEXT NOT NULL, " +
                                "description TEXT NOT NULL, " +
                                "imageSetId TEXT, " +
                                "videoSetId TEXT, " +
                                "fileSetId TEXT, " +
                                "quizId TEXT, " +
                                "domainId TEXT NOT NULL, " +
                                "folderId TEXT, " +
                                "date TEXT NOT NULL, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(imageSetId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(videoSetId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(fileSetId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(quizId) REFERENCES quiz(quizId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(domainId) REFERENCES domain(domainId) ON DELETE RESTRICT, " +
                                "FOREIGN KEY(folderId) REFERENCES folder(folderId) ON DELETE SET NULL," +
                                "CHECK (imageSetId IS NOT NULL OR videoSetId IS NOT NULL OR fileSetId IS NOT NULL OR quizId IS NOT NULL)" +
                                ");"
                };
                return sql;
            case "questionOption":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "questionId TEXT NOT NULL, " +
                                "choice TEXT NOT NULL, " +
                                "isCorrect INTEGER NOT NULL DEFAULT 0, " +
                                "PRIMARY KEY(questionId, choice), " +
                                "FOREIGN KEY(questionId) REFERENCES quizQuestion(questionId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "quiz":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "quizId TEXT PRIMARY KEY " +
                                ");"
                };
                return sql;
            case "quizHistory":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "quizId TEXT PRIMARY KEY, " +
                                "userId TEXT NOT NULL, " +
                                "score INTEGER NOT NULL, " +
                                "timestamp TEXT NOT NULL, " +
                                "FOREIGN KEY(quizId) REFERENCES quiz(quizId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "quizOld":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "quizId TEXT NOT NULL, " +
                                "postId TEXT NOT NULL, " +
                                "PRIMARY KEY(quizId, postId), " +
                                "FOREIGN KEY(quizId) REFERENCES quiz(quizId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "quizQuestion":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "questionId TEXT PRIMARY KEY, " +
                                "quizId TEXT NOT NULL, " +
                                "question TEXT NOT NULL, " +
                                "questionNo INTEGER NOT NULL," +
                                "FOREIGN KEY(quizId) REFERENCES quiz(quizId) ON DELETE CASCADE " +
                                ");"
                };
                return sql;
            case "quizResult":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "quizResultId TEXT PRIMARY KEY, " +
                                "questionId TEXT NOT NULL, " +
                                "userId TEXT NOT NULL, " +
                                "userAns TEXT NOT NULL, " +
                                "timestamp TEXT NOT NULL, " +
                                "FOREIGN KEY(questionId) REFERENCES quizQuestion(questionId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userAns) REFERENCES questionOption(choice) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "staff":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe(" +
                                "staffId TEXT PRIMARY KEY, " +
                                "name TEXT NOT NULL UNIQUE, " +
                                "email TEXT NOT NULL UNIQUE, " +
                                "password TEXT NOT NULL " +
                                ");"
                };
                return sql;
            case "timeslot":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "timeslotId TEXT PRIMARY KEY, " +
                                "startTime INTEGER NOT NULL, " +
                                "endTime INTEGER NOT NULL " +
                                ");"
                };
                return sql;
            case "user":
                sql = new String[]{
                        "CREATE TABLE tempe (" +
                                "userId TEXT PRIMARY KEY, " +
                                "name TEXT NOT NULL DEFAULT 'bookworm', " +
                                "email TEXT NOT NULL UNIQUE, " +
                                "phoneNo TEXT UNIQUE, " +
                                "password TEXT NOT NULL, " +
                                "profilePic TEXT NOT NULL DEFAULT 'default_profile_pic_url', " +
                                "lastLogin TEXT NOT NULL, " +
                                "strikeLoginDays INTEGER NOT NULL);",
                        "CREATE TRIGGER check_unique_username " +
                                "BEFORE INSERT ON tempe " +
                                "WHEN NEW.name != 'bookworm' " + // Only check non-default usernames
                                "BEGIN " +
                                "    SELECT RAISE(FAIL, 'Username already exists') " +
                                "    WHERE (SELECT COUNT(*) FROM tempe WHERE name = NEW.name) > 0; " +
                                "END;",
                        "CREATE TRIGGER check_unique_username_update " +
                                "BEFORE UPDATE ON tempe " +
                                "WHEN NEW.name != 'bookworm' AND OLD.name != NEW.name " + // Only check when username changes
                                "BEGIN " +
                                "    SELECT RAISE(FAIL, 'Username already exists') " +
                                "    WHERE (SELECT COUNT(*) FROM tempe WHERE name = NEW.name) > 0; " +
                                "END;",
                        "UPDATE tempe " +
                                "SET " +
                                "    strikeLoginDays = CASE " +
                                "        WHEN lastLogin = date('now', '-1 day') THEN strikeLoginDays + 1 " +
                                "        ELSE 1 " +
                                "    END, " +
                                "    lastLogin = date('now')"
                };
                return sql;
            case "userHistory":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "postId TEXT NOT NULL, " +
                                "userId TEXT NOT NULL, " +
                                "progress INTEGER NOT NULL DEFAULT 1, " +
                                "time TEXT NOT NULL, " +
                                "PRIMARY KEY(postId, userId), " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE" +
                                ");"
                };
                return sql;
            case "verEducator":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "userId TEXT NOT NULL, " +
                                "imageSetId TEXT UNIQUE, " +
                                "fileSetId TEXT UNIQUE, " +
                                "domainId TEXT NOT NULL, " +
                                "staffId TEXT NOT NULL, " +
                                "verifiedStatus TEXT NOT NULL DEFAULT 'pending', " +
                                "PRIMARY KEY(userId, domainId), " +
                                "FOREIGN KEY(userId) REFERENCES user(userId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(domainId) REFERENCES domain(domainId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(staffId) REFERENCES staff(staffId) ON DELETE RESTRICT, " +
                                "FOREIGN KEY(imageSetId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL, " +
                                "FOREIGN KEY(fileSetId) REFERENCES mediaSet(mediaSetId) ON DELETE SET NULL " +
                                ");"
                };
                return sql;
            case "verPost":
                sql = new String[]{
                        "CREATE TABLE IF NOT EXISTS tempe (" +
                                "verPostId TEXT PRIMARY KEY, " +
                                "postId TEXT NOT NULL UNIQUE, " +
                                "staffId TEXT NOT NULL, " +
                                "verifiedStatus TEXT NOT NULL DEFAULT 'pending', " +
                                "FOREIGN KEY(postId) REFERENCES post(postId) ON DELETE CASCADE, " +
                                "FOREIGN KEY(staffId) REFERENCES staff(staffId) ON DELETE RESTRICT " +
                                ");"
                };
                return sql;
            default:
                return null;
        }
    }

    private String getInsertStatement(String tableName) {
        switch (tableName) {
            case "achievement":
                return "INSERT INTO tempe (userId, badgeId) VALUES (?, ?)";
            case "appointment":
                return "INSERT INTO tempe (counselorAvailabilityId, userId, isOnline) VALUES (?, ?, ?)";
            case "badge":
                return "INSERT INTO tempe (badgeId, badgeName, badgeImage) VALUES (?, ?, ?)";
            case "chatHistory":
                return "INSERT INTO tempe (messageId, senderUserId, recipientUserId, senderCounselorId, " +
                        "recipientCounselorId, message, mediaId, replyMessage, deliveredTime, readTime)" +
                        " VALUES (?, ?, ?, ?,?,?,?,?,?,?)";
            case "collection":
                return "INSERT INTO tempe (collectionId, userId, postId, courseId, folderId) VALUES (?, ?, ?,?,?)";
            case "comment":
                return "INSERT INTO tempe (commentId, userId, postId, comment, rootComment, replyUserId," +
                        "isRead, timestamp) VALUES (?, ?, ?,?,?,?,?,?)";
            case "counselor":
                return "INSERT INTO tempe (counselorId, name, office, email, password,profilePic,contactNo) " +
                        "VALUES (?, ?, ?,?,?,?,?)";
            case "counselorAvailability":
                return "INSERT INTO tempe (counselorAvailabilityId, counselorId, timeslotId, date" +
                        "isBooked) VALUES (?, ?, ?,?,?)";
            case "course":
                return "INSERT INTO tempe (courseId, title, description, coverImage," +
                        "post1, post2, post3,folderId, date) VALUES (?, ?, ?,?,?,?,?,?,?)";
            case "domain":
                return "INSERT INTO tempe (domainId, domainName) VALUES (?,  ?)";
            case "domainInterested":
                return "INSERT INTO tempe (userId, domainId) VALUES (?, ?)";
            case "folder":
                return "INSERT INTO tempe (folderId,userId, name, rootFolder) VALUES (?, ?, ?,?)";
            case "helpdesk":
                return "INSERT INTO tempe (helpdeskId, issueId, userId, postId, courseId, commentId, " +
                        "quizId, staffId, reason, helpdeskStatus) VALUES (?, ?, ?,?,?,?,?,?,?,?)";
            case "issue":
                return "INSERT INTO tempe (issueId, type) VALUES (?, ?)";
            case "media":
                return "INSERT INTO tempe (mediaId, mediaSetId, url) VALUES (?, ?, ?)";
            case "mediaRead":
                return "INSERT INTO tempe (mediaId, postId, userId) VALUES (?, ?, ?)";
            case "mediaSet":
                return "INSERT INTO tempe (mediaSetId) VALUES (?)";
            case "notification":
                return "INSERT INTO tempe (notificationId, userId, message, deliveredTime, " +
                        "readTime) VALUES (?, ?, ?,?,?)";
            case "post":
                return "INSERT INTO tempe (postId, userId, title, description, imageSetId, videoSetId" +
                        "fileSetId, quizId, domainId,folderId,date) VALUES (?, ?, ?,?,?,?,?,?,?,?,?)";
            case "quiz":
                return "INSERT INTO tempe (quizId) VALUES (?)";
            case "questionOption":
                return "INSERT INTO tempe (questionId, choice, isCorrect) VALUES (?, ?, ?)";
            case "quizHistory":
                return "INSERT INTO tempe (quizId, userId, score, timestamp) VALUES (?, ?, ?,?)";
            case "quizOld":
                return "INSERT INTO tempe (quizId, postId) VALUES (?, ?)";
            case "quizQuestion":
                return "INSERT INTO tempe (questionId, quizId, question, questionNo) VALUES (?, ?, ?,?)";
            case "quizResult":
                return "INSERT INTO tempe (quizResultId, questionId, userId, userAns, timestamp" +
                        ") VALUES (?, ?, ?,?,?)";
            case "staff":
                return "INSERT INTO tempe (staffId, name, email, password) VALUES (?, ?, ?,?)";
            case "timeslot":
                return "INSERT INTO tempe (timeslotId, startTime, endTime) VALUES (?, ?, ?)";
            case "user":
                return "INSERT INTO tempe (userId, name, email, phoneNo, password, profilePic, " +
                        "lastLogin, strikeLoginDays) VALUES (?, ?, ?,?,?,?,?,?)";
            case "userHistory":
                return "INSERT INTO tempe (postId, userId, progress, time) VALUES (?, ?, ?,?)";
            case "verEducator":
                return "INSERT INTO tempe (userId, imageSetId, fileSetId, domainId, staffId, " +
                        "verifiedStatus) VALUES (?, ?, ?,?,?,?)";
            case "verPost":
                return "INSERT INTO tempe (verPostId, postId, staffId, verifiedStatus) VALUES (?, ?, ?,?)";
            default:
                return null;
        }
    }
    private Object[] getVariables(String tableName, Object currentChange){
        switch (tableName) {
            case "achievement":
                Achievement achievement = (Achievement)currentChange;
                return new Object[]{achievement.getUserId(), achievement.getBadgeId()};
            case "appointment":
                Appointment appointment = (Appointment)currentChange;
                return new Object[]{appointment.getCounselorAvailabilityId(),appointment.getUserId(), appointment.isOnline()};
            case "badge":
                Badge badge = (Badge)currentChange;
                return new Object[]{badge.getBadgeId(), badge.getBadgeName(), badge.getBadgeImage()};
            case "chatHistory":
                ChatHistory chat = (ChatHistory)currentChange;
                return new Object[]{chat.getMessageId(), chat.getSenderUserId(), chat.getRecipientUserId(),
                        chat.getSenderCounselorId(), chat.getRecipientCounselorId(), chat.getMessage(),
                        chat.getMediaId(),chat.getReplyMessage(), chat.getDeliveredTime(), chat.getReadTime()
                };
            case "collection":
                Collection collection = (Collection)currentChange;
                return new Object[]{collection.getCollectionId(), collection.getUserId(), collection.getPostId(),
                        collection.getCourseId(), collection.getFolderId()};
            case "comment":
                Comment comment = (Comment)currentChange;
                return new Object[]{comment.getCommentId(), comment.getUserId(), comment.getPostId(),
                    comment.getComment(), comment.getRootComment(), comment.getReplyUserId(),
                    comment.isRead(), comment.getTimestamp()};
            case "counselor":
                Counselor counselor = (Counselor)currentChange;
                return new Object[]{counselor.getCounselorId(), counselor.getName(), counselor.getOffice(),
                    counselor.getEmail(), counselor.getPassword(), counselor.getProfilePic(),
                    counselor.getContactNo()};
            case "counselorAvailability":
                CounselorAvailability availability = (CounselorAvailability)currentChange;
                return new Object[]{availability.getCounselorAvailabilityId(),
                    availability.getCounselorId(),availability.getTimeslotId(), availability.getDate(),
                    availability.isBooked()};
            case "course":
                Course course = (Course)currentChange;
                return new Object[]{course.getCourseId(), course.getTitle(),course.getDescription(),
                        course.getCoverImage(), course.getPost1(),course.getPost2(),
                        course.getPost3(), course.getFolderId(), course.getDate()};
            case "domain":
                Domain domain = (Domain)currentChange;
                return new Object[]{domain.getDomainId(), domain.getDomainName()};
            case "domainInterested":
                DomainInterested domainInt = (DomainInterested)currentChange;
                return new Object[]{domainInt.getUserId(), domainInt.getDomainId()};
            case "folder":
                Folder folder = (Folder)currentChange;
                return new Object[]{folder.getFolderId(), folder.getUserId(), folder.getName(),
                        folder.getRootFolder()};
            case "helpdesk":
                Helpdesk helpdesk = (Helpdesk)currentChange;
                return new Object[]{helpdesk.getHelpdeskId(), helpdesk.getIssueId(), helpdesk.getUserId(),
                    helpdesk.getPostId(), helpdesk.getCourseId(), helpdesk.getCommentId(), helpdesk.getQuizId(),
                    helpdesk.getStaffId(), helpdesk.getReason(), helpdesk.getHelpdeskStatus()};
            case "issue":
                Issue issue = (Issue)currentChange;
                return new Object[]{issue.getIssueId(), issue.getType()};
            case "media":
                Media media = (Media)currentChange;
                return new Object[]{media.getMediaId(), media.getMediaSetId(), media.getUrl()};
            case "mediaRead":
                MediaRead mediaRead = (MediaRead)currentChange;
                return new Object[]{mediaRead.getMediaId(), mediaRead.getPostId(), mediaRead.getUserId()};
            case "mediaSet":
                MediaSet mediaSet = (MediaSet)currentChange;
                return new Object[]{mediaSet.getMediaSetId()};
            case "notification":
                Notification notification = (Notification)currentChange;
                return new Object[]{notification.getNotificationId(), notification.getUserId(),
                    notification.getMessage(), notification.getDeliveredTime(), notification.getReadTime()};
            case "post":
                Post post = (Post)currentChange;
                return new Object[]{post.getPostId(), post.getUserId(), post.getTitle(), post.getDescription(),
                    post.getImageSetId(), post.getVideoSetId(), post.getFileSetId(), post.getQuizId(), post.getDomainId(),
                    post.getFolderId(), post.getDate()};
            case "questionOption":
                QuestionOption quesOpt = (QuestionOption)currentChange;
                return new Object[]{quesOpt.getQuestionId(), quesOpt.getChoice(), quesOpt.isCorrect()};
            case "quiz":
                Quiz quiz = (Quiz)currentChange;
                return new Object[]{quiz.getQuizId()};
            case "quizHistory":
                QuizHistory quizHis = (QuizHistory)currentChange;
                return new Object[]{quizHis.getQuizId(), quizHis.getUserId(), quizHis.getScore(),
                    quizHis.getTimestamp()};
            case "quizOld":
                QuizOld quizOld = (QuizOld)currentChange;
                return new Object[]{quizOld.getQuizId(), quizOld.getPostId()};
            case "quizQuestion":
                QuizQuestion quizQues = (QuizQuestion)currentChange;
                return new Object[]{quizQues.getQuestionId(), quizQues.getQuizId(),
                        quizQues.getQuestion(), quizQues.getQuestionNo()};
            case "quizResult":
                QuizResult result = (QuizResult)currentChange;
                return new Object[]{result.getQuizResultId(), result.getQuestionId(), result.getUserId(),
                    result.getUserAns(), result.getTimestamp()};
            case "staff":
                Staff staff = (Staff)currentChange;
                return new Object[]{staff.getStaffId(), staff.getName(), staff.getEmail(), staff.getPassword()};
            case "timeslot":
                Timeslot timeslot = (Timeslot)currentChange;
                return new Object[]{timeslot.getTimeslotId(), timeslot.getStartTime(), timeslot.getEndTime()};
            case "user":
                User user = (User)currentChange;
                return new Object[]{user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNo(),
                    user.getPassword(), user.getProfilePic(), user.getLastLogin(), user.getStrikeLoginDays()};
            case "userHistory":
                UserHistory userHis = (UserHistory)currentChange;
                return new Object[]{userHis.getPostId(), userHis.getUserId(), userHis.getProgress(),
                    userHis.getTime()};
            case "verEducator":
                VerEducator verEdu = (VerEducator)currentChange;
                return new Object[]{verEdu.getUserId(), verEdu.getImageSetId(), verEdu.getFileSetId(),
                    verEdu.getDomainId(), verEdu.getStaffId()};
            case "verPost":
                VerPost verPost = (VerPost)currentChange;
                return new Object[]{verPost.getVerPostId(), verPost.getPostId(), verPost.getStaffId(),
                    verPost.getVerifiedStatus()};
            default:
                return null;
            }
        }

        // Fetch current user data (upon the login)
        // if only register then no need to fetch the user data
        public void syncUserPartial (String userId){
            syncPartialDocumentId(userId, "user");
            syncPartialRoleId(userId, "userId", "domainInterested");
            syncPartialRoleId(userId, "userId", "verEducator");
            syncPartialRoleId(userId, "userId", "quizHistory");
            syncPartialRoleId(userId, "userId", "quizResult");
            syncPartialRoleId(userId, "userId", "collection");
            syncPartialRoleId(userId, "userId", "folder");
            syncPartialRoleId(userId, "userId", "userHistory");
            syncPartialRoleId(userId, "userId", "mediaRead");
            syncPartialRoleId(userId, "userId", "appointment");
            syncPartialRoleId(userId, "userId", "achievement");
            syncPartialRoleId(userId, "senderUserId", "chatHistory");
            syncPartialRoleId(userId, "recipientUserId", "chatHistory");
            syncPartialRoleId(userId, "userId", "notification");
            syncPartialJoinByPostVerEducator(userId, "verPost", verPosts -> {
                List<VerPost> verPostss = (List<VerPost>) verPosts;
                if (verPostss != null) {
                    database.verPostDao().insertAll(verPostss);
                } else {
                    // Handle error
                    Log.e("Firestore", "Error fetching verification posts");
                }
            });
            syncPartialJoinByPostVerEducator(userId, "mediaSet", mediaSets -> {
                List<MediaSet> mediaSetss = (List<MediaSet>) mediaSets;
                if (mediaSetss != null) {
                    database.mediaSetDao().insertAll(mediaSetss);
                } else {
                    // Handle error
                    Log.e("Firestore", "Error fetching media set");
                }
            });
            syncPartialJoinByPostVerEducator(userId, "media", medias -> {
                List<MediaSet> mediass = (List<MediaSet>) medias;
                if (mediass != null) {
                    database.mediaSetDao().insertAll(mediass);
                } else {
                    // Handle error
                    Log.e("Firestore", "Error fetching media set");
                }
            });
        }

        //Fetch full access data into Room
        public void syncUserFull () {
            LiveData<List<Domain>> allDomainLiveData =
                    syncAll("domain", Domain.class);
            LiveData<List<QuestionOption>> allQuestionOptionLiveData =
                    syncAll("questionOption", QuestionOption.class);
            LiveData<List<QuizQuestion>> allQuizQuestionLiveData =
                    syncAll("quizQuestion", QuizQuestion.class);
            LiveData<List<QuizOld>> allQuizOldLiveData =
                    syncAll("quizOld", QuizOld.class);
            LiveData<List<Quiz>> allQuizLiveData =
                    syncAll("quiz", Quiz.class);
            LiveData<List<Post>> allPostLiveData =
                    syncAll("post", Post.class);
            LiveData<List<Course>> allCourseLiveData =
                    syncAll("course", Course.class);
            LiveData<List<Comment>> allCommentLiveData =
                    syncAll("comment", Comment.class);
            LiveData<List<Issue>> allIssueLiveData =
                    syncAll("issue", Issue.class);
            LiveData<List<Timeslot>> allTimeslotLiveData =
                    syncAll("timeslot", Timeslot.class);
            LiveData<List<CounselorAvailability>> allCounselorAvailabilityLiveData =
                    syncAll("counselorAvailability", CounselorAvailability.class);
            LiveData<List<Badge>> allBadgeLiveData =
                    syncAll("badge", Badge.class);
        }

        public void syncPartialJoinByCounselorAvailability (String
        counselorId, OnCompleteListener < List < Appointment >> callback){
            fetchAvailability(counselorId, availabilitys -> {
                List<Task<QuerySnapshot>> tasks = new ArrayList<>();
                int batchSize = 10;
                for (int i = 0; i < ((List<String>) availabilitys).size(); i += batchSize) {
                    List<String> batch = ((List<String>) availabilitys).subList(i, Math.min(((List<String>) availabilitys).size(), i + batchSize));
                    tasks.add(firestore.collection("appointment").whereIn("counselorAvailabilityId", batch).get());
                }

                Tasks.whenAllComplete(tasks).addOnCompleteListener(allTasks -> {
                    List<Appointment> appointments = new ArrayList<>();
                    for (Task<?> task : tasks) {
                        if (task.isSuccessful()) {
                            QuerySnapshot snapshot = (QuerySnapshot) task.getResult();
                            for (DocumentSnapshot doc : snapshot.getDocuments()) {
                                appointments.add(doc.toObject(Appointment.class));
                            }
                        }
                    }
                    callback.onComplete(Tasks.forResult(appointments));
                });
            });
        }

        private void fetchAvailability (String
                    counselorId, OnCompleteListener < List < String >> callback){
            firestore.collection("courseAvailability")
                    .whereEqualTo("counselorId", counselorId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> counselorAvailabilityIds = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                counselorAvailabilityIds.add(document.getId());
                            }
                            callback.onComplete(Tasks.forResult(counselorAvailabilityIds));
                        } else {
                            Log.e("FirestoreSync", "Failed to fetch counselor availability for counselorId: " + counselorId);
                            callback.onComplete(Tasks.forResult(Collections.emptyList()));
                        }
                    });
        }

        //identifier = the id of counselor/user
        //compareWith = column in firestore to compare
        //tableName = which table you are referring
        public void syncPartialDocumentId (String identifier, String tableName){
            firestore.collection(tableName)
                    .document(identifier)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Object currentObject = documentSnapshot.toObject(getDomainClass(tableName));
                            if (currentObject != null) {
                                Executor.executeTask(() -> insertDataToDatabase(tableName, currentObject));
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync data for table: " + tableName, e));
        }

        public void syncPartialRoleId (String identifier, String compareWith, String tableName){
            firestore.collection(tableName)
                    .whereEqualTo(compareWith, identifier)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<?> resultList = querySnapshot.toObjects(getDomainClass(tableName));
                        Executor.executeTask(() -> {
                            insertDataToDatabase(tableName, resultList);
                        });
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync data for table: " + tableName, e));
        }

        private Class<?> getDomainClass (String tableName){
            switch (tableName) {
                //for role syncing
                case "user":
                    return User.class;
                case "counselor":
                    return Counselor.class;
                //for non-role syncing
                case "achievement":
                    return Achievement.class;
                case "appointment":
                    return Appointment.class;
                case "chatHistory":
                    return ChatHistory.class;
                case "collection":
                    return Collection.class;
                case "counselorAvailability":
                    return CounselorAvailability.class;
                case "domainInterested":
                    return DomainInterested.class;
                case "folder":
                    return Folder.class;
                case "mediaRead":
                    return MediaRead.class;
                case "media":
                    return Media.class;
                case "mediaSet":
                    return MediaSet.class;
                case "notification":
                    return Notification.class;
                case "quizHistory":
                    return QuizHistory.class;
                case "quizResult":
                    return QuizResult.class;
                case "quiz":
                    return Quiz.class;
                case "userHistory":
                    return UserHistory.class;
                case "verEducator":
                    return VerEducator.class;
                default:
                    return null; // Handle default case if needed
            }
        }

        private void insertDataToDatabase (String tableName, Object currentObject){
            switch (tableName) {
                case "user":
                    database.userDao().insert((User) currentObject);
                    break;
                case "counselor":
                    database.counselorDao().insert((Counselor) currentObject);
                    break;
                case "achievement":
                    database.achievementDao().insert((Achievement) currentObject);
                    break;
                case "appointment":
                    database.appointmentDao().insert((Appointment) currentObject);
                    break;
                case "badge":
                    database.badgeDao().insert((Badge) currentObject);
                    break;
                case "chatHistory":
                    database.chatHistoryDao().insert((ChatHistory) currentObject);
                    break;
                case "collection":
                    database.collectionDao().insert((Collection) currentObject);
                    break;
                case "comment":
                    database.commentDao().insert((Comment) currentObject);
                    break;
                case "counselorAvailability":
                    database.counselorAvailabilityDao().insert((CounselorAvailability) currentObject);
                    break;
                case "course":
                    database.courseDao().insert((Course) currentObject);
                    break;
                case "domain":
                    database.domainDao().insert((Domain) currentObject);
                    break;
                case "domainInterested":
                    database.domainInterestedDao().insert((DomainInterested) currentObject);
                    break;
                case "folder":
                    database.folderDao().insert((Folder) currentObject);
                    break;
                case "helpdesk":
                    database.helpdeskDao().insert((Helpdesk) currentObject);
                    break;
                case "issue":
                    database.issueDao().insert((Issue) currentObject);
                    break;
                case "media":
                    database.mediaDao().insert((Media) currentObject);
                    break;
                case "mediaRead":
                    database.mediaReadDao().insert((MediaRead) currentObject);
                    break;
                case "mediaSet":
                    database.mediaSetDao().insert((MediaSet) currentObject);
                    break;
                case "notification":
                    database.notificationDao().insert((Notification) currentObject);
                    break;
                case "post":
                    database.postDao().insert((Post) currentObject);
                    break;
                case "questionOption":
                    database.questionOptionDao().insert((QuestionOption) currentObject);
                    break;
                case "quiz":
                    database.quizDao().insert((Quiz) currentObject);
                    break;
                case "quizHistory":
                    database.quizHistoryDao().insert((QuizHistory) currentObject);
                    break;
                case "quizOld":
                    database.quizOldDao().insert((QuizOld) currentObject);
                    break;
                case "quizQuestion":
                    database.quizQuestionDao().insert((QuizQuestion) currentObject);
                    break;
                case "quizResult":
                    database.quizResultDao().insert((QuizResult) currentObject);
                    break;
                case "staff":
                    database.staffDao().insert((Staff) currentObject);
                    break;
                case "timeslot":
                    database.timeslotDao().insert((Timeslot) currentObject);
                    break;
                case "userHistory":
                    database.userHistoryDao().insert((UserHistory) currentObject);
                    break;
                case "verEducator":
                    database.verEducatorDao().insert((VerEducator) currentObject);
                    break;
                case "verPost":
                    database.verPostDao().insert((VerPost) currentObject);
                    break;
                default:
                    break;
            }
        }
        private void insertDataToDatabase (String tableName, List < ?>data){
            switch (tableName) {
                case "achievement":
                    database.achievementDao().insertAll((List<Achievement>) data);
                    break;
                case "appointment":
                    database.appointmentDao().insertAll((List<Appointment>) data);
                    break;
                case "badge":
                    database.badgeDao().insertAll((List<Badge>) data);
                    break;
                case "chatHistory":
                    database.chatHistoryDao().insertAll((List<ChatHistory>) data);
                    break;
                case "collection":
                    database.collectionDao().insertAll((List<Collection>) data);
                    break;
                case "comment":
                    database.commentDao().insertAll((List<Comment>) data);
                    break;
                case "counselor":
                    database.counselorDao().insertAll((List<Counselor>) data);
                    break;
                case "counselorAvailability":
                    database.counselorAvailabilityDao().insertAll((List<CounselorAvailability>) data);
                    break;
                case "course":
                    database.courseDao().insertAll((List<Course>) data);
                    break;
                case "domain":
                    database.domainDao().insertAll((List<Domain>) data);
                    break;
                case "domainInterested":
                    database.domainInterestedDao().insertAll((List<DomainInterested>) data);
                    break;
                case "folder":
                    database.folderDao().insertAll((List<Folder>) data);
                    break;
                case "helpdesk":
                    database.helpdeskDao().insertAll((List<Helpdesk>) data);
                    break;
                case "issue":
                    database.issueDao().insertAll((List<Issue>) data);
                    break;
                case "media":
                    database.mediaDao().insertAll((List<Media>) data);
                    break;
                case "mediaRead":
                    database.mediaReadDao().insertAll((List<MediaRead>) data);
                    break;
                case "mediaSet":
                    database.mediaSetDao().insertAll((List<MediaSet>) data);
                    break;
                case "notification":
                    database.notificationDao().insertAll((List<Notification>) data);
                    break;
                case "post":
                    database.postDao().insertAll((List<Post>) data);
                    break;
                case "questionOption":
                    database.questionOptionDao().insertAll((List<QuestionOption>) data);
                    break;
                case "quiz":
                    database.quizDao().insertAll((List<Quiz>) data);
                    break;
                case "quizHistory":
                    database.quizHistoryDao().insertAll((List<QuizHistory>) data);
                    break;
                case "quizOld":
                    database.quizOldDao().insertAll((List<QuizOld>) data);
                    break;
                case "quizQuestion":
                    database.quizQuestionDao().insertAll((List<QuizQuestion>) data);
                    break;
                case "quizResult":
                    database.quizResultDao().insertAll((List<QuizResult>) data);
                    break;
                case "staff":
                    database.staffDao().insertAll((List<Staff>) data);
                    break;
                case "timeslot":
                    database.timeslotDao().insertAll((List<Timeslot>) data);
                    break;
                case "user":
                    database.userDao().insertAll((List<User>) data);
                    break;
                case "userHistory":
                    database.userHistoryDao().insertAll((List<UserHistory>) data);
                    break;
                case "verEducator":
                    database.verEducatorDao().insertAll((List<VerEducator>) data);
                    break;
                case "verPost":
                    database.verPostDao().insertAll((List<VerPost>) data);
                    break;
                default:
                    Log.e("FirestoreSync", "Unknown table name: " + tableName);
                    break;
            }
        }

        public void syncPartialJoinByPostVerEducator (String userId, String
        tableName, OnCompleteListener < List < ?>>callback){

            // Step 1: Fetch posts by userId to get the list of IDs for the join
            fetchUserPosts(userId, postIds -> {
                if (!((List<String>) postIds).isEmpty()) {
                    // Step 2: Depending on the tableName, fetch the corresponding data (VerPost, MediaSet, etc.)
                    switch (tableName) {
                        case "verPost":
                            // Fetch VerPosts by postIds
                            getVerPostsByPostIds((List<String>) postIds, callback);
                            break;

                        case "mediaSet":
                            // Extract MediaSet IDs from the posts and fetch MediaSets
                            List<String> mediaSetIds = extractMediaSetIdsFromPosts((List<String>) postIds);
                            fetchUserVerEducators(userId, mediaSetId -> {
                                mediaSetIds.addAll((List<String>) mediaSetId);
                            });
                            if (!mediaSetIds.isEmpty()) {
                                getMediaSetsByIds(mediaSetIds, callback);
                            }
                            break;

                        // Add more cases for other tables as needed, e.g., "userHistory", "chatHistory", etc.
                        default:
                            Log.e("FirestoreSync", "Unsupported table: " + tableName);
                            callback.onComplete(Tasks.forResult(Collections.emptyList()));
                            break;
                    }
                } else {
                    // If no posts exist for the user, return an empty list
                    callback.onComplete(Tasks.forResult(Collections.emptyList()));
                }
            });
        }

        private void fetchUserPosts (String userId, OnCompleteListener < List < String >> callback){
            firestore.collection("post")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> postIds = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                postIds.add(document.getId()); // Collect post IDs
                            }
                            callback.onComplete(Tasks.forResult(postIds));
                        } else {
                            Log.e("FirestoreSync", "Failed to fetch posts for userId: " + userId);
                            callback.onComplete(Tasks.forResult(Collections.emptyList()));
                        }
                    });
        }

        private void fetchUserVerEducators (String
        userId, OnCompleteListener < List < String >> callback){
            firestore.collection("verEducator")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            List<String> mediaSetIds = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                mediaSetIds.add(document.toObject(VerEducator.class).getFileSetId());
                                mediaSetIds.add(document.toObject(VerEducator.class).getImageSetId()); // Collect post IDs
                            }
                            callback.onComplete(Tasks.forResult(mediaSetIds));
                        } else {
                            Log.e("FirestoreSync", "Failed to fetch posts for userId: " + userId);
                            callback.onComplete(Tasks.forResult(Collections.emptyList()));
                        }
                    });
        }

        private List<String> extractMediaSetIdsFromPosts (List < String > postIds) {
            List<String> mediaSetIds = new ArrayList<>();

            // Iterate over post IDs and fetch relevant MediaSetIds (imageSetId, videoSetId, fileSetId)
            for (String postId : postIds) {
                firestore.collection("post").document(postId).get()
                        .addOnSuccessListener(document -> {
                            String imageSetId = document.getString("imageSetId");
                            String videoSetId = document.getString("videoSetId");
                            String fileSetId = document.getString("fileSetId");

                            if (imageSetId != null) mediaSetIds.add(imageSetId);
                            if (videoSetId != null) mediaSetIds.add(videoSetId);
                            if (fileSetId != null) mediaSetIds.add(fileSetId);
                        })
                        .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to extract MediaSetIds from post"));
            }

            return mediaSetIds;
        }

        private void getVerPostsByPostIds (List < String > postIds, OnCompleteListener < List < ?>>
        callback){
            List<Task<QuerySnapshot>> tasks = new ArrayList<>();

            // Divide postIds into batches of 10
            int batchSize = 10;
            for (int i = 0; i < postIds.size(); i += batchSize) {
                List<String> batch = postIds.subList(i, Math.min(postIds.size(), i + batchSize));
                Task<QuerySnapshot> task = firestore.collection("verPost")
                        .whereIn("postId", batch)
                        .get();
                tasks.add(task);
            }

            // Combine all tasks into one
            Tasks.whenAllComplete(tasks).addOnCompleteListener(allTasks -> {
                List<VerPost> allVerPosts = new ArrayList<>();
                for (Task<?> task : tasks) {
                    if (task.isSuccessful() && task.getResult() instanceof QuerySnapshot) {
                        QuerySnapshot snapshot = (QuerySnapshot) task.getResult();
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            allVerPosts.add(doc.toObject(VerPost.class));
                        }
                    }
                }
                callback.onComplete(Tasks.forResult(allVerPosts)); // Pass the combined results to the callback
            });
        }

        private void getMediaSetsByIds (List < String > mediaSetIds, OnCompleteListener < List < ?>>
        callback){
            List<Task<QuerySnapshot>> tasks = new ArrayList<>();

            // Perform batched queries (limit the batch size to avoid overloading)
            int batchSize = 10;  // Adjust batch size as needed
            for (int i = 0; i < mediaSetIds.size(); i += batchSize) {
                List<String> batch = mediaSetIds.subList(i, Math.min(mediaSetIds.size(), i + batchSize));
                Task<QuerySnapshot> task = firestore.collection("mediaSet")
                        .whereIn("mediaSetId", batch)
                        .get();
                tasks.add(task);
            }

            // Handle all queries once completed
            Tasks.whenAllComplete(tasks).addOnCompleteListener(allTasks -> {
                List<MediaSet> mediaSets = new ArrayList<>();
                for (Task<?> task : tasks) {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = (QuerySnapshot) task.getResult();
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            mediaSets.add(doc.toObject(MediaSet.class));  // Add the MediaSet object to the list
                        }
                    } else {
                        Log.e("FirestoreSync", "Error fetching media sets");
                    }
                }

                callback.onComplete(Tasks.forResult(mediaSets));  // Pass the combined results to the callback
            });
        }

        public <T > LiveData < List < T >> syncAll(String tableName, Class < T > modelClass) {
            MutableLiveData<List<T>> liveData = new MutableLiveData<>();

            firestore.collection(tableName)
                    .addSnapshotListener((querySnapshot, e) -> {
                        if (e != null) {
                            Log.e("FirestoreSync", "Listen failed.", e);
                            return;
                        }

                        if (querySnapshot != null) {
                            List<T> dataList = querySnapshot.toObjects(modelClass);
                            liveData.setValue(dataList); // Update LiveData for UI

                            // Update local database for offline access
                            Executor.executeTask(() -> {
                                insertDataToDatabase(tableName, dataList);
                            });
                        }
                    });

            return liveData;
        }

    public <T> void addFirestoreListener(
            String collectionName,
            Class<T> modelClass,
            Consumer<T> onAdded,
            Consumer<T> onModified,
            Consumer<T> onRemoved) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionName)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Error listening to changes in " + collectionName, error);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    T addedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() -> onAdded.accept(addedItem));
                                    break;

                                case MODIFIED:
                                    T modifiedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() -> onModified.accept(modifiedItem));
                                    break;

                                case REMOVED:
                                    T removedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() ->onRemoved.accept(removedItem));
                                    break;
                            }
                        }
                    }
                });
    }

    public <T> void addPartialFirestoreListener(
            String collectionName,
            Class<T> modelClass,
            Consumer<T> onAdded,
            Consumer<T> onModified,
            Consumer<T> onRemoved) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionName)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore", "Error listening to changes in " + collectionName, error);
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    T addedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() -> onAdded.accept(addedItem));
                                    break;

                                case MODIFIED:
                                    T modifiedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() -> onModified.accept(modifiedItem));
                                    break;

                                case REMOVED:
                                    T removedItem = documentChange.getDocument().toObject(modelClass);
                                    Executor.executeTask(() ->onRemoved.accept(removedItem));
                                    break;
                            }
                        }
                    }
                });
    }

    public void initializeFirestoreListenersUser() {
        //post
        addFirestoreListener(
                "post",
                Post.class,
                database.postDao()::insert, // Handle add and update operations
                database.postDao()::update,
                database.postDao()::delete
//                postId -> database.postDao.deleteById(postId) // Use custom deleteById method
        );
        //domain
        addFirestoreListener(
                "domain",
                Domain.class,
                database.domainDao()::insert,
                database.domainDao()::update,
                database.domainDao()::delete
        );
        //questionOption
        addFirestoreListener(
                "questionOption",
                QuestionOption.class,
                database.questionOptionDao()::insert,
                database.questionOptionDao()::update,
                database.questionOptionDao()::delete
        );
        //quizQuestion
        addFirestoreListener(
                "quizQuestion",
                QuizQuestion.class,
                database.quizQuestionDao()::insert,
                database.quizQuestionDao()::update,
                database.quizQuestionDao()::delete
        );
        //quizOld
        addFirestoreListener(
                "quizOld",
                QuizOld.class,
                database.quizOldDao()::insert,
                database.quizOldDao()::update,
                database.quizOldDao()::delete
        );
        //course
        addFirestoreListener(
                "course",
                Course.class,
                database.courseDao()::insert,
                database.courseDao()::update,
                database.courseDao()::delete
        );
        //comment
        addFirestoreListener(
                "comment",
                Comment.class,
                database.commentDao()::insert,
                database.commentDao()::update,
                database.commentDao()::delete
        );
        //issue
        addFirestoreListener(
                "issue",
                Issue.class,
                database.issueDao()::insert,
                database.issueDao()::update,
                database.issueDao()::delete
        );
        //timeslot
        addFirestoreListener(
                "timeslot",
                Timeslot.class,
                database.timeslotDao()::insert,
                database.timeslotDao()::update,
                database.timeslotDao()::delete
        );
        //counselorAvailability
        addFirestoreListener(
                "counselorAvailability",
                CounselorAvailability.class,
                database.counselorAvailabilityDao()::insert,
                database.counselorAvailabilityDao()::update,
                database.counselorAvailabilityDao()::delete
        );
        //badge
        addFirestoreListener(
                "badge",
                Badge.class,
                database.badgeDao()::insert,
                database.badgeDao()::update,
                database.badgeDao()::delete
        );
        //quiz
        addFirestoreListener(
                "quiz",
                Quiz.class,
                database.quizDao()::insert,
                database.quizDao()::update,
                database.quizDao()::delete
        );
    }

    public void initializeFirestoreListenersStaff() {
        initializeFirestoreListenersUser();
        //user
        addFirestoreListener(
                "user",
                User.class,
                database.userDao()::insert,
                database.userDao()::update,
                database.userDao()::delete
        );
        //domainInterested
        addFirestoreListener(
                "domainInterested",
                DomainInterested.class,
                database.domainInterestedDao()::insert,
                database.domainInterestedDao()::update,
                database.domainInterestedDao()::delete
        );
        //verEducator
        addFirestoreListener(
                "verEducator",
                VerEducator.class,
                database.verEducatorDao()::insert,
                database.verEducatorDao()::update,
                database.verEducatorDao()::delete
        );
        //quizHistory
        addFirestoreListener(
                "quizHistory",
                QuizHistory.class,
                database.quizHistoryDao()::insert,
                database.quizHistoryDao()::update,
                database.quizHistoryDao()::delete
        );
        //quizResult
        addFirestoreListener(
                "quizResult",
                QuizResult.class,
                database.quizResultDao()::insert,
                database.quizResultDao()::update,
                database.quizResultDao()::delete
        );
        //collection
        addFirestoreListener(
                "collection",
                Collection.class,
                database.collectionDao()::insert,
                database.collectionDao()::update,
                database.collectionDao()::delete
        );
        //folder
        addFirestoreListener(
                "folder",
                Folder.class,
                database.folderDao()::insert,
                database.folderDao()::update,
                database.folderDao()::delete
        );
        //userHistory
        addFirestoreListener(
                "userHistory",
                UserHistory.class,
                database.userHistoryDao()::insert,
                database.userHistoryDao()::update,
                database.userHistoryDao()::delete
        );
        //mediaRead
        addFirestoreListener(
                "mediaRead",
                MediaRead.class,
                database.mediaReadDao()::insert,
                database.mediaReadDao()::update,
                database.mediaReadDao()::delete
        );
        //appointment
        addFirestoreListener(
                "appointment",
                Appointment.class,
                database.appointmentDao()::insert,
                database.appointmentDao()::update,
                database.appointmentDao()::delete
        );
        //achievement
        addFirestoreListener(
                "achievement",
                Achievement.class,
                database.achievementDao()::insert,
                database.achievementDao()::update,
                database.achievementDao()::delete
        );
        //chatHistory
        addFirestoreListener(
                "chatHistory",
                ChatHistory.class,
                database.chatHistoryDao()::insert,
                database.chatHistoryDao()::update,
                database.chatHistoryDao()::delete
        );
        //notification
        addFirestoreListener(
                "notification",
                Notification.class,
                database.notificationDao()::insert,
                database.notificationDao()::update,
                database.notificationDao()::delete
        );
        //verPost
        addFirestoreListener(
                "verPost",
                VerPost.class,
                database.verPostDao()::insert,
                database.verPostDao()::update,
                database.verPostDao()::delete
        );
        //media
        addFirestoreListener(
                "media",
                Media.class,
                database.mediaDao()::insert,
                database.mediaDao()::update,
                database.mediaDao()::delete
        );
        //mediaSet
        addFirestoreListener(
                "mediaSet",
                MediaSet.class,
                database.mediaSetDao()::insert,
                database.mediaSetDao()::update,
                database.mediaSetDao()::delete
        );
        //helpdesk
        addFirestoreListener(
                "helpdesk",
                Helpdesk.class,
                database.helpdeskDao()::insert,
                database.helpdeskDao()::update,
                database.helpdeskDao()::delete
        );
        //counselor
        addFirestoreListener(
                "counselor",
                Counselor.class,
                database.counselorDao()::insert,
                database.counselorDao()::update,
                database.counselorDao()::delete
        );
        //staff
        addFirestoreListener(
                "staff",
                Staff.class,
                database.staffDao()::insert,
                database.staffDao()::update,
                database.staffDao()::delete
        );
    }

    public void initializeFirestoreListenersCounselor() {
        //timeslot
        addFirestoreListener(
                "timeslot",
                Timeslot.class,
                database.timeslotDao()::insert,
                database.timeslotDao()::update,
                database.timeslotDao()::delete
        );
    }
}