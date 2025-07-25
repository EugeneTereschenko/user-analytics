package com.example.demo.util;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConverter {

    public static Date convertDateStringToDate(String dateString) {
        try {
            if (dateString == null || dateString.isEmpty()) {
                return null; // Handle null or empty string
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }

    public static Time convertTimeStringToTime(String timeString) {
        try {
            if (timeString == null || timeString.isEmpty()) {
                return null; // Handle null or empty string
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = sdf.parse(timeString);
            return new Time(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return null; // Handle null date
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String convertTimeToString(Time time) {
        if (time == null) {
            return null; // Handle null time
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time);
    }
}
