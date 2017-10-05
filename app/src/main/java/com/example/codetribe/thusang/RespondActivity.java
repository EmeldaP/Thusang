package com.example.codetribe.thusang;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;

import static junit.runner.BaseTestRunner.savePreferences;

public class RespondActivity extends Activity implements View.OnClickListener {
    private Spinner respond;
    private CheckBox checkbox;
    private EditText responseText;
    Button Ok, Cancel;
    AlarmManager alarm;
    private BroadcastReceiver stopAutoResponderReceiver1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond);

        respond = (Spinner) findViewById(R.id.spinner);
        checkbox = (CheckBox) findViewById(R.id.checkboxSendLocation);
        responseText = (EditText) findViewById(R.id.respond);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.respondForDisplayItems, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        respond.setAdapter(adapter);

        Ok = (Button) findViewById(R.id.ohk);
        Ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                savePreferences();
                setResult(RESULT_OK, null);
                finish();
            }
        });
        Cancel = (Button) findViewById(R.id.cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                respond.setSelection(-1);

                savePreferences();
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        updateUIFromPreferences();
    }

    private void updateUIFromPreferences() {

    }

    private void savePreferences() {
        // Get the current settings from the UI
        boolean autoRespond = respond.getSelectedItemPosition() > 0;
        int respondForIndex = respond.getSelectedItemPosition();
        boolean includeLoc = checkbox.isChecked();
        String respondText = responseText.getText().toString();

        setAlarm(respondForIndex);

    }

    PendingIntent intentToFire;

    public static final String alarmAction = "com.example.codetribe.thusang.AUTO_RESPONSE_EXPIRED";
    private BroadcastReceiver stopAutoResponderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(alarmAction)) {

            }
        }
    };

    private void startAutoResponder() {
        startActivityForResult(new Intent(RespondActivity.this,
                MainActivity.class), 0);
        startActivityForResult(new Intent(RespondActivity.this, MainActivity.class), 0);
    }

    //alarm
    private void setAlarm(int respondForIndex) {
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (intentToFire == null) {
            if (intentToFire == null) {
                Intent intent = new Intent(String.valueOf(respondForIndex));
                intentToFire = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                IntentFilter filter = new IntentFilter(String.valueOf(respondForIndex));
                registerReceiver(stopAutoResponderReceiver, filter);
            }
        }
        if (respondForIndex < 1)
            // If “disabled” is selected, cancel the alarm.
            alarm.cancel(intentToFire);
        else {
            // Otherwise find the length of time represented by the selection and set the alarm to
            // trigger after that time has passed.
            Resources r = getResources();
            int[] respondForValues = r.getIntArray(R.array.respondForValues);
            int respondFor = respondForValues[respondForIndex];
            long t = System.currentTimeMillis();
            t = t + respondFor * 1000 * 60;
            // Set the alarm.
            alarm.set(AlarmManager.RTC_WAKEUP, t, intentToFire);
        }
    }

    @Override
    public void onClick(View v) {}
    public void requestReceived(String from) {

        }
    }
