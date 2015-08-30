package ru.hse.paulgroup2.thermostat;

import java.util.Calendar;

/**
 * Created by verygrey on 30.08.2015.
 */
public class Date {

    static final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

    int hour;
    int minute;
    int day;

    public Date(Calendar calendar) {
        this.hour = calendar.get(Calendar.HOUR);
        this.minute = calendar.get(Calendar.MINUTE);
        this.day = calendar.get(Calendar.DAY_OF_WEEK);    }

    public void addMinute() {
        minute++;
        if (minute == 60) {
            minute = 0;
            hour++;
            if (hour == 24) {
                hour = 0;
                day++;
                if (day == 8) {
                    day = 1;
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
        return days[day - 1] + " " + hour + ":" + min;
    }
}
