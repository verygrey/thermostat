package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by verygrey on 31.05.2015.
 */
public class ThermostatSchedule implements Serializable{

//    private static final long serialVersionUID = -1096217523172222472L;

    double daytemp;
    double nighttemp;

    public static final int NIGHT = 0, NIGHTBEGIN = 2;
    public static final int DAY = 1, DAYBEGIN = 3;

    ArrayList<LinkedList<ArrayList<Integer>>> dayPeriods;

    public ThermostatSchedule() {
        daytemp = 20;
        nighttemp = 15;
        dayPeriods = new ArrayList<>();
        ArrayList<Integer> mid = new ArrayList<>();
        mid.add(0); mid.add(24 * 60); mid.add(NIGHT);
        LinkedList<ArrayList<Integer>> dayperiods = new LinkedList<>();
        dayperiods.add(new ArrayList<Integer>(mid));
        for (int i = 0; i < 7; i++) {
            dayPeriods.add(new LinkedList<ArrayList<Integer>>(dayperiods));
        }
    }

    public ThermostatSchedule(ThermostatSchedule ts) {
        this();
        daytemp = ts.daytemp;
        nighttemp = ts.nighttemp;
        dayPeriods = ts.dayPeriods;
    }

    private boolean addPeriodBegin(int time, int mode, int day) {
        LinkedList<ArrayList<Integer>> dayperiods = getDayPeriods(day);
        for (int i = 0; i < dayperiods.size(); i++) {
            if (dayperiods.get(i).get(1) > time) {
                ArrayList<Integer> newperiod = new ArrayList<>();
                newperiod.add(time);
                newperiod.add(dayperiods.get(i).get(1));
                newperiod.add(mode);
                dayperiods.get(i).set(1, time);
                dayperiods.add(i, newperiod);
                return true;
            }
        }
        return false;
    }

    public boolean addDayBegin(Integer time, Integer day) {
        return addPeriodBegin(time, DAY, day);
    }


    public boolean addNightBegin(Integer time, Integer day) {
        return addPeriodBegin(time, NIGHT, day);
    }

    public void removePeriod(int time, int day) {
        if (time == 24*60) {
        }
        LinkedList<ArrayList<Integer>> dayperiods = getDayPeriods(day);
        for (int i = 0; i < dayperiods.size(); i++) {
            if (dayperiods.get(i).get(1) == time) {
                if (i != 0) {
                    dayperiods.get(i - 1).set(1, dayperiods.get(i).get(1));
                    dayperiods.remove(i);
                } else {
                    if (dayperiods.size() != 1) {
                        dayperiods.get(1).set(0, 0);
                    }
                    dayPeriods.remove(0);
                }
                break;
            }
        }
    }

    private static int HMtoTime(Integer hour, Integer minute) {
        return hour * 60 + minute;
    }

    public int getMode(int hour, int minute, int day)
        throws IllegalStateException {
        int time = HMtoTime(hour, minute);
        LinkedList<ArrayList<Integer>> dayperiods = getDayPeriods(day);
        for (int i = 0; i < dayperiods.size(); i++) {
            if (dayperiods.get(i).get(0) == time) {
                return dayperiods.get(i).get(2) + 2;
            }
            if (dayperiods.get(i).get(0) <= time && time < dayperiods.get(i).get(1)) {
                return dayperiods.get(i).get(2);
            }
        }
        throw new IllegalStateException("Incorrect clonedSchedule state: not found time");
    }

    public double getTemp(int hour, int minute, int day) {
        switch (getMode(hour, minute, day)) {
            case NIGHT:
            {
                return nighttemp;
            }
            case DAY:
            {
                return daytemp;
            }
        }
        throw new IllegalStateException("Incorrect result of getMode()");
    }

    public LinkedList<ArrayList<Integer>> getDayPeriods(int numday) {
        if (numday < 1 || numday > 7) {
            numday = 1;
        }
        return dayPeriods.get(numday - 1);
    }

    public ArrayList<Pair<Integer, Integer>> getPeriods(int numday, int mode) {
        ArrayList<Pair<Integer, Integer>> days = new ArrayList<>();
        for (ArrayList<Integer> list: getDayPeriods(numday)) {
            if (list.get(2) == mode) {
                days.add(new Pair<>(list.get(0), list.get(1)));
            }
        }
        return days;
    }
}