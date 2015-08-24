package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by verygrey on 31.05.2015.
 */
public class ThermostatServer implements Serializable{

    double futureTemp;
//    double oldTemp;
//    double currentTemp;
    double userTemp;

    int oldMode;
    int currentMode;

    boolean user;
    boolean locked;

    int curra, currb;

    int day, hour, minute, second;
    int periods;

    int velocity;
    ThermostatSchedule schedule;

    ThermostatServer() {
        velocity = 60 * 10;
        curra = 10;
        currb = 0;
        userTemp = 10.0;
        futureTemp = 20.0;
        user = false;
        locked = false;
        currentMode = ThermostatSchedule.NIGHT;
        schedule = new ThermostatSchedule();
        Calendar time = Calendar.getInstance();
        day = time.get(Calendar.DAY_OF_WEEK); // sunday, monday...
        hour = time.get(Calendar.HOUR_OF_DAY); // 24
        minute = time.get(Calendar.MINUTE);
        second = time.get(Calendar.SECOND);
        timer.start();
    }

    private Thread timer = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    eachTick();
                }
                catch (InterruptedException ex) {
                    System.out.println("Internal thread in Timer was interrupted");
                    break;
                }
            }
        }
    });

    private void eachTick() {

        boolean timetochange = false;

        periods = velocity;
        while (periods > 0) {
            second++;
            if (second == 60) {
                second = 0;
                minute++;
                if (minute == endtimeb && hour == endtimea) {
                    timetochange = true;
                }
                if (minute == 60) {
                    minute = 0;
                    hour++;
                    if (minute == endtimeb && hour == endtimea) {
                        timetochange = true;
                    }
                    if (hour == 24) {
                        hour = 0;
                        day++;
                        if (day == 8) {
                            day = 1;
                        }
                    }
                }
            }
            periods--;
        }
        double temp;
        if (user) {
            temp = userTemp;
            int time = hour * 60 + minute;
            if (timetochange) {
                if (!locked) {
                    user = false;
                    currentMode = schedule.getMode(hour, minute, day);
                } else {
                    for (ArrayList<Integer> p: schedule.getDayPeriods(day)) {
                        if (p.get(0) <= time && p.get(1) > time) {
                            endtimea = p.get(1) / 60;
                            endtimeb = p.get(1) % 60;
                        }
                    }
                }
            }
        } else {
            temp = schedule.getTemp(hour, minute, day);
        }
        double curr = getCurrentTemp();
        for (int i = 0; i < velocity && temp != curr; i++) {
            if (temp > curr) {
                currb += speed;
                while (currb > 9) {
                    curra += 1;
                    currb -= 10;
//                curra++;
//                currb=0;
                }
            } else {
                if (temp < curr) {
                    currb -= speed;
                    while (currb < 0) {
                        curra -= 1;
                        currb += 10;
//                    curra--;
//                    currb = 9;
                    }
                }
            }
            curr = getCurrentTemp();
        }
//        System.out.println("current server " + getCurrentTemp());
    }

    double getCurrentTemp() {
        return curra + (double) currb / 10;
    }

    double speed = 1;

    public double getTemp() {
        return futureTemp;
    }

    public void setTemp(double temp) {
        futureTemp = temp;
    }

    public ThermostatSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(ThermostatSchedule schedule) {
        this.schedule = schedule;
    }

    public boolean isDay() {
        return schedule.getMode
                (hour, minute, day)
                == ThermostatSchedule.DAY;
    }

    public double getUserTemp() {
        return userTemp;
    }

    public void setUserTemp(double userTemp) {
        this.userTemp = userTemp;
    }

    public boolean isUser() {
        return user;
    }

    public void setUser(boolean user) {
        this.user = user;
        int time = hour * 60 + minute;
        for (ArrayList<Integer> p: schedule.getDayPeriods(day)) {
            if (p.get(0) <= time && p.get(1) > time) {
                endtimea = p.get(1) / 60;
                endtimeb = p.get(1) % 60;
            }
        }
    }

    int endtimea, endtimeb;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
