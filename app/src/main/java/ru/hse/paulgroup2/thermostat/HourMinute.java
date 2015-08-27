package ru.hse.paulgroup2.thermostat;

/**
 * Created by Ivan on 26.08.2015.
 */
public class HourMinute {
    private int hour;
    private int minute;

    public HourMinute(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public boolean isBetter(HourMinute other) {
        if ((this.minute + this.hour * 60) > (other.minute + other.hour * 60)) {
            return true;
        }
        return false;
    }

    public boolean insidePeriod(HourMinute begin, HourMinute end) {
        if ((hour * 60 + minute) >= (begin.hour * 60 + begin.minute) &&
                (hour * 60 + minute) <= (end.hour * 60 + end.minute)) {
            return true;
        }
        return false;
    }

    public HourMinute add(int minute) {
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

    public HourMinute subtract(int minute) {
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
}
