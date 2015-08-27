package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Ivan on 25.08.2015.
 */
public class NewThermostatSchedule {
    public final static int NIGHT = 0;
    public final static int DAY = 1;

    private ArrayList<ArrayList<Pair<HourMinute,HourMinute>>> schedule; // 7 days, 5 periods, dayBegin(hour-min)-dayEnd(hour-min)

    NewThermostatSchedule() {
        schedule = new ArrayList<>(7);
        for (ArrayList<Pair<HourMinute,HourMinute>> dayOfWeek : schedule) {
            dayOfWeek = new ArrayList<>(5);
            //dayOfWeek.add(new Pair<>(new HourMinute(0, 0), new HourMinute(23, 59)));
        }
    }

    public boolean addPeriod(int dayOfWeek, HourMinute dayBegin, HourMinute dayEnd) {
        if (isFull(dayOfWeek) || dayBegin.isBetter(dayEnd) || overlaps(dayOfWeek, dayBegin, dayEnd)) {
            return false;
        }
        schedule.get(dayOfWeek).add(new Pair<>(dayBegin, dayEnd));
        sort(schedule.get(dayOfWeek));
        return true;
    }

    public boolean removePeriod(int dayOfWeek, int number) {
        if (schedule.get(dayOfWeek).size() < number) {
            schedule.get(dayOfWeek).remove(number);
            if (schedule.get(dayOfWeek).isEmpty()) {
                schedule.get(dayOfWeek).add(new Pair<>(new HourMinute(0, 0), new HourMinute(23, 59)));
            }
            return true;
        }
        return false;
    }

    private void sort(ArrayList<Pair<HourMinute, HourMinute>> daySchedule) {
        if (daySchedule.size() < 2) {
            return;
        }
        // TODO: sort by dayBegin
    }

    // only for sorted
    private boolean overlaps(int dayOfWeek, HourMinute dayBegin, HourMinute dayEnd) {
        ArrayList<Pair<HourMinute, HourMinute>> currentDay = schedule.get(dayOfWeek);
        int size = currentDay.size();
        if (size == 0) {
            return false;
        }
        if (currentDay.get(0).first.isBetter(dayEnd)) {
            return false;
        }
        else if (dayBegin.isBetter(currentDay.get(size - 1).second)) {
            return false;
        }
        else {
            for (Pair<HourMinute, HourMinute> period : currentDay) {
                // ...|...|... or  ...|.._|___
                if (period.first.isBetter(dayBegin) && dayEnd.isBetter(period.first)) {
                    return true;
                }
                // ___|...|___
                if (dayBegin.isBetter(period.first) && period.second.isBetter(dayEnd)) {
                    return true;
                }
                // ___|_..|... or ...|...|...
                if (period.second.isBetter(dayBegin) && dayEnd.isBetter(period.second)) {
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

    public boolean needTempUpdate(int dayOfWeek, HourMinute currentTime, int mode) {
        boolean insidePeriod = false;
        for (Pair<HourMinute, HourMinute> period : schedule.get(dayOfWeek)) {
            if (currentTime.insidePeriod(period.first, period.second)) {
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
    @Deprecated
    public Pair<HourMinute, HourMinute> getNextDayPeriod(int dayOfWeek, int currentPeriod) {
        if (schedule.get(dayOfWeek).size() > currentPeriod) {
            return schedule.get(dayOfWeek).get(currentPeriod + 1);
        } else {
            return null;
        }
    }

    /**
     * @return null if there are no period day periods today
     */
    private HourMinute getBeginOfNextDayPeriod(int dayOfWeek, int currentPeriod) {
        if (getNextDayPeriod(dayOfWeek, currentPeriod) != null) {
            return getNextDayPeriod(dayOfWeek, currentPeriod).first;
        }
        return null;
    }

    /**
     * @return period of night after current day period
     */
    public Pair<HourMinute, HourMinute> getNightPeriod(int dayOfWeek, int currentPeriod) {
        HourMinute begin = schedule.get(dayOfWeek).get(currentPeriod).second;
        HourMinute end = getBeginOfNextDayPeriod(dayOfWeek, currentPeriod);
        return new Pair<>(begin.add(1), end.subtract(1));
    }
}
