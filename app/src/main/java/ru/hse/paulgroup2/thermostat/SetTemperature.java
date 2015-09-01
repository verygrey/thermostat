package ru.hse.paulgroup2.thermostat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetTemperature extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_temp);

        Intent incoming = getIntent();
        final String type = incoming.getStringExtra("TYPE");

        ImageView icon = (ImageView) findViewById(R.id.typeicon);
        switch (type) {
            case "DAY": {
                icon.setImageResource(R.drawable.bigsunpic);
                break;
            }
            case "NIGHT": {
                icon.setImageResource(R.drawable.bigmoonpic);
                break;
            }
            case "USER": {
                icon.setImageResource(R.drawable.biguserpic);
                break;
            }
        }

        Temperature current = (Temperature)incoming.getSerializableExtra("NOW");

        final SeekBar temperatureBar = (SeekBar) findViewById(R.id.temperatureBar);
        final TextView textView = (TextView) findViewById(R.id.textTemp);
        textView.setText(current.toString());
        temperatureBar.setMax(250);
        temperatureBar.setProgress(current.whole * 10 + current.frac - 50);
        temperatureBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("" + ((double)progress / 10 + 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button buttonOK = (Button) findViewById(R.id.buttonOK);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent outcoming = new Intent();
                outcoming.putExtra("TYPE", type);
                outcoming.putExtra("TEMPERATURE", new Temperature(temperatureBar.getProgress() / 10 + 5, temperatureBar.getProgress() % 10));
                setResult(RESULT_OK, outcoming);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_temp, menu);
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
}
