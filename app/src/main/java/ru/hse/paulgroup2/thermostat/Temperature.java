package ru.hse.paulgroup2.thermostat;

/**
 * Created by verygrey on 30.08.2015.
 */
public class Temperature {
    int whole;
    int frac;

    public Temperature(int whole, int frac) {
        this.whole = whole;
        this.frac = frac;
    }

    @Override
    public String toString() {
        return whole + "." + frac;
    }
}
