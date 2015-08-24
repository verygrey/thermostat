package ru.hse.paulgroup2.thermostat;

import java.io.Serializable;

/**
 * Created by verygrey on 31.05.2015.
 */

public class ThermostatController implements Serializable{

    ThermostatModel model = new ThermostatModel();

    public ThermostatController(ThermostatModel model) {
        this.model = model;
    }

    public void importScheduleFromServer() {
        model.setUserSchedule(new ThermostatSchedule(model.getServerSchedule()));
    }

    public void exportScheduleToServer() {
        model.setServerSchedule(new ThermostatSchedule(model.getUserSchedule()));
    }

    public boolean addDayNightPeriod(int time, int day) {
        return model.getUserSchedule().addDayBegin(time, day);
    }

    public boolean addNightDayPeriod(int time, int day) {
        return model.getUserSchedule().addNightBegin(time, day);
    }

    public void removePeriod(int time, int day) {
        model.getUserSchedule().removePeriod(time, day);
    }
}
