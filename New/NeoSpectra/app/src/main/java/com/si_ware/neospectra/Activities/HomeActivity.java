package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.content.pm.PackageManager.NameNotFoundException;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;

import static com.si_ware.neospectra.Global.GlobalVariables.measurmentsViewCaller;
import static com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions.RESET_FIRMWARE;
import static com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions.UPDATE_FIRMWARE;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.first_time;
import static com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra;
import static com.si_ware.neospectra.Global.GlobalVariables.gDeviceName;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.GlobalVariables.MAX_SCANNER_MEMORY;

public class HomeActivity extends NavDrawerActivity implements
        View.OnClickListener {

    Context mContext;
    Button mScanButton, mConfigureDevice,
            mDisconnectDevice, mOTASettingsButton, mViewMeasurements, mSuperuserBtn;
    ImageButton menuButton;

    TextView tv_device_name, tv_firmware_version, tv_memory, tvBattery, tvBatteryStatus;

    int superUserVisibility = View.INVISIBLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home2, null, false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        if (gDeviceName.indexOf("_") != -1)
            gDeviceName = gDeviceName.substring(gDeviceName.indexOf("_")+1);

        mContext = this;

        setContentView(R.layout.activity_home2);
        // Set all class membership
        mScanButton = findViewById(R.id.button_scan);
        mSuperuserBtn = findViewById(R.id.superuser_btn);
        mDisconnectDevice = findViewById(R.id.btn_disconnect);
        mConfigureDevice = findViewById(R.id.button_configure);
        tv_device_name = findViewById(R.id.button_device_name);
        tv_firmware_version = findViewById(R.id.button_firmware_version);
        tv_memory = findViewById(R.id.tv_memory_percentage);
        tvBattery = findViewById(R.id.tv_battery_percentage);
        tvBatteryStatus = findViewById(R.id.tv_battery_status);
        mOTASettingsButton = findViewById(R.id.button_ota_configure);
        menuButton = findViewById(R.id.home_menu_button);
        mViewMeasurements = findViewById(R.id.btn_view_measurements);

        // Set device name
        tv_device_name.setText(gDeviceName);


        if(bluetoothAPI != null)
        {
            bluetoothAPI.setHomeContext(this.mContext);
            bluetoothAPI.sendMemoryRequest();
            bluetoothAPI.sendBatRequest();
        }

        mSuperuserBtn.setVisibility(superUserVisibility);
        mSuperuserBtn.setOnClickListener(v->
        {
            Intent iMain = new Intent(HomeActivity.this, SuperUserActivity.class);
            startActivity(iMain);
        });

        mScanButton.setOnClickListener(v->
        {
            Intent iMain = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(iMain);
        });

        mViewMeasurements.setOnClickListener(v->
        {
            measurmentsViewCaller = HomeActivity.class;
            Intent iMain = new Intent(HomeActivity.this, ResultsActivity.class);
            startActivity(iMain);
        });

        mOTASettingsButton.setOnClickListener(v->
        {
            Intent iOTA = new Intent(HomeActivity.this, OTAActivity.class);
            startActivity(iOTA);
        });

        mConfigureDevice.setOnClickListener(v->{
            Intent iMain = new Intent(HomeActivity.this, ConfigureActivity.class);
            startActivity(iMain);
        });

        // This is the listener of device disconnection
        mDisconnectDevice.setOnClickListener(v->{
            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Disconnect");
            myAlert.setMessage("Are you sure you want to disconnect the device?");
            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    // Request of bluetooth disconnection
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
        });

        menuButton.setOnClickListener(v->{
            showPopup(v);
        });
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.home_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(i->{
            return onMenuItemClick(i);
        });
        popup.show();
    }

    // Menu Items contents(clear history -Get History -Update Firmware -OTA Request)
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            // Clear the all taken readings from the device memory
            case R.id.clear_hist:
                if(bluetoothAPI != null)
                    bluetoothAPI.sendClearMemoryRequest();
                return true;
            // Retrieve all taken readings from the device memory to the mobile app
            case R.id.get_hist:
                if(bluetoothAPI != null) {
                    String numberOfSavedSpectra = tv_memory.getText().toString();
                    final String numberOfSavedSpectra1 = numberOfSavedSpectra.split("/")[0];

                    Intent popupIntent = new Intent(HomeActivity.this, PopupActivity.class);

                    android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                            .Builder(mContext);
                    myAlert.setTitle("Add Files name");

                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText("");

                    myAlert.setView(input);

                    myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                            popupIntent.putExtra("FilesName", input.getText().toString());
                            dialogInterface.dismiss();

                            popupIntent.putExtra("NumberOfSavedSpectra", numberOfSavedSpectra1);
                            startActivity(popupIntent);

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
                return true;
            //Update the firmware with a new firmware version
            case R.id.menuItemUpdateFirmware:
                Intent popupIntent = new Intent(HomeActivity.this, OTA_popup.class);
                popupIntent.putExtra("Action", UPDATE_FIRMWARE.getValue());
                startActivity(popupIntent);

                return true;
            // Restore to default firmware
            case R.id.menuItemOTAReset:
                Intent popupIntent2 = new Intent(HomeActivity.this, OTA_popup.class);
                popupIntent2.putExtra("Action", RESET_FIRMWARE.getValue());
                startActivity(popupIntent2);

                return true;
            //SW About infromation(SW Name -Version -Production company name -Website name)
            case R.id.SWabout:

                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                PackageInfo pInfo = null;
                String virsion= "";
                try {
                    ApplicationInfo ai = this.getPackageManager().getApplicationInfo("com.si_ware.neospectra", PackageManager.GET_META_DATA);

                     virsion = ai.metaData.getString("Specification-Version") + "." +
                             ai.metaData.getString("Implementation-Version");
                } catch (NameNotFoundException e)
                {
                    //TODO Handle exception
                }

                myAlert.setMessage( "Software : NeoSpectra-Scanner \u2122 \u00A9 2020 SWS.\r\nVersion : " + virsion + "\r\nProvided by : Si-Ware Systems.\r\nWebsite: www.si-ware.com\r\nFor assistance please contact neospectra.support@si-ware.com");
                myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                });

                myAlert.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));

        if(bluetoothAPI != null) {
            if (!bluetoothAPI.isDeviceConnected()) {
                endActivity();
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
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

    // Handling broadcast receiver
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String intentName = intent.getStringExtra("iName");
            System.out.println("Action: " + intent.getAction());
            switch (intentName) {
                // Receive a streamed data due to a taken scan
                case "MemoryScanData":
                    double[] reading = intent.getDoubleArrayExtra("data");

                    if (reading == null) {
                        logMessage("HomeView", "Reading is NULL.");
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

                    if ((y_reading.length > 0) && (x_reading.length > 0)) {
                        dbReading newReading = new dbReading();
                        newReading.setReading(y_reading, x_reading);
                        gAllSpectra.add(newReading);
                    }

                    break;
                // Receive a memory information
                case "Memory":
                    logMessage("HomeActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    tv_memory.setTextSize(15);
                    tv_memory.setText("" + intent.getLongExtra("data", 0) + "/"
                            + MAX_SCANNER_MEMORY);
                    break;
                // Receive FW version information
                case "FWVersion":
                    logMessage("HomeActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");

                    tv_firmware_version.setText("" + intent.getLongExtra("data", 0));
                    break;
                // Receive battery capacity information
                case "BatCapacity":
                    logMessage("HomeActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    tvBattery.setText("" + intent.getLongExtra("data", 0) + "%");
                    break;
                // Receive charging status
                case "ChargingStatus":
                    logMessage("HomeActivity", "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + intent.getLongExtra("data", 0) + "\n");
                    long status = intent.getLongExtra("data", 0);
                    if(status == 0L)
                        tvBatteryStatus.setText("");
                    else if(status == 1L)
                        tvBatteryStatus.setText("Charging");
                    else if(status == 2L) {
                        tvBatteryStatus.setText("Fast Charging");
                        tvBatteryStatus.setTextSize(15);
                    }
                    else
                        tvBatteryStatus.setText("");
                    break;
                // Disconnection notification
                case "Disconnection_Notification":
                    endActivity();
                    break;

            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    private void endActivity()
    {
        bluetoothAPI = null;
        Intent mIntent = new Intent(HomeActivity.this, ConnectActivity.class);
        startActivity(mIntent);
    }

}