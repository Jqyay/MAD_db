package com.example.madguardians.database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.madguardians.R;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryUploadWorker extends Worker {
    private Cloudinary cloudinary;
    private NotificationManager notificationManager;
    private Context context;
    private String progress = "Uploading video...";
    private final int NOTIFICATION_ID = 1;
    private static boolean isStoped = false;

    public CloudinaryUploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", context.getString(R.string.cloudinary_cloud_name));
        config.put("api_key", context.getString(R.string.cloudinary_api_key));
        config.put("api_secret", context.getString(R.string.cloudinary_api_secret));

        cloudinary = new Cloudinary(config);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        isStoped = false;

//        // Initialize Room database and DAO
//        AppDatabase db = AppDatabase.getInstance(context);
//        videoDao = db.videoDao();
    }

    @NonNull
    @Override
    public Result doWork() {
//        setForegroundAsync(showNotification(progress));
        String filePath = getInputData().getString("filePath");
        String fileType = getInputData().getString("fileType");
        Log.d("TAG", "filePath: " + filePath);
        Log.d("TAG", "fileType: " + fileType);
//        String database = getInputData().getString("database");

        try {
            Map response = null;
            // Perform upload to Cloudinary
            if (fileType.equalsIgnoreCase("image") ) {
                response = cloudinary.uploader().upload(filePath, ObjectUtils.emptyMap());
            } else if (fileType.equalsIgnoreCase("video")) {
                response = cloudinary.uploader().upload(filePath, ObjectUtils.asMap("resource_type", "video"));
            } else if (fileType.equalsIgnoreCase("pdf")) {
                File file = new File(filePath);
                byte[] bytes = Files.readAllBytes(file.toPath());
                response = cloudinary.uploader().upload(bytes, ObjectUtils.asMap("resource_type", "raw"));
            }
            Log.d("TAG", "doWork: here1");
            if (response != null) {
                Log.d("TAG", "doWork: here2");
                String uploadedUrl = (String) response.get("secure_url");
                if (uploadedUrl != null) {
                    Log.d("TAG", "doWork: here3");
                    Log.d("Cloudinary", uploadedUrl);
                    Data outputData = new Data.Builder().putString("uploadedUrl", uploadedUrl).build();
                    return Result.success(outputData);
                } else
                    return Result.failure();
            }

//            // Save to Room Database (in background)
//            Video video = new Video();
//            video.setUrl(videoUrl);
//            video.setUploaded(true);
//            videoDao.insert(video); // Make sure this is a synchronous insert

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "doWork: here4");
            return Result.failure();
        }
        return Result.failure();
    }

//    private ForegroundInfo showNotification(String progress) {
//        return new ForegroundInfo(NOTIFICATION_ID, createNotification(progress));
//    }
//
//    private Notification createNotification(String progress) {
//        final String CHANNEL_ID = "100";
//        String title = "Foreground Notification";
//        String cancel = "Cancel";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(
//                    new NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_HIGH));
//        }
//
//        Notification notification = new NotificationCompat.Builder(getApplicationContext(), "upload_channel")
//                .setContentTitle(title)
//                .setTicker(title)
//                .setContentText(progress)
//                .setSmallIcon(R.drawable.ic_upload)
//                .setOngoing(true)
//                .setOnlyAlertOnce(true)
//                .build();
//
//        return notification;
//    }
//
//    private void updateNotification(String progress) {
//        Notification notification = createNotification(progress);
//        NotificationManager notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(NOTIFICATION_ID, notification);
//    }
}


