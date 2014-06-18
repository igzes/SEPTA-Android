package org.septa.android.app.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

import org.septa.android.app.activities.schedules.ItinerarySelectionActionBarActivity;
import org.septa.android.app.databases.SEPTADatabase;
import org.septa.android.app.models.TripObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDateUtilities {

    public static int getDayOfTheWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        return day;
    }

    public static boolean isTodaySunday() {
        int day = getDayOfTheWeek();

        switch (day) {
            case Calendar.SUNDAY: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    // this is a short cut method to return the service id.
    // technically this data should be looked up in the SQLlite database.
    // but for now
    public static int getServiceIdForNow(Context context) {
        int day = getDayOfTheWeek();

         return getServiceIdForDay(day);
    }

    public static int getServiceIdForDay(int day) {
        switch (day) {
            case Calendar.SUNDAY: {
                return 3;
            }
            case Calendar.SATURDAY: {
                return 2;
            }
            case Calendar.FRIDAY: {
                return 4;
            }
            default: {      // this will cover any days not specifically called out
                return 1;
            }
        }
    }

    public static String getStringFromTime(int time) {
        if (time < 1000)
            return "0"+String.valueOf(time);
        else
            return String.valueOf(time);
    }

    public static int getNowTimeFormatted() {
        Date now = new Date();

        SimpleDateFormat format = new SimpleDateFormat("HHmm");
        String formattedNow = format.format(now);

        return Integer.parseInt(formattedNow);
    }

    public static String getLocalizedHHMMStamp(Context context, Date date) {
        // Different formatters for 12 and 24 hour timestamps
        SimpleDateFormat formatter24 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatter12 = new SimpleDateFormat("hh:mm a");

        // According to users preferences the OS clock is displayed in 24 hour format
        if (DateFormat.is24HourFormat(context)) {
            return formatter24.format(date);
        }

        return formatter12.format(date);
    }

    public static String formatHoursMinutesDisplay(long minutes) {
        if (minutes == 1) {
            return "1 min";
        }

        if (minutes <= 60) {
            return minutes + " mins";
        }

        long hours = minutes / 60;

        return hours + " hours "+(minutes/60) + " mins";
    }

    public static long minutesUntilStartTime(Date now, TripObject trip) {
        Date startTime = null;

        try {
            startTime = new SimpleDateFormat("kkmm").parse(CalendarDateUtilities.getStringFromTime(trip.getStartTime().intValue()));
            if (now.after(startTime)) {
                startTime = new Date(startTime.getTime() + (1000*60*60*24));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (startTime.getTime() - now.getTime()) / (60 * 1000);
    }

    public static long tripTime(TripObject trip) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = new SimpleDateFormat("HHmm").parse(CalendarDateUtilities.getStringFromTime(trip.getStartTime().intValue()));
            endDate = new SimpleDateFormat("HHmm").parse(CalendarDateUtilities.getStringFromTime(trip.getEndTime().intValue()));
        } catch (Exception ex) {
            // TODO: put error handling in here
        }

        if (endDate.before(startDate)) {  // if end date is showing less than start, it must be the next day
                endDate = new Date(endDate.getTime() + (1000*60*60*24));
        }

        return (endDate.getTime() - startDate.getTime()) / (60 * 1000);
    }
}