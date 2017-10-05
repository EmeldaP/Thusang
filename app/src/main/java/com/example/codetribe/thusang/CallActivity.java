package com.example.codetribe.thusang;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity {
    private TelephonyManager tele;
    Button Emergency;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);


        PhoneCallListner phoneCallListner = new PhoneCallListner();
        TelephonyManager telephonyManager = (TelephonyManager) this.
                getSystemService(context.TELEPHONY_SERVICE);
          telephonyManager.listen(phoneCallListner,PhoneCallListner.LISTEN_CALL_STATE);


        Emergency = (Button) findViewById(R.id.buttonCall);

        Emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:112"));

                if (ActivityCompat.checkSelfPermission(CallActivity.this, android.Manifest.permission.CALL_PHONE)
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
                startActivity(call);
            }
        });
    }

    private class PhoneCallListner extends PhoneStateListener {

        private boolean isPhoneCalling = false;
        String LOG_TAG = "LOGGING 123";


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (TelephonyManager.CALL_STATE_RINGING == state)
                Log.i(LOG_TAG, "RINGING ,NUMBER:" + incomingNumber);

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                Log.i(LOG_TAG, "Off hook:");
                isPhoneCalling = true;
            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {
                    Log.i(LOG_TAG, "restart app");
                    Intent intent = getBaseContext().getPackageManager().
                            getLaunchIntentForPackage(getBaseContext().getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    isPhoneCalling = false;
                }

                super.onCallStateChanged(state, incomingNumber);
            }
        }

        //tracking cellphone location change
        PhoneStateListener cellLocationListener = new PhoneStateListener() {
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {
                    GsmCellLocation gsmLocation = (GsmCellLocation) location;
                    Toast.makeText(getApplicationContext(),
                            String.valueOf(gsmLocation.getCid()),
                            Toast.LENGTH_LONG).show();
                } else if (location instanceof CdmaCellLocation) {
                    CdmaCellLocation cdmaLocation = (CdmaCellLocation) location;
                    StringBuilder sb = new StringBuilder();
                    sb.append(cdmaLocation.getBaseStationId());
                    sb.append("\\n@");
                    sb.append(cdmaLocation.getBaseStationLatitude());
                    sb.append(cdmaLocation.getBaseStationLongitude());
                    Toast.makeText(getApplicationContext(),
                            sb.toString(),
                            Toast.LENGTH_LONG).show();
                }
                tele.listen(cellLocationListener,
                        PhoneStateListener.LISTEN_CELL_LOCATION);
            }
        };
    }
}
//    private class PhoneListner {
//    }

