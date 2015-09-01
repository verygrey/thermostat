package ru.hse.paulgroup2.thermostat;

import java.io.Serializable;

/**
 * Created by verygrey on 30.08.2015.
 */
public class Temperature implements Serializable {
    int whole;
    int frac;

    public Temperature(int whole, int frac) {
        this.whole = whole;
        this.frac = frac;
    }

    public void correctTo(int delta, Temperature limit) {
        if (limit.equals(this)) {
            return;
        }
        if (limit.toInt() > this.toInt()) {
            frac += delta;
            while (frac > 9) {
                frac -= 10;
                whole++;
            }
            if (limit.toInt() < this.toInt()) {
                this.whole = limit.whole;
                this.frac = limit.frac;
            }
        } else {
            frac -= delta;
            while (frac < 0) {
                frac += 10;
                whole--;
            }
            if (limit.toInt() > this.toInt()) {
                this.whole = limit.whole;
                this.frac = limit.frac;
            }
        }
    }

    @Override
    public String toString() {
        return whole + "." + frac;
    }

    public int toInt() { return whole * 10 + frac; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Temperature that = (Temperature) o;

        if (whole != that.whole) return false;
        return frac == that.frac;

    }

    @Override
    public int hashCode() {
        int result = whole;
        result = 31 * result + frac;
        return result;
    }
}
