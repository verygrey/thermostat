package ru.hse.paulgroup2.thermostat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by verygrey on 01.09.2015.
 */
public class MyNewTSchedule implements Serializable{

    public final static int NIGHT = 0;
    public final static int DAY = 1;

    private ArrayList<LinkedList<Period>> schedule; // 7 days, up to 5 periods, dayBegin(hour-min)-dayEnd(hour-min)

    public MyNewTSchedule() {
        schedule = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            LinkedList<Period> dayOfWeek = new LinkedList<>();
            schedule.add(dayOfWeek);
        }
    }

    public boolean addPeriodToEnd(int day, Time begin, Time end) {
        schedule.get(day).add(new Period(begin, end));
        return true;
    }

    boolean inside(Time time, Period period) {
        return period.begin.toInt() <= time.toInt() && time.toInt() <= period.end.toInt();
    }

    public boolean needTempUpdate(int day, Time currentTime, int mode) {
        for (Period period: schedule.get(day)) {
            if (inside(currentTime, period)) {
                if (mode == NIGHT) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        if (mode == NIGHT) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return null if there are no period day periods today
     */
    public Period getNextDayPeriod(int dayOfWeek, int currentPeriod) {
        if (schedule.get(dayOfWeek).size() > currentPeriod + 1) {
            return schedule.get(dayOfWeek).get(currentPeriod + 1);
        } else {
            return null;
        }
    }

    /**
     * @return null if there are no period day periods today
     */
    private Time getBeginOfNextDayPeriod(int dayOfWeek, int currentPeriod) {
        if (getNextDayPeriod(dayOfWeek, currentPeriod) != null) {
            return getNextDayPeriod(dayOfWeek, currentPeriod).begin;
        }
        return null;
    }

    /**
     * @return period of night after current day period
     */
    public Period getNightPeriod(int dayOfWeek, int currentPeriod) {
        Time begin = schedule.get(dayOfWeek).get(currentPeriod).end.minuteAfter();
        Time end = getBeginOfNextDayPeriod(dayOfWeek, currentPeriod);
        if (end == null) {
            end = new Time(0, 0);
        }
        end = end.minuteBefore();
        return new Period(begin, end);
    }

    /**
     * Common clonedSchedule is without nights because it is easier way to keep and calculate clonedSchedule,
     * for example sorting
     * This clonedSchedule seems like day,night, day, night, ... , day, night
     * @return null if clonedSchedule is empty
     */
    public ArrayList<Period> getFullSchedule(int day) {
        if (schedule.size() == 0) {
            return null;
        }
        ArrayList<Period> fullSchedule = new ArrayList<>();
        int period = 0;
        while (period < schedule.get(day).size()) {
            fullSchedule.add(schedule.get(day).get(period));
            fullSchedule.add(getNightPeriod(day, period));
            period++;
        }
        return fullSchedule;
    }
}
