package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.runMode;
import com.si_ware.neospectra.Global.GlobalVariables.wavelengthCorrection;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.io.File;
import java.util.Arrays;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra;
import static com.si_ware.neospectra.Global.GlobalVariables.gApodizationFunction;
import static com.si_ware.neospectra.Global.GlobalVariables.gCorrectionMode;
import static com.si_ware.neospectra.Global.GlobalVariables.gFftPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gInterpolationPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsFftEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsInterpolationEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainSettings;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainValue;
import static com.si_ware.neospectra.Global.GlobalVariables.gRunMode;
import static com.si_ware.neospectra.Global.GlobalVariables.measurmentsViewCaller;
import static com.si_ware.neospectra.Global.GlobalVariables.progressBarPosition;
import static com.si_ware.neospectra.Global.GlobalVariables.scanTime;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.rotateProgressBar;
import static com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage;

public class SuperUserActivity extends NavDrawerActivity {

    DrawerLayout drawer;
    TextView mBatteryLog, mP3_ID_Value, mTemperatureValue;
    EditText mScannerIDValue;
    Button mScannerIDButton;

    Thread temperatureThread;
    int temperaturePeriod = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_superuser, null, false);

        drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        mP3_ID_Value = findViewById(R.id.P3_ID_Value);
        mTemperatureValue = findViewById(R.id.temperatureValue);

        mBatteryLog = findViewById(R.id.BatteryLog);
        mBatteryLog.setMovementMethod(new ScrollingMovementMethod());

        mScannerIDValue = findViewById(R.id.ScannerIDValue);
        mScannerIDButton = findViewById(R.id.ScannerIDButton);
        mScannerIDButton.setOnClickListener(v->{
            if(bluetoothAPI != null)
            {
                bluetoothAPI.setScannerID(Long.parseLong(mScannerIDValue.getText().toString()));
            }
        });

        temperatureThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bluetoothAPI != null) {
                        bluetoothAPI.sendTemperatureRequest();
                    }
                    try {
                        Thread.sleep(temperaturePeriod);
                    }catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                        break;
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent iMain = new Intent(this, HomeActivity.class);
        startActivity(iMain);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));

        if(bluetoothAPI != null)
        {
            bluetoothAPI.sendP3_ID_Request();
        }
        temperatureThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        temperatureThread.interrupt();
    }

    // Handle broadcast receiver
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String intentName = intent.getStringExtra("iName");
            System.out.println("Action: " + intent.getAction());
            switch (intentName) {
                // Case temperature sensor reading is requested
                case "Temperature":{
                    logMessage("SuperUserActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    mTemperatureValue.setText(Long.toString(intent.getLongExtra("data", 0)));
                    break;
                }
                // Case P3_ID is requested
                case "P3_ID": {
                    logMessage("SuperUserActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");

                    mP3_ID_Value.setText(Long.toString(intent.getLongExtra("data", 0)));
                    break;
                }
                // Case battery information is requested
                case "Battery_info": {
                    logMessage("SuperUserActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n");

                    String batteryLog = intent.getStringExtra("data");
                    if (batteryLog != null)
                    {
                        mBatteryLog.append(batteryLog);
                        mBatteryLog.append("\n");
                        mBatteryLog.append("----------------------\n");
                    }
                    break;
                }
                default:

            }
        }
    };
}
