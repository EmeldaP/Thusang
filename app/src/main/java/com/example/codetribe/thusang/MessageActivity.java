package com.example.codetribe.thusang;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {

    String PhoneNumber = "";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button send;
    EditText phoneNo;
    EditText Message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Intent sms = new Intent(Intent.ACTION_VIEW);
        sms.setType("vnd.android-dir/mms-sms");
        // Specify the Phone Number
        sms.putExtra("address",PhoneNumber);


        Intent sms1 = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:+27726478359"));
        sms.putExtra("sms_body","type message");
        startActivity(sms1);


        startActivity(sms);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("PhoneNumber", null, "sms message", null, null);

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "default content");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);

        send = (Button) findViewById(R.id.button2);
        phoneNo = (EditText) findViewById(R.id.editText);
        Message = (EditText) findViewById(R.id.editText2);
    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String("01234"));
        smsIntent.putExtra("sms_body", "Test ");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MessageActivity.this, "SMS faild, please try again later.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sms) {
            startActivity(new Intent(MessageActivity.this, MainActivity.class));
            return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

}





