package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.si_ware.neospectra.Adapters.SensorsAdapter;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3BLEDevice;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.R;

import java.util.ArrayList;
import java.util.List;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.currentConnectedDevice;
import static com.si_ware.neospectra.Global.GlobalVariables.gApodizationFunction;
import static com.si_ware.neospectra.Global.GlobalVariables.gCorrectionMode;
import static com.si_ware.neospectra.Global.GlobalVariables.gFftPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gInterpolationPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsFftEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsInterpolationEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainSettings;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainValue;
import static com.si_ware.neospectra.Global.GlobalVariables.gRunMode;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.rotateProgressBar;

public class SettingsActivity extends NavDrawerActivity implements
        View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    DrawerLayout drawer;

    private FirebaseAuth mAuth;

    Toolbar toolbar;

    private Paint paint;

    //Bluetooth Area
    private RelativeLayout bluetoothBoard;

    static private TextView tvStatus;

    static private ProgressBar pbSearching;
    static private RecyclerView rvDevices;

    static private List<SWS_P3BLEDevice> bleDevices;
    static private SensorsAdapter adapter;
    private boolean isBluetoothEnabled, isLocationEnabled;

    static private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_settings, null, false);
        mContext = this;

// Create new instance from bluetoothApi if it null
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(this, mContext);
        }

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


        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);
//        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Bluetooth" + "</font>"));
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ffffff"));
//        getSupportActionBar().setBackgroundDrawable(colorDrawable);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create new instance from bluetoothApi if it null
        if (bluetoothAPI == null) {
            bluetoothAPI = new SWS_P3API(this, mContext);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        bluetoothBoard = contentView.findViewById(R.id.board_bluetooth);
        initBluetoothArea();

        //Navigation Drawer
        drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);

        //  initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

    }

    static public void found_device(List<SWS_P3BLEDevice> list_Devices) {
        bleDevices = list_Devices;
        displayDevices("Searching button", bleDevices);
    }

    private void initBluetoothArea() {
        // Bluetooth board
        tvStatus = bluetoothBoard.findViewById(R.id.tv_status);
        pbSearching = bluetoothBoard.findViewById(R.id.pb_bluetooth_scanning);

        // Start the progressBar rotation if the pb is visible
        if (pbSearching.getVisibility() == View.VISIBLE)
            rotateProgressBar(SettingsActivity.this, pbSearching);
        rvDevices = bluetoothBoard.findViewById(R.id.rv_bluetooth_devices);

        // the recycler view is hidden by default
        rvDevices.setVisibility(View.INVISIBLE);

        // ========== Backdoor to testing activities ================= //
        tvStatus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent iTesting = new Intent(SettingsActivity.this, MainPage.class);
                startActivity(iTesting);
                return true;
            }
        });
    }

    private void initListeners() {

        while (bluetoothAPI == null) {
            logMessage(TAG, "Initializing bluetooth API ...");
        }

        isBluetoothEnabled = bluetoothAPI.isBluetoothEnabled();
        isLocationEnabled = bluetoothAPI.isLocationEnabled(mContext);

        if (isBluetoothEnabled && isLocationEnabled) {

            tvStatus.setText(mContext.getResources().getString(R.string.bluetooth_scan));
            pbSearching.setVisibility(View.VISIBLE);
            rotateProgressBar(mContext, pbSearching);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    bleDevices = bluetoothAPI.scanAvailableDevices();

                    SettingsActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            displayDevices("Searching button", bleDevices);
                        }
                    });

                }
            });
        }
    }

    static private void displayDevices(String from, List<SWS_P3BLEDevice> devices) {
        logMessage("bluetooth", "Displaying devices from: " + from);
        if (devices == null || devices.size() < 1) {
            Toast.makeText(mContext, "devices size <1", Toast.LENGTH_SHORT).show();
            return;
        }

        rvDevices.setVisibility(View.VISIBLE);
        // Set the recyclerView Adapter
        rvDevices.setLayoutManager(new LinearLayoutManager(mContext));

        adapter = new SensorsAdapter(mContext, devices);
        rvDevices.setAdapter(adapter);
        pbSearching.setVisibility(View.INVISIBLE);
        tvStatus.setText(mContext.getResources().getString(R.string.bluetooth_found_devices));
        logMessage("bluetooth", "displaying Done, length equals: " + devices.size());
    }


    private void updateSearchButtonStatus() {
        isBluetoothEnabled = bluetoothAPI.isBluetoothEnabled();
        isLocationEnabled = bluetoothAPI.isLocationEnabled(mContext);

        Log.d("bluetooth", "Bluetooth status is: " + isBluetoothEnabled +
                " Location service is: " + isLocationEnabled);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Disconnect the device before exit from the application
        if (bluetoothAPI != null) bluetoothAPI.disconnectFromDevice();
    }

    private BitmapDrawable writeOnDrawable(int drawableId, @NonNull String text) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bmp1 = BitmapFactory.decodeResource(this.getResources(), drawableId);
        //then create a copy of bitmap bmp1 into bmp2
        Bitmap bm = bmp1.copy(bmp1.getConfig(), true);

        if (paint == null) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.grey)); //Change this if you want other color of text
        paint.setTextSize(30); //Change this if you want bigger/smaller font

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 10, bm.getHeight() / 4, paint); //Change the position of the text here

        return new BitmapDrawable(getResources(), bm);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Update the flags of bluetooth and location
        getCurrentStatusOfLocationAndBluetooth();
        initListeners();
        // Update the status of search button
        updateSearchButtonStatus();

        if (currentConnectedDevice != null) {
            logMessage("bluetooth", "Current device found");
            List<SWS_P3BLEDevice> mDevices = new ArrayList<>();
            currentConnectedDevice.connected = true;
            mDevices.add(currentConnectedDevice);
            displayDevices("OnResume", mDevices);
        }

    }

    private void getCurrentStatusOfLocationAndBluetooth() {
        isBluetoothEnabled = getBluetoothStatus();
        isLocationEnabled = getLocationStatus();
    }


    private boolean getLocationStatus() {
        return bluetoothAPI.isLocationEnabled(mContext);
    }

    private boolean getBluetoothStatus() {
        return bluetoothAPI.isBluetoothEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                Intent back = new Intent(this, IntroActivity.class);
                startActivity(back);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}