package com.si_ware.neospectra.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends NavDrawerActivity implements
        View.OnClickListener {

    @Nullable
    private Fragment myFragment;
    DrawerLayout drawer;

    @NonNull
    private String TAG = "Main Activity";
    private Context mContext;

    /* To prevent user from asking for the same process twice*/
    private boolean isWaitingForSensorReading = false;
    private boolean isStopEnabled = false;
    private boolean isWaitingForBackGroundReading = false;

    public int backgroundScanTime = 2;
    ScanPresenter scanPresenter;

    public static boolean error_sensor_reading = false;
    FloatingActionButton btnScan;
    FloatingActionButton btnBackground;
    TextView text_scan;
    public boolean isScanBG = false;
    private Button btnViewScan;
    private TextView tv_progressValue;
    private SeekBar mSeekBar;
    private EditText tx_numberOfRuns;
    private TextView tv_progressCount;
    private TextView textScan;
    private int notifications_count = 0;
    private int count = 1;
    private ProgressBar pbProgressBar;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);


        drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        // Create new instance from bluetoothApi if it null
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(this, mContext);
        }
        if(bluetoothAPI != null)
        {
            bluetoothAPI.setHomeContext(this.mContext);
            bluetoothAPI.sendMemoryRequest();
            bluetoothAPI.sendBatRequest();
        }

        mContext = this;

        scanPresenter = new ScanPresenter();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // Get all needed configuration settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        gRunMode = preferences.getString("run_mode", runMode.Single_Mode.toString());
        gIsInterpolationEnabled = preferences.getBoolean("linear_interpolation_switch", false);
        gInterpolationPoints = preferences.getString("data_points", pointsCount.points_257.toString());
        gIsFftEnabled = preferences.getBoolean("fft_settings_switch", false);
        gApodizationFunction = preferences.getString("apodization_function", apodization.Boxcar.toString());
        gFftPoints = preferences.getString("fft_points", zeroPadding.points_8k.toString());
        gOpticalGainSettings = preferences.getString("optical_gain_settings", "Default");
        gOpticalGainValue = preferences.getInt(gOpticalGainSettings, 0);
        gCorrectionMode = preferences.getString("wavelength_correction", wavelengthCorrection.Self_Calibration.toString());

        btnScan = findViewById(R.id.fab_scan);
        btnBackground = findViewById(R.id.fab_stop);

        mSeekBar = findViewById(R.id.seek_scantime);
        textScan = findViewById(R.id.text_scan);
        textScan.setText("Scan");
        mSeekBar.setProgress(scanTime);
        tv_progressValue = findViewById(R.id.tv_progress);
        tv_progressValue.setText("" + scanTime);
        // Adjust progress value if scan time changed from its default value
        if (scanTime != 2) {
            tv_progressValue.setX(progressBarPosition / 2);
        }

        pbProgressBar = findViewById(R.id.progressBarMain);
        pbProgressBar.setVisibility(View.INVISIBLE);
        btnViewScan = findViewById(R.id.button_viewScan);
        tx_numberOfRuns = findViewById(R.id.tx_numberOfRuns);
        btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
        btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
        btnBackground.setEnabled(true);

        // Disable progress count
        tv_progressCount = findViewById(R.id.countProgress);
        tv_progressCount.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
        tv_progressCount.setEnabled(false);


        ArrayAdapter<CharSequence> bluetoothSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.BLE_Services, android.R.layout.simple_spinner_item);
        bluetoothSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        text_scan = findViewById(R.id.text_scan);

        // This is the listener of scan button
        btnScan.setOnClickListener(v -> {
            if (textScan.getText() == "Scan") {

                if (backgroundScanTime < scanTime) {
                    logMessage(TAG, "Material Scan time is greater than reference material scan time ");
                    showAlertMessage(mContext, "Error", "Material scan time should be less than or equal reference scan time");
                    return;
                }
                btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnViewScan.setEnabled(false);
                isScanBG = false;
                textScan.setText("Stop");
                btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnBackground.setEnabled(false);
                if (Double.parseDouble(tx_numberOfRuns.getText().toString()) > 1) {
                    tv_progressCount.setEnabled(true);
                    pbProgressBar.setVisibility(View.VISIBLE);
                    rotateProgressBar(mContext, pbProgressBar);
                }
                if (count == 1)
                    tv_progressCount.setText("1/" + tx_numberOfRuns.getText().toString());
                tv_progressCount.setTextColor(Color.BLACK);

                // Request scan reading from the device
                sendAbsorbanceCommand();

            } else {
                // Handling stop request
                btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnScan.setEnabled(false);
                btnViewScan.setEnabled(false);
                tv_progressCount.setEnabled(false);
                pbProgressBar.setVisibility(View.INVISIBLE);
                btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                btnBackground.setEnabled(false);
                isStopEnabled = true;


            }
        });

        // This is the listener of background reading request
        btnBackground.setOnClickListener(v -> {

            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
            btnScan.setEnabled(false);
            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
            btnViewScan.setEnabled(false);
            isScanBG = true;
            btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
            btnBackground.setEnabled(false);
            tv_progressCount.setEnabled(false);
            tv_progressCount.setText("");
            backgroundScanTime = scanTime;
            pbProgressBar.setVisibility(View.INVISIBLE);

            // Request background reading from device
            sendBackgroundCommand();

        });

        // This is the scan time seekBar listener
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mSeekBar.getProgress() < 1) {
                    mSeekBar.setProgress(1);
                    return;
                }
                // Update scan time
                scanTime = progress;
                int val = (int) ((double) (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / (double) (seekBar.getMax() * 1.15));
                tv_progressValue.setText("" + progress);
                progressBarPosition = seekBar.getX() + val + seekBar.getThumbOffset() / 2;

                //Update GUI position
                tv_progressValue.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

        });

        //Start Results Activity
        btnViewScan.setOnClickListener(v -> {
                    measurmentsViewCaller = MainActivity.class;
                    Intent intent = new Intent(this, ResultsActivity.class);
                    startActivity(intent);
                }

        );


    }

    private void updateProgressValue(int value) {
        String[] strings = tv_progressCount.getText().toString().split("/");

        strings[0] = String.valueOf(value);
        tv_progressCount.setText(strings[0] + "/" + strings[1]);
        tv_progressCount.setTextColor(Color.BLACK);

    }


    @Override
    public void onBackPressed() {

        android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                .Builder(mContext);
        myAlert.setTitle("Disconnect");
        myAlert.setMessage("Are you sure you want to disconnect the device?");
        myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (bluetoothAPI != null) bluetoothAPI.disconnectFromDevice();
                Intent iMain = new Intent(mContext, ConnectActivity.class);
                iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(iMain);
            }
        });

        myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        myAlert.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();

        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.INTENT_ACTION));

        if (bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        // Unregister the receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    // Handling of broadcast receiver
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String intentName = intent.getStringExtra("iName");
            switch (intentName) {
                //Case data is received successfully
                case "sensorNotification_data":
                    gotSensorReading(intent);
                    logMessage(TAG, "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + Arrays.toString(intent.getDoubleArrayExtra("data")) + "\n");
                    break;
                // Case sensor notification with failure
                case "sensorNotification_failure":
                    gotSensorReading(intent);
                    logMessage(TAG, "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + String.valueOf(intent.getIntExtra("data", 0)) + "\n");
                    int errorCode = intent.getIntExtra("data", 0);
                    enableButtonAndView(true);
                    notifications_count = 0;
                    isWaitingForBackGroundReading = false;
                    isWaitingForSensorReading = false;
                    showAlertMessage(mContext, "Error", "Error " + String.valueOf(0x000000FF & errorCode) + " occurred during measurement!");
                    break;
                case "sensorWriting":

                    break;
                //Case device is disconnected
                case "Disconnection_Notification":
                    endActivity();
                    break;

                default:
                    Log.v(TAG + "intent", "Got unknown broadcast intent");
            }
        }
    };


    // Enable/Disable main activity buttons
    private void enableButtonAndView(boolean enable) {
        if (enable) {
            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnScan.setEnabled(true);
            textScan.setText("Scan");
            btnViewScan.setEnabled(true);
            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnBackground.setEnabled(true);
            btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));

        } else {
            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
            btnScan.setEnabled(false);
            btnViewScan.setEnabled(false);

        }
    }


    @Override
    public File getFileStreamPath(String name) {
        return super.getFileStreamPath(name);
    }


    // Handling receiving data from sensor
    private void gotSensorReading(Intent intent) {
        boolean isNotificationSuccessful = intent.getBooleanExtra("isNotificationSuccess", false);
        String notificationReason = intent.getStringExtra("reason");
        String errorMessage = intent.getStringExtra("err");

        /* If the notification is unsuccessful */
        if (!isNotificationSuccessful) {
            tv_progressCount.setEnabled(false);
            tv_progressCount.setText("");
            pbProgressBar.setVisibility(View.INVISIBLE);

            logMessage(notificationReason, errorMessage);
            return;
        }

        /* If an error occured */
        if (!notificationReason.equals("gotData")) {
            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnScan.setEnabled(true);
            btnViewScan.setEnabled(true);

            notifications_count = 0;
            return;
        }

        // Number of the received notifications
        notifications_count++;

        if (isScanBG) {
            if ((notifications_count % 3) == 0) {
                enableButtonAndView(true);
                isWaitingForBackGroundReading = false;
                textScan.setText("Scan");
                btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnBackground.setEnabled(true);
            }
            return;
        }

        // Get readings
        double[] reading = intent.getDoubleArrayExtra("data");

        if (reading == null) {
            logMessage(TAG, "Reading is NULL.");
            enableButtonAndView(true);
            notifications_count = 0;
            return;
        }

        // The array constructed from two arrays have the same length, Y, then X.
        int middleOfArray = reading.length / 2;
        double[] x_reading = new double[middleOfArray],
                y_reading = new double[middleOfArray];
        // split the main array to two arrays, Y & X
        for (int i = 0; i < middleOfArray; i++) {
            //Added this fix for the inconsistency in size of received data
            y_reading[i] = reading[i];
            x_reading[i] = reading[middleOfArray + i];
        }

        // Prepare the data to get ArrayRealVector with length 314 item,
        // and set its value to the singleton sensor reading model.
        if ((y_reading.length > 0) && (x_reading.length > 0)) {
            dbReading newReading = new dbReading();
            newReading.setReading(y_reading, x_reading);
            // Add the taken read to global ArrayList which holds all the taken readings
            gAllSpectra.add(newReading);
            enableButtonAndView(true);
            isWaitingForSensorReading = false;


            Double numberOfRuns = Double.parseDouble(tx_numberOfRuns.getText().toString());

            // Enable Stop action button in case there are a multiple number of runs
            if (count < numberOfRuns) {
                if (isStopEnabled) {
                    count = 1;
                    tv_progressCount.setEnabled(false);
                    tv_progressCount.setText("");
                    pbProgressBar.setVisibility(View.INVISIBLE);
                    isStopEnabled = false;
                    return;
                }

                btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnScan.setEnabled(true);
                btnViewScan.setEnabled(true);

                btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnBackground.setEnabled(true);
                textScan.setText("Scan");

                count++;
                updateProgressValue(count);
                // Press scan button again as it is a continues mode
                btnScan.performClick();
            } else// Handle only one run
            {
                tv_progressCount.setEnabled(false);
                tv_progressCount.setText("");
                pbProgressBar.setVisibility(View.INVISIBLE);
                tv_progressCount.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));

                btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnScan.setEnabled(true);
                btnViewScan.setEnabled(true);

                btnBackground.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnBackground.setEnabled(true);
                textScan.setText("Scan");
                count = 1;
                Toast.makeText(getApplicationContext(), "Scan is complete", Toast.LENGTH_LONG).show();
            }
        }

        logMessage(TAG, "Sensor Reading Length = " + reading.length);

    }

    private void sendAbsorbanceCommand() {
        System.out.println("inside sendAbsorbanceCommand");
        if (isWaitingForSensorReading) {
            logMessage(TAG, "Still waiting for sensor reading ... ");
            return;
        }
        isWaitingForSensorReading = true;
        askForSensorReading();
    }

    private void sendBackgroundCommand() {
        System.out.println("inside sendBackgroundCommand");
        if (isWaitingForBackGroundReading) {
            logMessage(TAG, "Still waiting for sensor reading ... ");
            return;
        }
        isWaitingForBackGroundReading = true;
        askForBackGroundReading();
    }


    private void askForSensorReading() {
        if (scanPresenter == null) {
            scanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(this,
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");

            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }
        System.out.println("error_sensor_reading= " + error_sensor_reading);

        if (error_sensor_reading == true) {
            showAlertMessage(this,
                    "Error in sensor reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            return;
        }
        scanPresenter.requestSensorReading(mSeekBar.getProgress());

    }

    private void askForBackGroundReading() {
        if (scanPresenter == null) {
            scanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(this,
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }
        System.out.println("error_sensor_reading= " + error_sensor_reading);
        if (error_sensor_reading == true) {
            System.out.println("error_sensor_reading==true");
            showAlertMessage(this,
                    "Error in background reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnScan.setEnabled(false);
            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnViewScan.setEnabled(false);
            return;
        }

        scanPresenter.requestBackgroundReading(mSeekBar.getProgress());
    }

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(MainActivity.this, ConnectActivity.class);
        startActivity(mIntent);
    }


}
