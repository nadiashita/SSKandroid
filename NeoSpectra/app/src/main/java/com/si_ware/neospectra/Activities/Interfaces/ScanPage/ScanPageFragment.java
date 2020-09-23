package com.si_ware.neospectra.Activities.Interfaces.ScanPage;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.si_ware.neospectra.Activities.ConnectActivity;
import com.si_ware.neospectra.Activities.HomeActivity;
import com.si_ware.neospectra.Activities.IntroActivity;
import com.si_ware.neospectra.Activities.MainActivity;
import com.si_ware.neospectra.Activities.MainPage;
import com.si_ware.neospectra.Activities.ResultsActivity;
import com.si_ware.neospectra.Activities.SettingsActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.filterDate;
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

public class ScanPageFragment extends Fragment {

    @NonNull
    private String TAG = "Main Activity";
    private Context mContext;

    GraphView mGraphView;
    CardView btnRefresh, btnBackground, btnScan, btnProcess;
    Spinner edtResolution, edtOptical;
    public boolean isScanBG = false;
    public int backgroundScanTime = 2;
    private ProgressBar pbProgressBar;
    private boolean isWaitingForBackGroundReading = false;
    ScanPresenter scanPresenter;
    public static boolean error_sensor_reading = false;
    private SeekBar mSeekBar;
    private TextView textScan;
    private EditText tx_numberOfRuns;
    private int count = 1;
    private boolean isWaitingForSensorReading = false;
    private boolean isStopEnabled = false;
    private TextView tv_progressCount;
    private TextView tv_progressValue;
    private double maxValue = 0;
    private static int measurementCount_Spectroscopy = 0;
    private static int colors[] = {0xFFFF0000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF007F, 0xFF7F00FF, 0xFFFF00FF, 0xFFFFFF00, 0xFF007FFF, 0xFFFF7F00, 0xFF00FF7F, 0xFF7FFF00};
    private int notifications_count = 0;


    String[] Resolution = {"1", "2", "3"};
    String[] Optical = {"2", "4", "6", "8", "10"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);

        Toolbar toolbar = view.findViewById(R.id.titlebar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(getActivity(), mContext);
        }
        mContext = getActivity();

        scanPresenter = new ScanPresenter();

        ArrayAdapter<String> itemResolution = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Resolution);
        ArrayAdapter<String> itemOptical = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Optical);

        mGraphView = view.findViewById(R.id.graph);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnBackground = view.findViewById(R.id.btnBackground);
        btnScan = view.findViewById(R.id.btnScan);
        btnProcess = view.findViewById(R.id.btnProcess);
        edtResolution = view.findViewById(R.id.edtResolution);
        edtOptical = view.findViewById(R.id.edtOptical);
        pbProgressBar = view.findViewById(R.id.progressBarMain);
        textScan = view.findViewById(R.id.text_scan);
        textScan.setText("Scan");
        tx_numberOfRuns = view.findViewById(R.id.tx_numberOfRuns);
        tv_progressCount = view.findViewById(R.id.countProgress);
//        mSeekBar = view.findViewById(R.id.seek_scantime);
//        mSeekBar.setProgress(scanTime);
        tv_progressValue = view.findViewById(R.id.tv_progress);
        tv_progressValue.setText("" + scanTime);

        edtResolution.setAdapter(itemResolution);
        edtOptical.setAdapter(itemOptical);

        btnRefresh.setEnabled(false);
        btnRefresh.setOnClickListener(null);
        btnRefresh.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnScan.setEnabled(false);
        btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnScan.setOnClickListener(null);
        btnProcess.setEnabled(false);
        btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnProcess.setOnClickListener(null);


        // Get all needed configuration settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        gRunMode = preferences.getString("run_mode", GlobalVariables.runMode.Single_Mode.toString());
        gIsInterpolationEnabled = preferences.getBoolean("linear_interpolation_switch", false);
        gInterpolationPoints = preferences.getString("data_points", GlobalVariables.pointsCount.points_257.toString());
        gIsFftEnabled = preferences.getBoolean("fft_settings_switch", false);
        gApodizationFunction = preferences.getString("apodization_function", GlobalVariables.apodization.Boxcar.toString());
        gFftPoints = preferences.getString("fft_points", GlobalVariables.zeroPadding.points_8k.toString());
        gOpticalGainSettings = preferences.getString("optical_gain_settings", "Default");
        gOpticalGainValue = preferences.getInt(gOpticalGainSettings, 0);
        gCorrectionMode = preferences.getString("wavelength_correction", GlobalVariables.wavelengthCorrection.Self_Calibration.toString());

        ArrayAdapter<CharSequence> bluetoothSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
                R.array.BLE_Services, android.R.layout.simple_spinner_item);
        bluetoothSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Adjust progress value if scan time changed from its default value
//        if (scanTime != 2) {
//            tv_progressValue.setX(progressBarPosition / 2);
//        }

        pbProgressBar.setVisibility(View.INVISIBLE);
//        btnViewScan = findViewById(R.id.button_viewScan);
//        btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
//        btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
//        btnBackground.setEnabled(true);

        // Disable progress count
        tv_progressCount.setBackgroundColor(Color.parseColor("#0A376A"));
        tv_progressCount.setEnabled(false);


//        ArrayAdapter<CharSequence> bluetoothSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
//                R.array.BLE_Services, android.R.layout.simple_spinner_item);
//        bluetoothSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi refresh
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

                bottomSheetDialog.setCanceledOnTouchOutside(false);

                LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_refresh);
                CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
                CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
                bottom_sheet_refresh.setVisibility(View.VISIBLE);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi yes pada bottom sheet
//                        getActivity().finish();
//                        startActivity(getActivity().getIntent());
                        maxValue = 0;
                        if (!mGraphView.getSeries().isEmpty())
                            mGraphView.removeAllSeries();
                        mGraphView.getViewport().setMinY(90);
                        mGraphView.getViewport().setMaxY(110);
                        mGraphView.getViewport().setMinX(1100);
                        mGraphView.getViewport().setMaxX(2650);
                        mGraphView.getViewport().setScalable(false);
                        mGraphView.getViewport().setScrollable(false);
                        mGraphView.getViewport().setScalableY(false);
                        mGraphView.getViewport().setScrollableY(false);
                        gAllSpectra.clear();

                        measurementCount_Spectroscopy = 0;
                        tx_numberOfRuns.setText("1");
                        btnRefresh.setEnabled(false);
                        btnRefresh.setOnClickListener(null);
                        btnRefresh.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnScan.setEnabled(false);
                        btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnScan.setOnClickListener(null);
                        btnProcess.setEnabled(false);
                        btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
                        btnProcess.setOnClickListener(null);
                        btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                        btnBackground.setEnabled(true);
                        bottomSheetDialog.hide();

                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi cancel bottom sheet
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fungsi button background
                tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());

                btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
                btnScan.setEnabled(false);
//                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
//                btnViewScan.setEnabled(false);
                isScanBG = true;

                tv_progressCount.setEnabled(false);
                tv_progressCount.setText("");
                backgroundScanTime = scanTime;
                pbProgressBar.setVisibility(View.INVISIBLE);

                // Request background reading from device
                sendBackgroundCommand();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fungsi scan
//                tx_numberOfRuns.setText((Integer) edtResolution.getSelectedItem());

                tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());

                if (textScan.getText() == "Scan") {

                    if (backgroundScanTime < scanTime) {
                        logMessage(TAG, "Material Scan time is greater than reference material scan time ");
                        showAlertMessage(mContext, "Error", "Material scan time should be less than or equal reference scan time");
                        return;
                    }
//                    btnScan.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.Button_Disabled));
//                    btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
//                    btnViewScan.setEnabled(false);
                    isScanBG = false;
                    textScan.setText("Stop");
                    btnScan.setCardBackgroundColor(Color.parseColor("#ff1d21"));

                    btnRefresh.setEnabled(true);
                    btnRefresh.setCardBackgroundColor(Color.parseColor("#1D86FF"));
                    btnProcess.setEnabled(true);
                    btnProcess.setCardBackgroundColor(Color.parseColor("#1D86FF"));

                    btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                    btnBackground.setEnabled(false);
                    btnBackground.setOnClickListener(null);
                    if (Double.parseDouble(tx_numberOfRuns.getText().toString()) > 1) {
//                        tv_progressCount.setEnabled(true);
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
                    btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
//                    btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
                    btnScan.setEnabled(false);
//                    btnViewScan.setEnabled(false);
                    tv_progressCount.setEnabled(false);
                    pbProgressBar.setVisibility(View.INVISIBLE);
                    btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
                    btnBackground.setEnabled(false);
                    isStopEnabled = true;


                }
            }
        });

        btnProcess.setOnClickListener(v -> {
//                fungsi process
            tx_numberOfRuns.setText(edtResolution.getSelectedItem().toString());
            final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getActivity());
            bottomSheetDialog2.setContentView(R.layout.bottom_sheet_dialog);

            bottomSheetDialog2.setCanceledOnTouchOutside(false);

            LinearLayout bottom_sheet_process = bottomSheetDialog2.findViewById(R.id.bottom_sheet_process);
            CardView btn_yes2 = bottomSheetDialog2.findViewById(R.id.btn_yes2);
            CardView btn_cancel2 = bottomSheetDialog2.findViewById(R.id.btn_cancel2);
            bottom_sheet_process.setVisibility(View.VISIBLE);

            btn_yes2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi yes pada bottom sheet
                    displayGraph();
                    bottomSheetDialog2.hide();

//                        measurmentsViewCaller = MainPage.class;
//                        Intent intent = new Intent(getActivity(), ResultsActivity.class);
//                        startActivity(intent);
                }
            });

            btn_cancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        fungsi cancel bottom sheet
                    bottomSheetDialog2.hide();
                    tx_numberOfRuns.setText("1");
                }
            });
            bottomSheetDialog2.show();
        });

    }


    private void updateProgressValue(int value) {
        String[] strings = tv_progressCount.getText().toString().split("/");

        strings[0] = String.valueOf(value);
        tv_progressCount.setText(strings[0] + "/" + strings[1]);
        tv_progressCount.setTextColor(Color.BLACK);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
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
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
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
            btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
            btnScan.setEnabled(true);
            textScan.setText("Scan");
//            btnViewScan.setEnabled(true);
//            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnBackground.setEnabled(true);
            btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
        } else {
            btnScan.setCardBackgroundColor(Color.parseColor("#0A376A"));
            btnScan.setEnabled(false);
//            btnViewScan.setEnabled(false);

        }
    }


//    @Override
//    public File getFileStreamPath(String name) {
//        return super.getFileStreamPath(name);
//    }


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
            btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
//            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnScan.setEnabled(true);
//            btnViewScan.setEnabled(true);

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
                btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
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

                btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
//                btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
                btnScan.setEnabled(true);
//                btnViewScan.setEnabled(true);

                btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnBackground.setEnabled(true);

                btnRefresh.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnRefresh.setEnabled(true);

                btnProcess.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnProcess.setEnabled(true);
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
                tv_progressCount.setBackgroundColor(Color.parseColor("#0A376A"));

                btnScan.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnScan.setEnabled(true);
//                btnViewScan.setEnabled(true);

                btnBackground.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnBackground.setEnabled(true);

                btnRefresh.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnRefresh.setEnabled(true);

                btnProcess.setCardBackgroundColor(Color.parseColor("#1d86ff"));
                btnProcess.setEnabled(true);
                textScan.setText("Scan");
                count = 1;
                displayGraph();

                Toast.makeText(getContext(), "Scan is complete", Toast.LENGTH_LONG).show();
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
            showAlertMessage(getActivity(),
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");

            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }
        System.out.println("error_sensor_reading= " + error_sensor_reading);

        if (error_sensor_reading == true) {
            showAlertMessage(getActivity(),
                    "Error in sensor reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            return;
        }
        scanPresenter.requestSensorReading(2);

    }

    private void askForBackGroundReading() {
        if (scanPresenter == null) {
            scanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(getActivity(),
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
            showAlertMessage(getActivity(),
                    "Error in background reading",
                    "Sorry,You have a problem with your Bluetooth version!");
            btnScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
            btnScan.setEnabled(false);
//            btnViewScan.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
//            btnViewScan.setEnabled(false);
            return;
        }

        btnRefresh.setEnabled(false);
        btnRefresh.setOnClickListener(null);
        btnRefresh.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnScan.setEnabled(true);
        btnScan.setCardBackgroundColor(Color.parseColor("#1D86FF"));
        btnProcess.setEnabled(false);
        btnProcess.setOnClickListener(null);
        btnProcess.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnBackground.setCardBackgroundColor(Color.parseColor("#0A376A"));
        btnBackground.setEnabled(false);
        btnBackground.setOnClickListener(null);
        scanPresenter.requestBackgroundReading(2);
    }

    private void endActivity() {
        bluetoothAPI = null;
        Intent mIntent = new Intent(getActivity(), ConnectActivity.class);
        startActivity(mIntent);
    }

    public void displayGraph() {
        for (int i = 0; i < gAllSpectra.size(); ++i) {
            dbReading sensorReading = gAllSpectra.get(i);
            ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

            if (sensorReading != null) {
                if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                    double[] xVals = sensorReading.getXReading();
                    double[] yVals = sensorReading.getYReading();

                    for (int j = xVals.length - 1; j >= 0; --j) {
                        dataPoints.add(new DataPoint(1e7 / xVals[j], yVals[j] * 100));
                        if (maxValue < yVals[j] * 100)
                            maxValue = yVals[j] * 100;
                    }

                    DataPoint dataPointsArray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries(dataPointsArray);
                    series.setThickness(4);
                    series.setColor(colors[i % 12]);
                    series.setTitle("Meas. " + String.valueOf(i + 1));
                    mGraphView.addSeries(series);


                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            AlertDialog.Builder myAlert = new AlertDialog.Builder(mContext);
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    // Request of bluetooth disconnection
                    if (bluetoothAPI != null) {
                        bluetoothAPI.disconnectFromDevice();

                        Intent iMain = new Intent(mContext, IntroActivity.class);
                        iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iMain);
                    }

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
        return super.onOptionsItemSelected(item);
    }
}