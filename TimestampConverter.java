package com.example.madguardians.database;

import androidx.room.TypeConverter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimestampConverter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @TypeConverter
    public static String fromTimestamp(Date date) {
        return date == null ? null : dateFormat.format(date);
    }

    @TypeConverter
    public static Date toTimestamp(String dateString) {
        try {
            return dateString == null ? null : dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
