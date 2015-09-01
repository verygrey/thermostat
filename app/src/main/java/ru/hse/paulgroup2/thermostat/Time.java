package ru.hse.paulgroup2.thermostat;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Ivan on 26.08.2015.
 */
public class Time implements Serializable{
    private int hour;
    private int minute;

    public Time(Calendar calendar) {
        this.hour = calendar.get(Calendar.HOUR);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public boolean isLater(Time other) {
        if ((this.minute + this.hour * 60) > (other.minute + other.hour * 60)) {
            return true;
        }
        return false;
    }

    public boolean insidePeriod(Time begin, Time end) {
        if ((hour * 60 + minute) >= (begin.hour * 60 + begin.minute) &&
                (hour * 60 + minute) <= (end.hour * 60 + end.minute)) {
            return true;
        }
        return false;
    }

    public Time add(int minute) {
        this.minute += minute;
        if (this.minute == 60) {
            this.minute = 0;
            hour++;
            if (hour == 24) {
                hour = 0;
            }
        }
        return this;
    }

    public Time subtract(int minute) {
        this.minute -= minute;
        if (this.minute < 0) {
            this.minute = 60 + this.minute;
            hour--;
            if (hour < 0) {
                hour = 23;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        String min = (minute < 10) ? "0" + minute : "" + minute;
        return "" + hour + ":" + min;
    }

    public int toInt() {
        return hour * 60 + minute;
    }

    public Time minuteBefore() {
        if (minute == 0) {
            if (hour == 0) {
                return new Time(23, 59);
            } else {
                return new Time(hour - 1, 59);
            }
        } else {
            return new Time(hour, minute - 1);
        }
    }

    public Time minuteAfter() {
        if (minute == 59) {
            if (hour == 23) {
                return new Time(0, 0);
            } else {
                return new Time(hour + 1, 0);
            }
        } else {
            return new Time(hour, minute + 1);
        }
    }
}
