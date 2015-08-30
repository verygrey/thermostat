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

    private ArrayList<ArrayList<Pair<Time,Time>>> schedule; // 7 days, 5 periods, dayBegin(hour-min)-dayEnd(hour-min)

    NewThermostatSchedule() {
        schedule = new ArrayList<>(7);
        for (ArrayList<Pair<Time,Time>> dayOfWeek : schedule) {
            dayOfWeek = new ArrayList<>(5);
            //dayOfWeek.add(new Pair<>(new HourMinute(0, 0), new HourMinute(23, 59)));
        }
    }

    public boolean addPeriod(int dayOfWeek, Time dayBegin, Time dayEnd) {
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
                schedule.get(dayOfWeek).add(new Pair<>(new Time(0, 0), new Time(23, 59)));
            }
            return true;
        }
        return false;
    }

    private void sort(ArrayList<Pair<Time, Time>> daySchedule) {
        if (daySchedule.size() < 2) {
            return;
        }

        Pair<Time, Time> key;
        int i, j;
        for (i = 1; i < daySchedule.size(); i++) {
            key = daySchedule.get(i);
            for (j = i - 1; j >= 0 && key.first.isBetter(daySchedule.get(j).first); j--) {
                daySchedule.set(j + 1, daySchedule.get(j));
            }
            daySchedule.set(j + 1, key);
        }
    }

    // only for sorted
    private boolean overlaps(int dayOfWeek, Time dayBegin, Time dayEnd) {
        ArrayList<Pair<Time, Time>> currentDay = schedule.get(dayOfWeek);
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
            for (Pair<Time, Time> period : currentDay) {
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

    public boolean needTempUpdate(int dayOfWeek, Time currentTime, int mode) {
        boolean insidePeriod = false;
        for (Pair<Time, Time> period : schedule.get(dayOfWeek)) {
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
    public Pair<Time, Time> getNextDayPeriod(int dayOfWeek, int currentPeriod) {
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
            return getNextDayPeriod(dayOfWeek, currentPeriod).first;
        }
        return null;
    }

    /**
     * @return period of night after current day period
     */
    public Pair<Time, Time> getNightPeriod(int dayOfWeek, int currentPeriod) {
        Time begin = schedule.get(dayOfWeek).get(currentPeriod).second;
        Time end = getBeginOfNextDayPeriod(dayOfWeek, currentPeriod);
        return new Pair<>(begin.add(1), end.subtract(1));
    }

    /**
     * Common schedule is without nights because it is easier way to keep and calculate schedule,
     * for example sorting
     * This schedule seems like day,night, day, night, ... , day, night
     * @return null if schedule is empty
     */
    public ArrayList<Pair<Time, Time>> getFullSchedule(int day) {
        if (schedule.size() == 0) {
            return null;
        }
        ArrayList<Pair<Time, Time>> fullSchedule = new ArrayList<>();
        int period = 0;
        do {
            fullSchedule.add(schedule.get(day).get(period));
            fullSchedule.add(getNightPeriod(day, period));
            period++;
        } while (period < schedule.size());
        return fullSchedule;
    }
}
