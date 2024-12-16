# Database Structure
For database, we mainly have
  - Entity, DAO and Database class for Room Database (created as single class)
  - Firestore syncing methods 
  - Cloudinary 

> [!NOTE]
> Room Database - Local Storage
> 
> Firestore - Syncing of Data
> 
> Cloudinary - Online Media Storage

# Room Database 
### Entity
Used to declare table, including the table name, fields(attributes), keys, relationship and contraints.

For each table, we will have 1 entity class.

### DAO 
Data Access Object, used to define the methods to access the table.

For each table, we will have 1 DAO interface.

### Database
I created this as AppDatabase class, to be used to define methods to build the database and perform migration.



# Migration
Migration is done to enforce the constraints in Room Database, constraints like NOT NULL, DEFAULT are not act as the meta information of the table by using the Room entity class to declare it.

We will need to perform migration to replace the existing table with the a table created under SQLite. This way, I can also use CHECK constraints for some operation.

Besides enforcing constraints, I've added codes for creating trigger to perform check upon the insertion of data to a table. Here is an example of creating trigger using migration.
```
database.execSQL(
                    "CREATE TRIGGER check_unique_username_update " +
                            "BEFORE UPDATE ON user " +
                            "WHEN NEW.name != 'bookworm' AND OLD.name != NEW.name " + // Only check when username changes
                            "BEGIN " +
                            "    SELECT RAISE(FAIL, 'Username already exists') " +
                            "    WHERE (SELECT COUNT(*) FROM user WHERE name = NEW.name) > 0; " +
                            "END;"
            );
```
> [!NOTE]
> After we run the migration (as attached along when building the database), we need to change the version of database used in AppDatabase class.

Initially,
```
@Database(entities = {Achievement.class, Appointment.class, Badge.class, ChatHistory.class,
        Collection.class, Comment.class, Counselor.class, CounselorAvailability.class,
        Domain.class, DomainInterested.class, Folder.class, Helpdesk.class, Issue.class,
        MediaRead.class, MediaSet.class, Notification.class, Post.class, 
        QuestionOption.class, QuizHistory.class, QuizOld.class, QuizQuestion.class, 
        QuizResult.class, Staff.class, Timeslot.class, User.class, UserHistory.class, 
        VerEducator.class, VerPost.class}, version = 1)
```
Change to 
```
@Database(entities = {Achievement.class, Appointment.class, Badge.class, ChatHistory.class,
        Collection.class, Comment.class, Counselor.class, CounselorAvailability.class,
        Domain.class, DomainInterested.class, Folder.class, Helpdesk.class, Issue.class,
        MediaRead.class, MediaSet.class, Notification.class, Post.class, 
        QuestionOption.class, QuizHistory.class, QuizOld.class, QuizQuestion.class, 
        QuizResult.class, Staff.class, Timeslot.class, User.class, UserHistory.class, 
        VerEducator.class, VerPost.class}, version = 2)
```

# Inserting, Updating and Querying in Room Database
## Accessing the database instance 
```
private AppDatabase database;
database = AppDatabase.getDatabase(this);
```

## Accessing the DAO instance
```
UserDao userDao = database.userDao();
```

## Using DAO methods in the DAO class

For example, you would like to insert a new user into the table 'user',

in the 'UserDao', you have insertAppointment() method,
```
    @Insert
    void insertUser(User user);
```

put the insertUser() method into the activity class
```
addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String ageText = ageInput.getText().toString().trim();

            if (name.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter both name and age", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Age must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            addUser(name, age);

        });


private void addUser(String name, int age) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = new User(name, age);
            User user2 = new User(name,age);

            try {
                userDao.insertUser(user);
                userDao.insertUser(user2);
            } catch (SQLiteConstraintException e) {
                Log.e("DB_ERROR", "Duplicate username: " + e.getMessage());
            }
            firestore.collection("users")
                    .add(user) // Automatically generates a unique document ID
                    .addOnSuccessListener(documentReference ->
                            Log.d("Firestore", "User added with ID: " + documentReference.getId()))
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Failed to add user", e));

            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();
                clearInputs();
            });
        });
    }
```

We will need to have addUser() method as we need to push the same User instance(for here the instance we use is based on the entity class created in Room for the table) to Firestore.

## Using INNER JOIN / OUTER JOIN For Query
I didn't cover INNER JOIN or OUTER JOIN in my last commit, so if you would like to use inner join or outer join(left join/right join) for querying, you need to perform the following steps.

### 1. Add a method in the DAO class 
Here's an example for INNER JOIN:

For example, you want to retrieve appointments along with the user's name. 

In the AppointmentDao Interface, add the following codes

```
    @Query("SELECT a.appointmentId, a.date, u.name " +
           "FROM appointment AS a " +
           "INNER JOIN user AS u ON a.userId = u.userId " +
           "WHERE u.name LIKE :userName")
    LiveData<List<UserAppointment>> getAppointmentsByUserName(String userName);
```
### 2. Create a Data Class for Result
You need a data class to hold the combined results of the query for better access

```
public class UserAppointment {
    public String appointmentId;
    public String date;
    public String name;

    // Constructor
    public UserAppointment(String appointmentId, String date, String name) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.name = name;
    }
    //getters, and setters...
}
```

Room automatically maps the result to the UserAppointment data class.

When this query runs, Room looks at the query result columns (appointmentId, date, name) and tries to match them to the fields in the UserAppointment class.

> [!NOTE]
> The column names in the query must match the field names in the UserAppointment class (or you can use Room's @ColumnInfo annotation to customize the mapping if they differ).


Using Room's @ColumnInfo annotation,
Dao method
```
@Query("SELECT a.appointmentId AS appointment_id, a.date AS appointment_date, u.name AS user_name" +
          "FROM appointment AS a " +
          "INNER JOIN user AS u ON a.userId = u.userId " +
          "WHERE u.name LIKE :userName")
    LiveData<List<UserAppointment>> getAppointmentsByUserName(String userName);
```
Updated Data Class
```
public class UserAppointment {

    @ColumnInfo(name = "appointment_id")
    public String appointmentId; // Maps to "appointment_id" in the query result.

    @ColumnInfo(name = "appointment_date")
    public String date; // Maps to "appointment_date" in the query result.

    @ColumnInfo(name = "user_name")
    public String name; // Maps to "user_name" in the query result.

    // Constructor
    public UserAppointment(String appointmentId, String date, String name) {
        this.appointmentId = appointmentId;
        this.date = date;
        this.name = name;
    }
}
```
## Data Deletion
```
    try {
            roomDatabase.userDao().deleteUser(currentUserId); // Clean old user data
            roomDatabase.postDao().deletePostsByUser(currentUserId); // Clean old user posts
        } catch (SQLiteConstraintException e) {
              Log.e("DB_ERROR", "Failed to delete data: " + e.getMessage());
        }
```
    
# Firestore
Here are some common methods on handling data in Firestore.
## Data Insertion to Firestore
with the documentId set to be the userId
```
User newUser = new User("userId123", "John Doe", "email@example.com");
// Example Firestore save
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("users").document(newUser.getUserId())
    .set(newUser)
    .addOnSuccessListener(doc -> Log.d("Firestore", "User added"))
    .addOnFailureListener(e -> Log.e("Firestore", "Error adding user", e));
```
with the documentId auto generated
```
firestore.collection("users")
        .add(newUser) // Firestore will auto-generate a unique document ID
        .addOnSuccessListener(documentReference -> {
            Log.d("Firestore", "Document added with ID: " + documentReference.getId());
        })
        .addOnFailureListener(e -> Log.e("Firestore", "Error adding document", e));
```
## Data Retrieval from Firestore
### Fetch single entry based on the id (document id in Firestore)
```
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("users").document(userId)
    .get()
    .addOnSuccessListener(documentSnapshot -> {
        User user = documentSnapshot.toObject(User.class);
        if (user != null) {
            // Save to Room
            userDao.insert(user);
        }
    });
    .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync users", e));
```
### Fecth all data into a list 
```
FirebaseFirestore.getInstance()
    .collection("users")
    .get()
    .addOnSuccessListener(querySnapshot -> {
        List<User> users = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            User user = doc.toObject(User.class); // Map Firestore document to User entity
            if (user != null) {
                users.add(user);
            }
        }
        // Insert all users into Room database
        Executors.newSingleThreadExecutor().execute(() -> {
            roomDatabase.userDao().insertOrUpdateUsers(users);//insert all one by one
        //or use insertAll(posts) at one time
        roomDatabase.userDao().insertAll(users)
        });
    })
    .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync users", e));
```
### Fetch all data using whereEqualTo to filter data by userId etc.
```
FirebaseFirestore.getInstance()
    .collection("posts")
    .whereEqualTo("userId", currentUserId) // Fetch posts belonging to the current user
    .get()
    .addOnSuccessListener(querySnapshot -> {
        List<Post> userPosts = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
            Post post = doc.toObject(Post.class);
            if (post != null) {
                userPosts.add(post);
            }
        }
        // Update Room database with the current user's posts
        Executors.newSingleThreadExecutor().execute(() -> {
            roomDatabase.postDao().insertOrUpdatePosts(userPosts);
        });
    })
    .addOnFailureListener(e -> Log.e("FirestoreSync", "Failed to sync user posts", e));

```
### Fetching data when join is needed. For example, verPost and post 
1. Retrieve Posts of the User
- Use Firestore to fetch the user's posts based on their userId.

```
FirebaseFirestore db = FirebaseFirestore.getInstance();

public void getUserPosts(String userId, OnCompleteListener<QuerySnapshot> callback) {
    db.collection("post")
        .whereEqualTo("userId", userId)
        .get()
        .addOnCompleteListener(callback);
}
```

2. Query verPost Using the postIds
- Once you have the postIds from the previous query, fetch the related verPost records.

Common approach
```
public void getVerPostsByPostIds(List<String> postIds, OnCompleteListener<QuerySnapshot> callback) {
    db.collection("verPost")
        .whereIn("postId", postIds) // Firestore allows up to 10 IDs per query
        .get()
        .addOnCompleteListener(callback);
}
```
> [!CAUTION]
> For more than 10 postIds, you might need to batch the queries or use OR logic (Firestore currently does not natively support OR in the same query).

Approach Using Batching Queries Method
```
public void getVerPostsByPostIds(List<String> postIds, OnCompleteListener<List<VerPost>> callback) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Task<QuerySnapshot>> tasks = new ArrayList<>();
    
    // Divide postIds into batches of 10
    int batchSize = 10;
    for (int i = 0; i < postIds.size(); i += batchSize) {
        List<String> batch = postIds.subList(i, Math.min(postIds.size(), i + batchSize));
        Task<QuerySnapshot> task = db.collection("verPost")
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
        // Pass the combined results to the callback
        callback.onComplete(allVerPosts);
    });
}
```
Here's how to use it,
```
List<String> postIds = Arrays.asList("post1", "post2", "post3", ...); // Your post IDs

getVerPostsByPostIds(postIds, verPosts -> {
    if (verPosts != null) {
        // Insert the fetched VerPost records into Room
        Executors.newSingleThreadExecutor().execute(() -> {
            VerPostDao verPostDao = MyDatabase.getInstance(context).verPostDao(); // Replace with your DB instance getter
            verPostDao.insertVerPosts(verPosts); // Assumes you have a DAO method to insert a list of VerPosts
            Log.d("Room", "VerPosts inserted into Room database");
        });
    } else {
        Log.e("Firestore", "Error fetching VerPosts");
    }
});
```

When you call getVerPostsByPostIds, the method:

1. Splits the postIds into batches of 10.
2. Executes a query for each batch, generating multiple Task<QuerySnapshot> objects.
3. Uses Tasks.whenAllComplete() to wait until all these Task instances finish.
4. Processes the results once all tasks are complete.
> You only need to define a callback to handle the final combined result.




## Data Updating 
```
// Update Room
userDao.update(user);

// Update Firestore
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("users").document(user.getUserId())
    .set(user)
    .addOnSuccessListener(doc -> Log.d("Firestore", "User updated"))
    .addOnFailureListener(e -> Log.e("Firestore", "Error updating user", e));
```

## Syncing from Firestore to Room (SnapShotListener)
- Save posts locally in Room for offline access.
- Fetch posts from Firestore and periodically sync them with Room.


For insertion of data only,
```
FirebaseFirestore db = FirebaseFirestore.getInstance();
db.collection("posts").addSnapshotListener((querySnapshot, e) -> {
    if (e != null) {
        Log.e("Firestore", "Listen failed.", e);
        return;
    }
    if (querySnapshot != null) {
        List<Post> posts = new ArrayList<>();
        for (DocumentSnapshot doc : querySnapshot) {
            posts.add(doc.toObject(Post.class));
        }
        // Save to Room
        postDao.insertAll(posts);
    }
});
```
another example
```
FirebaseFirestore.getInstance()
    .collection("users")
    .document(currentUserId)
    .addSnapshotListener((documentSnapshot, e) -> {
        if (e != null) {
            Log.e("FirestoreSync", "Listen failed.", e);
            return;
        }

        if (documentSnapshot != null && documentSnapshot.exists()) {
            User currentUser = documentSnapshot.toObject(User.class);
            if (currentUser != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    roomDatabase.userDao().insertOrUpdateUser(currentUser);
                });
            }
        }
    });
```
For insertion, updates and deletion on the data in Firestore,
> [!NOTE]
> This process only needs to be done for the tables that users have full access to. 
```
db.collection("posts")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Log.e("Firestore", "Error listening to changes", error);
                        return;
                    }

                    // Loop through the changes (added, modified, removed)
                    if (value != null) {
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    // New post added, insert it into Room
                                    Post post = documentChange.getDocument().toObject(Post.class);
                                    insertPostIntoRoom(post);
                                    break;

                                case MODIFIED:
                                    // Post modified, update it in Room
                                    Post updatedPost = documentChange.getDocument().toObject(Post.class);
                                    updatePostInRoom(updatedPost);
                                    break;

                                case REMOVED:
                                    // Post removed, delete it from Room
                                    String postId = documentChange.getDocument().getId();
                                    deletePostFromRoom(postId);
                                    break;
                            }
                        }
                    }
                }
            });
```

## Data Deletion
```
// Delete the post from Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("posts")
        .document(postId)
        .delete()
        .addOnSuccessListener(aVoid -> {
            Log.d("Firestore", "Post deleted successfully");
        })
        .addOnFailureListener(e -> {
            Log.e("Firestore", "Failed to delete post", e);
        });
```


# Access Level of Each Role to the Data
## User
### Partial Access (to her/his own data) 
- user 
- domainInterested
- verPost
- verEducator
- quizHistory
- quizResult
- mediaSet
- media
- collection
- folder
- userHistory
- mediaRead
- appointment
- achievement
- chatHistory
- notification

### Full Access
- domain
- questionOption
- quizQuestion
- quizOld
- post
- course
- comment 
- issue
- timeslot
- counselorAvailability
- badge
- quiz

### No Access
- helpdesk
- counselor
- staff


## Staff
FULL ACCESS to all tables

## Counselor
### Partial Access
- appointment 
- counselorAvailability
- counselor
- chatHistory

### Full Access
- timeslot

### No Access
- user 
- domainInterested
- verPost
- verEducator
- quizHistory
- quizResult
- mediaSet
- collection
- folder
- userHistory
- mediaRead
- achievement
- badge
- notification
- domain
- questionOption
- quizQuestion
- quizOld
- post
- course
- comment 
- issue
- helpdesk
- staff

# Flow of using Database
### 1. OnCreate - Retrieve data from Firestore

### 2. Sync user-specific data to the tables (according to the access level)

### 3. OnAction - Retrieve data from Room

### 4. Inserting and Updating data 
- check on the data using temporary to validate
- push to both real table and firestore

### 5. Deleting data 
- delete straightaway on the Room database 
- delete also in the Firestore 

>[!NOTE]
> SnapShotListener of Firestore is used to sync changes on Firestore to Room (updates by other users)

# Data Check before Data Insertion
Before inserting data into both Room and Firestore, we need to check if it obey the constraints we set for the table, 

## 1. Create the Temporary Database
- Create a SupportSQLiteDatabase or a temporary Room database designed only for validation.
- Define the schema with constraints for validation.

```
SupportSQLiteOpenHelper.Configuration config = SupportSQLiteOpenHelper.Configuration.builder(context)
        .name(null) // In-memory database, not persisted
        .callback(new SupportSQLiteOpenHelper.Callback(1) {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                // Define the table with constraints
                db.execSQL("CREATE TABLE temp_users (" +
                        "userId TEXT PRIMARY KEY, " +
                        "email TEXT NOT NULL, " +
                        "CHECK (email LIKE '%@%')" + // Example constraint
                        ");");
            }

            @Override
            public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {
                // No upgrades needed for temporary use
            }
        })
        .build();

SupportSQLiteOpenHelper helper = new FrameworkSQLiteOpenHelperFactory().create(config);
SupportSQLiteDatabase tempDb = helper.getWritableDatabase();
```
## 2. Populate Temporary Table with Firestore Data
- Fetch data from Firestore and insert it into the temporary table.

```
FirebaseFirestore firestore = FirebaseFirestore.getInstance();
firestore.collection("data")
    .get()
    .addOnSuccessListener(queryDocumentSnapshots -> {
        for (DocumentSnapshot document : queryDocumentSnapshots) {
            ContentValues values = new ContentValues();
            values.put("id", document.getId());
            values.put("email", document.getString("email"));
            values.put("name", document.getString("name"));

            // Insert into the temporary database
            tempDb.insert("temp_data", SQLiteDatabase.CONFLICT_ABORT, values);
        }
    });
```

## 3. Validate Data
- Perform validation or additional checks within the temporary table. If everything is valid, push the changes to Firestore.

```
ContentValues newData = new ContentValues();
newData.put("id", "123");
newData.put("email", "test@example.com");
newData.put("name", "Test User");

try {
    tempDb.insertOrThrow("temp_data", SQLiteDatabase.CONFLICT_ABORT, newData);

    // Push to Firestore if validation succeeds
    Map<String, Object> firestoreData = new HashMap<>();
    firestoreData.put("email", "test@example.com");
    firestoreData.put("name", "Test User");

    firestore.collection("data").document("123")
        .set(firestoreData)
        .addOnSuccessListener(aVoid -> Log.d("Firestore", "Data successfully written!"))
        .addOnFailureListener(e -> Log.e("Firestore", "Error writing document", e));
} catch (SQLiteConstraintException e) {
    Log.e("Validation", "Validation failed: " + e.getMessage());
}

```

## 4. Drop the Temporary Database
- After validation, close and release the temporary database resources.
```
  tempDb.execSQL("DROP TABLE IF EXISTS temp_data");
  helper.close(); 
```

# Methods to Access Database 
To use Room and Firestore, you need to insert the following codes in each activity class
```
//Initialize Room database 
private AppDatabase database;
database = AppDatabase.getDatabase(this);

//Initialize Firestore
private final RoomDatabase roomDatabase;
firestoreManager = new FirestoreManager (database);
```
To use DAO methods for querying, inserting, updating and deleting data, declare each DAO instance using the database instance
```
UserDao userDao = database.userDao();
DomainDao domainDao = database.domainDao();
... other DAOs you would like to use 
```

# Cloudinary
Cloudinary is the database for saving media, which are images, videos and PDF. For your information, maximum file size for image and PDF is 10MB per file, while maximum file size for video is 100MB (don't have to care about it, all the code for execption handling is implemented)

There are 2 Java classes for media database: CloudinaryUploadWorker and MediaHandler.

## CloudinaryUploadWorker
As uploading files(or media) need some time, so a worker is implemented so that the files will keep uploading and saved in the database even though the activity is stopped.

To utilise the worker, follow the following format in your class:

```
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class YourActivity extends AppCompatActivity implements MediaHandler.MediaHandleCallback {

    // Add the following view/element based on your activity's need
    // a. for image viewer
    private MediaHandler imageHandler;
    private ImageView imageView;
    private Button uploadImage;
    // b. for video viewer
    private PlayerView playerView;
    private ExoPlayer player;
    private MediaHandler videoHandler;
    private Button uploadVideo;
    // c. for PDF viewer
    private WebView webView; 
    private MediaHandler pdfHandler;
    private Button uploadPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);

        // Register the following based on your need (if your activity need upload media function) (if just wanna show media, these 3 are not needed):
        // a. Register the activity result launcher for image
        uploadImage = findViewById(R.id.uploadImage);
        imageView = findViewById(R.id.image);
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        mediaHandler.handleResult(imageUri, "image");
                    }
                }
        );
        imageHandler = new MediaHandler(this, imagePickerLauncher, this);
        // Trigger image selection
        uploadImage.setOnClickListener(v -> imageHandler.selectImage());

        // b. Register the activity result launcher for video
        uploadVideo = findViewById(R.id.uploadVideo);
        playerView = findViewById(R.id.playerView);
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        ActivityResultLauncher<Intent> videoPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri videoUri = result.getData().getData();
                        mediaHandler.handleResult(videoUri, "video");
                    }
                }
        );
        videoHandler = new MediaHandler(this, videoPickerLauncher, this);
        // Trigger video selection
        uploadVideo.setOnClickListener(v -> videoHandler.selectVideo());

        // c. Register the activity result launcher for pdf
        uploadPDF = findViewById(R.id.uploadPDF);
        webView = findViewById(R.id.webView);
        ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri pdfUri = result.getData().getData();
                        mediaHandler.handleResult(pdfUri, "pdf");
                    }
                }
        );
        pdfHandler = new MediaHandler(this, pdfPickerLauncher, this);
        // Trigger pdf selection
        uploadPDF.setOnClickListener(v -> videoHandler.selectVideo());
    }

    @Override
    public void onMediaSelected(Object filePath, String fileType) {
        // Handle the file path returned by the MediaHandler
        Toast.makeText(this, "Selected " + fileType + " path: " + filePath, Toast.LENGTH_LONG).show();
        if (fileType.equalsIgnoreCase("video")){
            videoHandler.uploadVideoInBackground((String) filePath, "YOUR_DATABASE", player);
        } else if (fileType.equalsIgnoreCase("image")){
            imageHandler.uploadImageInBackground((String) filePath, "YOUR_DATABASE", imageView);
        } else if (fileType.equalsIgnoreCase("pdf")){
            pdfHandler.uploadPdfInBackground((byte[]) filePath, "YOUR_DATABASE", webView)
        }
    }

    // If you are using the video player, implement the following so that the video will stop
    @Override
    protected void onStart() {
        super.onStart();
        if (player != null)
            player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}

```
# MsdiaHandler
To display images, PDFs, and video, use the following method:
```
i. MediaHandler.displayImage(Context context, String imageUrl, ImageView imageView) ;
ii. MediaHandler.displayPDF(String pdfUrl, WebView webView)
iii. MediaHandler.playVideo(String videoUrl, ExoPlayer player)
```
