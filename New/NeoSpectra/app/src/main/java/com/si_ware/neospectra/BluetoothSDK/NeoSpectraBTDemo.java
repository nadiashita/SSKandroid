package com.si_ware.neospectra.BluetoothSDK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.R;

import java.util.ArrayList;
import java.util.List;

public class NeoSpectraBTDemo extends AppCompatActivity {

    // =============================================================================================
    // Handler to the P3 API class
    // =============================================================================================
    @NonNull
    private SWS_P3API mP3API = new SWS_P3API(this, this);
    private List<SWS_P3BLEDevice> mBTDeviceList;
    private Context mThisContext;
    // =============================================================================================
    // UI Parameters
    // =============================================================================================
    private Switch mBTStatusSwitch;
    private Button mBTScanButton, mConnectButton, mSetNotificationButton, mSendPacketButton;
    private Spinner mDeviceListSpinner;
    private ProgressBar mScanProgress;
    private TextView mDeviceNameTextView, mDeviceMacAddressTextView, mDeviceRSSITextView, mAppStatus;
    private int mCurrentDeviceID = -1;
    private ImageView mNotificationSet;

    // =============================================================================================
    // UI Parameters from the alert dialog
    // =============================================================================================
    private Spinner spnrSelectCmd, spnrPoints, spnrOpticalGain, spnrApodization, spnrZeroPadding, spnrRunMode;
    private EditText txtScanTime;
    private Button btnSendPacket, btnCancelDialog;
    private TextView txtViewPacketContent_TV;
    private AlertDialog mDialog;

    // =============================================================================================
    // Listen to the recieved bytes
    // =============================================================================================
    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            double[] recBytes = intent.getDoubleArrayExtra("P3_data");
            Log.i(getClass().getSimpleName(), "##### Callback Received - " + String.valueOf(recBytes.length));
        }
    }

    private MyBroadcastReceiver mNotificationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neo_spectra_btdemo);

        mThisContext = this;

        connectControlsToLayout();


        // initialize the notification listener broadcast receiver.
        mNotificationListener = new MyBroadcastReceiver();
        this.registerReceiver(mNotificationListener, new IntentFilter(GlobalVariables.INTENT_ACTION));


        // --------------------------------------------------------------------------------
        // Determine initial conditions of different UI elements
        if (mP3API.isBluetoothEnabled()) {
            mBTStatusSwitch.setChecked(true);
            mBTScanButton.setEnabled(true);
        } else {
            mBTStatusSwitch.setChecked(false);
            mBTScanButton.setEnabled(false);
        }

        mScanProgress.setVisibility(View.INVISIBLE);
        mDeviceNameTextView.setVisibility(View.INVISIBLE);
        mDeviceMacAddressTextView.setVisibility(View.INVISIBLE);
        mDeviceRSSITextView.setVisibility(View.INVISIBLE);
        mConnectButton.setVisibility(View.INVISIBLE);

        mSetNotificationButton.setVisibility(View.INVISIBLE);
        mSendPacketButton.setVisibility(View.INVISIBLE);

        mNotificationSet.setVisibility(View.INVISIBLE);

        mAppStatus.setText("Ready");


        // --------------------------------------------------------------------------------
        // Request Permission for loacation
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            mP3API.getLocationPermissions();
        } else {
            //do your work
        }

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the bluetooth enable/disable switch
        mBTStatusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Bluetooth is disabled .. enable it
                    Intent mEnBT = mP3API.enableBluetooth();
                    int REQUEST_ENABLE_BT = 1;
                    startActivityForResult(mEnBT, REQUEST_ENABLE_BT);
                    mBTScanButton.setEnabled(true);
                } else {
                    // Bluetooth is enabled .. disable it
                    mP3API.disableBluetooth();
                    mBTScanButton.setEnabled(false);
                }
            }
        });

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the requestSensorReading button
        mBTScanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAppStatus.setText("Scanning ... ");
                mScanProgress.setVisibility(View.VISIBLE);
                // requestSensorReading for BT devices
                mBTDeviceList = mP3API.scanAvailableDevices();


                // show results for selection
                List<String> spinnerArray = new ArrayList<String>();

                int counter = 0;
                for (SWS_P3BLEDevice device : mBTDeviceList) {
                    counter++;
                    if (device.getDeviceName() != null)
                        spinnerArray.add(device.getDeviceName());
                    else if (spinnerArray.size() > 0)
                        spinnerArray.remove(counter);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (mThisContext, android.R.layout.simple_spinner_item, spinnerArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner sItems = findViewById(R.id.devicesList_spinner);
                sItems.setAdapter(adapter);

                if (mBTDeviceList.size() > 0)
                    mScanProgress.setVisibility(View.INVISIBLE);

                mAppStatus.setText("Found Devices ... ");
            }
        });

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the Connect button
        mDeviceListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // postition have the item ID
                // show the information of the device selected
                mDeviceNameTextView.setText("Name: " + mBTDeviceList.get(position).getDeviceName());
                mDeviceMacAddressTextView.setText("Mac Address: " + mBTDeviceList.get(position).getDeviceMacAddress());
                mDeviceRSSITextView.setText("RSSI: " + mBTDeviceList.get(position).getDeviceRSSI());
                // make the text fields invisible
                mDeviceNameTextView.setVisibility(View.VISIBLE);
                mDeviceMacAddressTextView.setVisibility(View.VISIBLE);
                mDeviceRSSITextView.setVisibility(View.VISIBLE);

                // show the connect button as well
                mConnectButton.setVisibility(View.VISIBLE);

                mCurrentDeviceID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // sometimes you need nothing here
            }
        });

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the Connect button
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mConnectButton.getText().equals("Disconnect")) {
                    mAppStatus.setText("Disconnecting ... ");
                    mP3API.disconnectFromDevice();
                    mAppStatus.setText("Ready ");
                    mConnectButton.setText("Connect");
                } else {
                    Boolean testBool = mP3API.connectToDevice(mBTDeviceList.get(mCurrentDeviceID).getDeviceInstance());

                    while (!mP3API.getCurrentConnectionStatus()) {
                        mAppStatus.setText("Connecting ... ");
                        mScanProgress.setVisibility(View.VISIBLE);
                    }

                    mAppStatus.setText("Connected to: " + mBTDeviceList.get(mCurrentDeviceID).getDeviceName());
                    mScanProgress.setVisibility(View.INVISIBLE);

                    mSetNotificationButton.setVisibility(View.VISIBLE);
                    mSendPacketButton.setVisibility(View.VISIBLE);

                    mConnectButton.setText("Disconnect");

                    mSetNotificationButton.setEnabled(true);
                    mNotificationSet.setVisibility(View.INVISIBLE);
                }


            }
        });

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the Set Notification button
        mSetNotificationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAppStatus.setText("Setting Notification ... ");

                mP3API.setNotifications();

                mNotificationSet.setVisibility(View.VISIBLE);
                mSetNotificationButton.setEnabled(false);

                mAppStatus.setText("Connected to: " + mBTDeviceList.get(mCurrentDeviceID).getDeviceName());
            }
        });

        // --------------------------------------------------------------------------------
        // Set Callback Listeners for the Send packet button
        mSendPacketButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // open the dialog to send a new packet
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(NeoSpectraBTDemo.this);
                View mView = getLayoutInflater().inflate(R.layout.packet_dialog_layout, null);

                // Connecting anc populating controls in the dialog layout
                connectControlsToDialogLayout(mView);
                populatePacketAlertDialogFields();


                // --------------------------------------------------------------------------------
                // register onClick listener to the send button at the dialog box
                btnSendPacket.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        // create a new packet instance
                        SWS_P3Packet newPacket = new SWS_P3Packet();

                        // gather information from different fields.
                        newPacket.setSWS_P3Packet_Command(spnrSelectCmd.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_ScanTime(txtScanTime.getText().toString());
                        newPacket.setSWS_P3Packet_PointsCount(spnrPoints.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_OpticalGain(spnrOpticalGain.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_Apodization(spnrApodization.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_ZeroPadding(spnrZeroPadding.getSelectedItem().toString());
                        newPacket.setSWS_P3Packet_RunMode(spnrRunMode.getSelectedItem().toString());

                        //printing the content received to the debug window
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Command());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ScanTime());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_PointsCount());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_OpticalGain());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_Apodization());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_ZeroPadding());
                        Log.d("SWS DEBUG:", newPacket.getSWS_P3Packet_RunMode());

                        // prepare the packet to be sent over the uart
                        newPacket.preparePacketContent();

                        // show packet on the bar
                        txtViewPacketContent_TV.setText(newPacket.getPacketAsText());

                        // send the values to the lowLevelInterface for sending
                        mP3API.sendPacket(newPacket.getPacketStream(), newPacket.isInterpolationMode());

                        mDialog.cancel();
                        mAppStatus.setText("Sending Packet ... ");

                        mAppStatus.setText("Connected to: " + mBTDeviceList.get(mCurrentDeviceID).getDeviceName());
                    }
                });

                btnCancelDialog.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });


                // register the view and show the dialog box on click
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    // =============================================================================================
    private void connectControlsToLayout() {
        // --------------------------------------------------------------------------------
        // Connecting controls to GUI parameters
        mBTStatusSwitch = findViewById(R.id.BTStatus_SW);
        mBTScanButton = findViewById(R.id.ScanBT_btn);
        mDeviceListSpinner = findViewById(R.id.devicesList_spinner);
        mScanProgress = findViewById(R.id.scanProgress);
        mDeviceNameTextView = findViewById(R.id.DeviceName_TV);
        mDeviceMacAddressTextView = findViewById(R.id.DeviceMAC_TV);
        mDeviceRSSITextView = findViewById(R.id.DeviceRSSI_TV);
        mConnectButton = findViewById(R.id.connect_btn);
        mAppStatus = findViewById(R.id.AppStatus_TV);
        mSetNotificationButton = findViewById(R.id.SetNotify_btn);
        mSendPacketButton = findViewById(R.id.btn_view_received_packet);
        mNotificationSet = findViewById(R.id.notification_IV);
    }

    // =============================================================================================
    private void connectControlsToDialogLayout(View mView) {
        // ===================================================================================
        // == View Components Registration
        // ===================================================================================
        btnSendPacket = mView.findViewById(R.id.btn_view_received_packet);
        btnCancelDialog = mView.findViewById(R.id.Cancel_btn);
        spnrSelectCmd = mView.findViewById(R.id.cmdSpinner);
        spnrPoints = mView.findViewById(R.id.PointsSpinner);
        spnrOpticalGain = mView.findViewById(R.id.opticalGainSpinner);
        spnrApodization = mView.findViewById(R.id.apodizationSpinner);
        spnrZeroPadding = mView.findViewById(R.id.zeroPaddingSpinner);
        spnrRunMode = mView.findViewById(R.id.runModeSpinner);
        txtScanTime = mView.findViewById(R.id.ScanTimeTextField);
        txtViewPacketContent_TV = mView.findViewById(R.id.packetContent_TV);
    }

    // =============================================================================================
    private void populatePacketAlertDialogFields() {
        // ===================================================================================
        // == Populate the spinner view elements with needed values
        // ===================================================================================
        // Commands Spinner adaptor
        ArrayAdapter<CharSequence> CmdSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.operations_spinner_content, android.R.layout.simple_spinner_item);
        CmdSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrSelectCmd.setAdapter(CmdSpinneradapter);

        // data points spinner adaptor
        ArrayAdapter<CharSequence> PointsSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.data_points_spinner_content, android.R.layout.simple_spinner_item);
        PointsSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrPoints.setAdapter(PointsSpinneradapter);

        // Optical gain spinner adaptor
        ArrayAdapter<CharSequence> OpticalGainSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.optical_gain_spinner_content, android.R.layout.simple_spinner_item);
        OpticalGainSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrOpticalGain.setAdapter(OpticalGainSpinneradapter);

        // Apodization spinner adaptor
        ArrayAdapter<CharSequence> ApodizationSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.apodization_spinner_content, android.R.layout.simple_spinner_item);
        ApodizationSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrApodization.setAdapter(ApodizationSpinneradapter);

        // Zero Padding spinner adaptor
        ArrayAdapter<CharSequence> ZeroPaddingSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.zero_padding_spinner_content, android.R.layout.simple_spinner_item);
        ZeroPaddingSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrZeroPadding.setAdapter(ZeroPaddingSpinneradapter);

        // Run Mode spinner adaptor
        ArrayAdapter<CharSequence> RunModeSpinneradapter = ArrayAdapter.createFromResource(this,
                R.array.run_mode_spinner_content, android.R.layout.simple_spinner_item);
        RunModeSpinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrRunMode.setAdapter(RunModeSpinneradapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mNotificationListener);
    }
}
