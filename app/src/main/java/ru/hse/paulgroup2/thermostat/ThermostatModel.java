package ru.hse.paulgroup2.thermostat;

import java.io.Serializable;

/**
 * Created by verygrey on 31.05.2015.
 */
public class ThermostatModel implements Serializable{

    ThermostatSchedule termostatSchedule = new ThermostatSchedule();
    ThermostatServer hiddenServer = new ThermostatServer();

    public ThermostatSchedule getServerSchedule() {
        return hiddenServer.getSchedule();
    }

    public void setServerSchedule(ThermostatSchedule schedule) {
        hiddenServer.setSchedule(schedule);
    }

    public ThermostatSchedule getUserSchedule() {
        return termostatSchedule;
    }

    public void setUserSchedule(ThermostatSchedule schedule) {
        termostatSchedule = schedule;
    }

    public double getUserTemp() {
        return hiddenServer.getUserTemp();
    }

    public void setUserTemp(double userTemp) {
        hiddenServer.setUserTemp(userTemp);
    }

    public boolean isUser() {
        return hiddenServer.isUser();
    }

    public void setUser(boolean user) {
        hiddenServer.setUser(user);
    }

    public boolean isLocked() {
        return hiddenServer.isLocked();
    }

    public void setLocked(boolean locked) {
        hiddenServer.setLocked(locked);
    }

    public double getNightTemp() {
        return hiddenServer.getSchedule().nighttemp;
    }

    public void setNightTemp(double temp) {
        hiddenServer.getSchedule().nighttemp = temp;
    }

    public double getDayTemp() {
        return hiddenServer.getSchedule().daytemp;
    }

    public void setDayTemp(double temp) {
        hiddenServer.getSchedule().daytemp = temp;
    }

    public double getCurrentTemp() {
        return hiddenServer.getCurrentTemp();
    }

    public boolean isDay() {
        return hiddenServer.isDay();
    }

}
