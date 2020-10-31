package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.R;

import static com.si_ware.neospectra.Activities.ConnectActivity.hasPermissions;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;


public class IntroActivity extends AppCompatActivity {

    Button btnConnect;

    static private Context mContext;


    final int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mContext = this;

        bluetoothAPI = new SWS_P3API(this, mContext);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Enable Bluetooth Adapter
        mBluetoothAdapter.enable();

        btnConnect = findViewById(R.id.btnConnect);

        // Request permission if it was not enable
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent connectActivity = new Intent(IntroActivity.this, SettingsActivity.class);
                startActivity(connectActivity);
            }
        });
    }

        // Check self permission if enabled or not
        public static boolean hasPermissions(Context context, String... permissions) {
            if (context != null && permissions != null) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
            return true;
        }
}