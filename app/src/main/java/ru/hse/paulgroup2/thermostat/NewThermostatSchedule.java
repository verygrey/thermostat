package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ivan on 25.08.2015.
 */
public class NewThermostatSchedule implements Serializable {
    public final static int NIGHT = 0;
    public final static int DAY = 1;

    private ArrayList<ArrayList<Period>> schedule; // 7 days, 5 periods, dayBegin(hour-min)-dayEnd(hour-min)

    public NewThermostatSchedule() {
        schedule = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            ArrayList<Period> dayOfWeek = new ArrayList<>(5);
            schedule.add(dayOfWeek);
            //dayOfWeek.add(new Pair<>(new HourMinute(0, 0), new HourMinute(23, 59)));
        }
        addPeriod(1, new Time(12, 0), new Time(13, 0));
    }

    public boolean addPeriod(int dayOfWeek, Time dayBegin, Time dayEnd) {
        if (isFull(dayOfWeek) || dayBegin.isBetter(dayEnd) || overlaps(dayOfWeek, dayBegin, dayEnd)) {
            return false;
        }
        schedule.get(dayOfWeek).add(new Period(dayBegin, dayEnd));
        sort(schedule.get(dayOfWeek));
        return true;
    }

    public boolean removePeriod(int dayOfWeek, int number) {
        if (schedule.get(dayOfWeek).size() < number) {
            schedule.get(dayOfWeek).remove(number);
            if (schedule.get(dayOfWeek).isEmpty()) {
                schedule.get(dayOfWeek).add(new Period(new Time(0, 0), new Time(23, 59)));
            }
            return true;
        }
        return false;
    }

    private void sort(ArrayList<Period> daySchedule) {
        if (daySchedule.size() < 2) {
            return;
        }

        Period key;
        int i, j;
        for (i = 1; i < daySchedule.size(); i++) {
            key = daySchedule.get(i);
            for (j = i - 1; j >= 0 && key.begin.isBetter(daySchedule.get(j).begin); j--) {
                daySchedule.set(j + 1, daySchedule.get(j));
            }
            daySchedule.set(j + 1, key);
        }
    }

    // only for sorted
    public boolean overlaps(int dayOfWeek, Time dayBegin, Time dayEnd) {
        ArrayList<Period> currentDay = schedule.get(dayOfWeek);
        int size = currentDay.size();
        if (size == 0) {
            return false;
        }
        if (currentDay.get(0).begin.isBetter(dayEnd)) {
            return false;
        }
        else if (dayBegin.isBetter(currentDay.get(size - 1).end)) {
            return false;
        }
        else {
            for (Period period : currentDay) {
                // ...|...|... or  ...|.._|___
                if (period.begin.isBetter(dayBegin) && dayEnd.isBetter(period.begin)) {
                    return true;
                }
                // ___|...|___
                if (dayBegin.isBetter(period.begin) && period.end.isBetter(dayEnd)) {
                    return true;
                }
                // ___|_..|... or ...|...|...
                if (period.end.isBetter(dayBegin) && dayEnd.isBetter(period.end)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isFull(int dayOfWeek) {
        if (schedule.get(dayOfWeek).size() < 5) {
            return false;
        }
        return true;
    }

    public boolean needTempUpdate(int dayOfWeek, Time currentTime, int mode) {
        boolean insidePeriod = false;
        for (Period period : schedule.get(dayOfWeek)) {
            if (currentTime.insidePeriod(period.begin, period.end)) {
                insidePeriod = true;
            }
        }
        if (mode == NIGHT && insidePeriod == true) {
            return true;
        }
        else if (mode == DAY && insidePeriod == false) {
            return true;
        }
        return false;
    }

    /**
     * @return null if there are no period day periods today
     */
    public Period getNextDayPeriod(int dayOfWeek, int currentPeriod) {
        if (schedule.get(dayOfWeek).size() > currentPeriod) {
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
        Time begin = schedule.get(dayOfWeek).get(currentPeriod).end;
        Time end = getBeginOfNextDayPeriod(dayOfWeek, currentPeriod);
        return new Period(begin.add(1), end.subtract(1));
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
        do {
            fullSchedule.add(schedule.get(day).get(period));
            fullSchedule.add(getNightPeriod(day, period));
            period++;
        } while (period < schedule.size());
        return fullSchedule;
    }
}
