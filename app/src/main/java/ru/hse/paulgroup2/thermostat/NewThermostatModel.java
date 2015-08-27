package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan on 25.08.2015.
 */
public class NewThermostatModel {
    public final static int NIGHT = 0;
    public final static int DAY = 1;

    private NewThermostatSchedule schedule;
    private double dayTemp;
    private double nightTemp;
    private double currentTemp;
    private int currentPeriod;
    private int day;
    private int hour;
    private int minute;
    private int currentMode;
    private boolean locked;

    private Thread timer = new Thread(()->{
        while(!Thread.currentThread().isInterrupted()) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
                tick();
            }
            catch (InterruptedException ex) {
                System.out.println("Internal thread in Timer was interrupted");
                break;
            }
        }
    });

    private void tick() {
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

        if (!locked && schedule.needTempUpdate(day, new HourMinute(hour, minute), currentMode)) {
            changeMode();
        }

        //TODO: if (locked && needTempUpdate) { changePeriod() }
    }

    private void changeMode() {
        if (currentMode == NIGHT) {
            currentMode = DAY;
            currentTemp = dayTemp;
        } else {
            currentMode = NIGHT;
            currentTemp = nightTemp;
        }
    }

    public NewThermostatSchedule getSchedule() {
        return schedule;
    }

    public double getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

    public double getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    public NewThermostatModel(NewThermostatSchedule schedule) {
        this.schedule = schedule;
        locked = false;
        Calendar time = Calendar.getInstance();
        day = time.get(Calendar.DAY_OF_WEEK);
        hour = time.get(Calendar.HOUR_OF_DAY);
        minute = time.get(Calendar.MINUTE);
        dayTemp = 18;
        nightTemp = 15;
        currentTemp = nightTemp;
        timer.start();
    }

    public int getCurrentMode() {
        return currentMode;
    }

    //todo: NIGHT at midnight always
    public int getNextMode() {
        if (currentMode == NIGHT) {
//            if (getNextDayPeriod(day, currentPeriod) != null) {
//                return DAY;
//            }
            return DAY;
        }
        return NIGHT;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean addPeriod(int dayOfWeek, HourMinute dayBegin, HourMinute dayEnd) {
        return schedule.addPeriod(dayOfWeek, dayBegin, dayEnd);
    }

    public boolean removePeriod(int dayOfWeek, int number) {
        return schedule.removePeriod(dayOfWeek, number);
    }

    public Pair<HourMinute, HourMinute> getNextDayPeriod(int dayOfWeek, int currentPeriod) {
        return schedule.getNextDayPeriod(dayOfWeek, currentPeriod);
    }

    //TODO: method for calculating current period
}
