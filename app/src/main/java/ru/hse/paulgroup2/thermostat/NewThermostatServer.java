package ru.hse.paulgroup2.thermostat;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ivan on 25.08.2015.
 */
public class NewThermostatServer {
    public final static int NIGHT = 0;
    public final static int DAY = 1;

    private NewThermostatSchedule schedule;
    private double dayTemp;
    private double nightTemp;
    private double currentTemp;
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

        if (!locked && schedule.needUpdate(day, new HourMinute(hour, minute), currentMode)) {
            changeMode();
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

    public NewThermostatServer(NewThermostatSchedule schedule) {
        this.schedule = schedule;
        locked = false;
        Calendar time = Calendar.getInstance();
        day = time.get(Calendar.DAY_OF_WEEK);
        hour = time.get(Calendar.HOUR_OF_DAY);
        minute = time.get(Calendar.MINUTE);
        timer.start();
    }

    public int getCurrentMode() {
        return currentMode;
    }

    public int getNextMode() {
        if (currentMode == NIGHT) {
            return DAY;
        } else {
            return NIGHT;
        }
    }
}
