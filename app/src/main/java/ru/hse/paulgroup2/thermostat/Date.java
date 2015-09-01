package ru.hse.paulgroup2.thermostat;

import java.util.Calendar;

/**
 * Created by verygrey on 30.08.2015.
 */
public class Date {

    static final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

    int hour;
    int minute;
    int day;

    public Date(Calendar calendar) {
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.day = calendar.get(Calendar.DAY_OF_WEEK) - 1;    }

    public void addMinute() {
        minute++;
        if (minute == 60) {
            minute = 0;
            hour++;
            if (hour == 24) {
                hour = 0;
                day++;
                if (day == 7) {
                    day = 0;
                }
            }
        }
    }

    @Override
    public String toString() {
        String min = "";
        if (minute < 10) {
            min += "0";
        }
        min += minute;
        return days[day] + " " + hour + ":" + min;
    }
}
