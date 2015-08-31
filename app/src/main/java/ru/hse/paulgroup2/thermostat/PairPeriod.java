package ru.hse.paulgroup2.thermostat;

import android.util.Pair;

import java.io.Serializable;

/**
 * Created by verygrey on 31.08.2015.
 */
public class PairPeriod implements Serializable{
    Period day;
    Period night;

    public PairPeriod(Pair<Time, Time> day, Pair<Time, Time> night) {
        this.day = new Period(day);
        this.night = new Period(night);
    }

    public PairPeriod(Period day, Period night) {
        this.day = day;
        this.night = night;
    }
}
