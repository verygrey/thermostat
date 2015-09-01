package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.io.Serializable;

/**
 * Created by verygrey on 31.08.2015.
 */
public class Period implements Serializable{
    Time begin;
    Time end;

    public Period(Pair<Time, Time> day) {
        begin = day.first;
        end = day.second;
    }

    public Period(Time begin, Time end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return begin.toString() + " - " + end.toString();
    }
}
