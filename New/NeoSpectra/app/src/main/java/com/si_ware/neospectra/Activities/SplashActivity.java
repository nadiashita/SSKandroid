package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.si_ware.neospectra.R;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = getApplicationContext();
    }

    private class ReadModules extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... Voids) {
            try
            {
                Thread.sleep(1500);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Intent iMain = new Intent(SplashActivity.this, ConnectActivity.class);
                    startActivity(iMain);

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ReadModules reading = new ReadModules();
        reading.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Disconnect the device before exit from the application
        if (bluetoothAPI != null) bluetoothAPI.disconnectFromDevice();
    }
}