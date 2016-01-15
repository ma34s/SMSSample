package com.example.seino.smssample;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final String ACTION_SENT = "info.ma34s.SMS.ACTION_SENT";
    private PendingIntent mSmsResIntent;
    private SMSResponseReceiver mSmsResReceiver;

    private EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSmsResIntent = PendingIntent.getBroadcast( this, 0, new Intent(ACTION_SENT), 0);

        mEditText = (EditText)findViewById(R.id.editText);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                String destinationAddress = mEditText.toString();
                String massage = "SMS Sample TEST";
                smsManager.sendTextMessage(destinationAddress, null, massage, mSmsResIntent, null);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mSmsResReceiver);
    }

    @Override
    protected void onStart() {
        super.onResume();
        mSmsResReceiver = new SMSResponseReceiver();
        registerReceiver(mSmsResReceiver, new IntentFilter(ACTION_SENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private static final int RES_TYPE=0;    //0: use Toast 1: use Snackbar
    private void showResponse(String str)
    {
        switch(RES_TYPE) {
            case 0:
                Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                View view = getWindow().getDecorView();
                Snackbar.make(view, str, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                break;
        }
    }


    class SMSResponseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    showResponse("SMS Sent successfully");
                    break;
                case Activity.RESULT_CANCELED:
                    showResponse("SMS Canceled");
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    showResponse("Generic Failure");
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    showResponse("No Service");
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    showResponse("NULL PDU");
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    showResponse("Radio Off");
                    break;
            }
        }
    }
}