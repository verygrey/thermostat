package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.util.ArrayList;
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
    /**
     * period of {@code DAY}
     */
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

        if (!locked && schedule.needTempUpdate(day, new Time(hour, minute), currentMode)) {
            changeMode();
        }

        if (schedule.getNextDayPeriod(day, currentPeriod) != null) {
            currentPeriod++;
        } else {
            currentPeriod = 0;
        }
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

    public int getCurrentPeriod() {
        return currentPeriod;
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
        currentPeriod = 0;
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

    public boolean addPeriod(int dayOfWeek, Time dayBegin, Time dayEnd) {
        return schedule.addPeriod(dayOfWeek, dayBegin, dayEnd);
    }

    public boolean removePeriod(int dayOfWeek, int number) {
        // updateCurrentPeriod() ?
        return schedule.removePeriod(dayOfWeek, number);
    }

    @Deprecated
    public Pair<Time, Time> getNextDayPeriod(int dayOfWeek, int currentPeriod) {
        return schedule.getNextDayPeriod(dayOfWeek, currentPeriod);
    }

    public Pair<Time, Time> getNightPeriod(int dayOfWeek, int currentPeriod) {
        return schedule.getNightPeriod(dayOfWeek, currentPeriod);
    }

    public ArrayList<Pair<Time, Time>> getFullSchedule(int day) {
        return schedule.getFullSchedule(day);
    }
}
