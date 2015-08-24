package ru.hse.paulgroup2.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Thermostat extends Activity {

    ImageView currentMode;
    TextView currentTemp, userTemp, dayTemp, nightTemp, currentTime;

    ThermostatModel tm;
    ThermostatController tc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermostat);

        tm = new ThermostatModel();
        tc = new ThermostatController(tm);

        currentMode = (ImageView) findViewById(R.id.currentmode);
        currentTemp = (TextView) findViewById(R.id.currenttemp);
        userTemp = (TextView) findViewById(R.id.usertemp);
        dayTemp = (TextView) findViewById(R.id.daytemp);
        nightTemp = (TextView) findViewById(R.id.nighttemp);
        currentTime = (TextView) findViewById(R.id.currenttime);
        initFields();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        runOnUiThread(timeupdateUI);
                    }
                    catch (InterruptedException ie) {
                        System.out.println("Updater is dead");
                    }
                }
            }
        }).start();
    }

    private Runnable timeupdateUI = new Runnable() {
        @Override
        public void run() {
            if (!tm.isLocked()) {
                if (tm.isUser()) {
                    currentMode.setImageResource(R.drawable.biguserpic);
                } else {
                    if (tm.isDay()) {
                        currentMode.setImageResource(R.drawable.bigsunpic);
                    } else {
                        currentMode.setImageResource(R.drawable.bigmoonpic);
                    }
                }
            }
            currentTemp.setText(dtos(tm.getCurrentTemp()));
            System.out.println("current model " + tm.hiddenServer.currentMode);
            currentTime.setText("NOW "+tm.hiddenServer.hour+":"+tm.hiddenServer.minute);
        }
    };

    public void setNewUserTemp(View view) {
        //Todo: add choose temp
        Toast.makeText(this, "UNFORTUNATELY NOT WORKING\n WE BROKEN IT ._.\nTHIS BUTTON SET RANDOM TEMPERATURE", Toast.LENGTH_LONG).show();
        double temp = (double)((int)(new Random().nextDouble() * 300)) / 10;
        tm.setUserTemp(temp);
        userTemp.setText(dtos(temp));
    }

    public void relockTemp(View view) {
        if (tm.isUser()) {
            if (tm.isLocked()) {
                tm.setLocked(false);
                currentMode.setImageResource(R.drawable.biguserpic);
                Toast.makeText(this, "Temp is unlocked", Toast.LENGTH_SHORT).show();
            } else {
                tm.setLocked(true);
                currentMode.setImageResource(R.drawable.biglockpic);
                Toast.makeText(this, "Temp is locked", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initFields() {
        if (tm.isDay()) {
            currentMode.setImageResource(R.drawable.bigsunpic);
        } else {
            currentMode.setImageResource(R.drawable.bigmoonpic);
        }
        currentTemp.setText(dtos(tm.getCurrentTemp()));
        userTemp.setText(dtos(tm.getUserTemp()));
        dayTemp.setText(dtos(tm.getDayTemp()));
        nightTemp.setText(dtos(tm.getNightTemp()));
    }

    public void onoffUserTemp(View view) {
        if (tm.isUser()) {
            tm.setUser(false);
            Toast.makeText(this, "User mode disabled", Toast.LENGTH_SHORT).show();
        } else {
            if (!tm.isLocked()) {
                tm.setUser(true);
                Toast.makeText(this, "User mode enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void exportSchedule(View view) {
        tc.exportScheduleToServer();
        Toast.makeText(this, "Schedule is exported", Toast.LENGTH_SHORT).show();
    }

    public void importSchedule(View view) {
        tc.importScheduleFromServer();
        userTemp.setText(dtos(tm.getUserTemp()));
        dayTemp.setText(dtos(tm.getDayTemp()));
        nightTemp.setText(dtos(tm.getNightTemp()));
        Toast.makeText(this, "Schedule is imported", Toast.LENGTH_SHORT).show();
    }

    private String dtos(double temp) {
        String plus ="";
        if ((int)(temp * 10) % 10 < 10) {
            plus = "0";
        }
        return " " + (int)temp + "." + plus + (int)(temp * 10) % 10 + "" + getString(R.string.degree_part);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_thermostat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setSchedule(View view) {
        Toast.makeText(this, "UNFORTUNATELY NOT WORKING\n WE BROKEN IT ._.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Thermostat.this, Schedule.class);
        intent.putExtra("SCHEDULE", tm.getUserSchedule());
        startActivity(intent);
    }
}
