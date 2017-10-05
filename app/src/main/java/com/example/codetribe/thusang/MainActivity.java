package com.example.codetribe.thusang;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;
import static com.example.codetribe.thusang.R.id.call;
import static com.example.codetribe.thusang.R.id.sms;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

 PersonalD personalD = new PersonalD();
    ReentrantLock lock;
    ListView list;
    CheckBox locationCheckBox;
    ArrayList<String> requesters;
    ArrayAdapter<String> ad;
    private Button Safe, Help, Responder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lock = new ReentrantLock();
        requesters = new ArrayList<String>();
        wireUpControls();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

    }

    private void wireUpControls() {
        locationCheckBox = (CheckBox) findViewById(R.id.checkboxSendLocation);
        ListView list = (ListView) findViewById(R.id.lw);
        int layoutID = android.R.layout.activity_list_item;
        ad = new ArrayAdapter<String>(this, layoutID, requesters);
        list.setAdapter(ad);

        Safe = (Button) findViewById(R.id.ok);
        Safe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                respond(true, locationCheckBox.isChecked());
                Toast.makeText(MainActivity.this,"Im ok and safe",Toast.LENGTH_SHORT).show();
            }
        });

        Help = (Button) findViewById(R.id.no);
        Help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                respond(true, locationCheckBox.isChecked());

                Intent intent = new Intent(getApplication(),CallActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Emergency sms have been send",
                        Toast.LENGTH_SHORT).show();
            }
        });
        Responder = (Button) findViewById(R.id.responder);
        Responder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startAutoResponder();
            }
            private void startAutoResponder() {
                Intent intent = new Intent(getApplication(),CallActivity.class);
                startActivity(intent);
            }
        });
    }

    private void respond(boolean ok, boolean includeLocation) {
        String okString = getString(R.string.allClearText);
        String notOkString = getString(R.string.HELPME);
        String outString = ok ? okString : notOkString;
        ArrayList<String> requestersCopy = (ArrayList<String>) requesters.clone();
        for (String to : requestersCopy)
            respond(to, outString, includeLocation);
    }

    private void respond(String to, String response, boolean locationCheckBox) {
        // Remove the target from the list of people we need to respond to.
        lock.lock();
        requesters.remove(to);
        ad.notifyDataSetChanged();
        lock.unlock();
        SmsManager sms = SmsManager.getDefault();

                // Send the message
        sms.sendTextMessage(to, null, response, null, null);
        StringBuilder sb = new StringBuilder();

             // Find the current location and send it as SMS messages if required.
        if (locationCheckBox) {
            String ls = Context.LOCATION_SERVICE;
            LocationManager lm = (LocationManager) getSystemService(ls);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location l = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (l == null)
                sb.append("Location unknown.");
            else {
                sb.append("I’m @:\\n");
                sb.append(l.toString() +  "\n");
                List<Address> addresses;
                Geocoder g = new Geocoder(getApplicationContext(),
                        Locale.getDefault());
                try {
                    addresses = g.getFromLocation(l.getLatitude(),l.getLongitude(), 1);
                    if (addresses != null) {
                        Address currentAddress = addresses.get(0);
                        if (currentAddress.getMaxAddressLineIndex() > 0) {
                            for (int i = 0;
                                 i < currentAddress.getMaxAddressLineIndex();
                                 i++) {
                                sb.append(currentAddress.getAddressLine(i));
                                sb.append("\n");
                            }
                        }
                        else {
                            if (currentAddress.getPostalCode() != null)
                                sb.append(currentAddress.getPostalCode());
                        }
                    }
                } catch (IOException e) {
                    Log.e("SMS_RESPONDER","IO Exception.", e);
                }
                ArrayList<String> locationMsgs =
                        sms.divideMessage(sb.toString());
                for (String locationMsg : locationMsgs)
                    sms.sendTextMessage(to, null, locationMsg, null, null);
            }
        }
    }
                   //Sending message
    public static final String SMS_RECEIVED ="android.provider.Telephony.SMS_RECEIVED";
    BroadcastReceiver emergencyResponseRequestReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(SMS_RECEIVED)) {
                        String allClearButtonText = getString(R.string.allClearButtonText).toLowerCase();
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            SmsMessage[] messages = new SmsMessage[pdus.length];
                            for (int i = 0; i < pdus.length; i++)
                                messages[i] =
                                        SmsMessage.createFromPdu((byte[]) pdus[i]);
                            for (SmsMessage message : messages) {
                                if (message.getMessageBody().toLowerCase().contains
                                        (allClearButtonText))
                                    requestReceived(message.getOriginatingAddress());
                            }
                        }
                    }
                }
    };
             public void requestReceived(String from) {
                   if (!requesters.contains(from)) {
                    lock.lock();
                    requesters.add(from);
                    ad.notifyDataSetChanged();
                   lock.unlock();
             }
        }
           //re-trying to send msg again
         public static final String SENT_SMS ="com.example.codetribe.thusang.SMS_SENT";
              Intent intent = new Intent(SENT_SMS);

    private BroadcastReceiver attemptedDeliveryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context _context, Intent _intent) {
                    if (_intent.getAction().equals(SENT_SMS)) {
                        if (getResultCode() != Activity.RESULT_OK) {
                            String recipient = _intent.getStringExtra("recipient");
                            requestReceived(recipient);
                        }
                    }
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(SMS_RECEIVED);
        registerReceiver(emergencyResponseRequestReceiver, filter);
        IntentFilter attemptedDeliveryfilter = new IntentFilter(SENT_SMS);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(emergencyResponseRequestReceiver);
    }
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.camVid) {
            captureImage();
            recordVideo();
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
            return (true);
        }
        else if(item.getItemId()==R.id.setting){
             signOut();
           return (true);
       }
        else if (item.getItemId()==R.id.search) {
            onSearchRequested();
            return (true);
        }
            else if (item.getItemId()==sms) {
            startActivity(new Intent(MainActivity.this, MessageActivity.class));
               sendSMS();
                return(true);
        }
        else if (item.getItemId()==call){
            startActivity(new Intent(MainActivity.this, CallActivity.class));
            return true;
        }
        return(super.onOptionsItemSelected(item));

    }

    private void signOut() {

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

            dialog.setMessage(" You want to sign out");


            dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });

            dialog.show();
    }

    private void captureImage() {
    }

    private void recordVideo(){}


    private void sendSMS() {
        String[] PROJECTION = new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String[] ARGS = {String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};
        final Cursor c = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PROJECTION, ContactsContract.CommonDataKinds.Phone.TYPE + "=?",
                ARGS, ContactsContract.Contacts.DISPLAY_NAME);

       //  new AlertDialog.Builder(this).setTitle("Pick a Person").setCursor(c, onSMSClicked,
        //                 ContactsContract.Contacts.DISPLAY_NAME).show();

          DialogInterface.OnClickListener onSMSClicked =   new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int position) {
                          c.moveToPosition(position);
                          noReallySendSMS(c.getString(2));
                      }
                  };
          new AlertDialog.Builder(this)
                  .setTitle("Pick a Person")
                  .setCursor(c, onSMSClicked, ContactsContract.Contacts.DISPLAY_NAME)
                  .show();
      }
   private void noReallySendSMS(String phone) {
        StringBuilder sb=new StringBuilder("We are going to ");

     // sb.append(names.getText());
        sb.append(" at ");
     //   sb.append(address.getText());
        sb.append(" for lunch!");
       SmsManager.getDefault().sendTextMessage(phone, null, sb.toString(), null, null);
      }

    @Override
    public void onClick(View v) {

    }
}
