package com.si_ware.neospectra.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;

import java.io.File;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.si_ware.neospectra.Global.GlobalVariables.OutputDirectory;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUMFILE_Y_AXIS_R_T;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_DEFAULT_PATH_TEMPLATE;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_FILE_X_AXIS_NM;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_REFL_PATH_TEMPLATE;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra;
import static com.si_ware.neospectra.Global.MethodsFactory.formatString;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.writeGraphFile;
import static com.si_ware.neospectra.Global.MethodsFactory.convertDataToT;
import static com.si_ware.neospectra.Global.MethodsFactory.switch_NM_CM;


public class PopupActivity extends Activity {

    TextView mTextProgressValue;
    int maxFileNum = 0;
    int fileIterator = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_progress);

        mTextProgressValue = findViewById(R.id.popup_progressValue);
        mTextProgressValue.setText("0/" + getIntent().getStringExtra("NumberOfSavedSpectra"));

        maxFileNum = Integer.valueOf(getIntent().getStringExtra("NumberOfSavedSpectra")).intValue();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels * 0.85), (int)(dm.heightPixels * 0.15));
        updateProgressValue(fileIterator);

        if(bluetoothAPI != null)
            bluetoothAPI.sendScansHistoryRequest(fileIterator);
    }

    private void updateProgressValue(int value)
    {
        String[] strings = mTextProgressValue.getText().toString().split("/");

        strings[0] = String.valueOf(value);
        mTextProgressValue.setText(strings[0] + "/" + strings[1]);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Register the broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void createOutDir(String dir){

        File file = new File(getExternalStoragePublicDirectory(dir).toString());
        System.out.println("************************* Path : "+dir);
        if (!file.exists())
            file.mkdir();

    }



    public   boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                createOutDir(OutputDirectory);
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            createOutDir(OutputDirectory);
            return true;
        }
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String intentName = intent.getStringExtra("iName");
            System.out.println("Action: " + intent.getAction());
            switch (intentName) {
                case "MemoryScanData":
                    double[] reading = intent.getDoubleArrayExtra("data");

                    if (reading == null) {
                        logMessage("PopupProgress", "Reading is NULL.");
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
                    x_reading = switch_NM_CM(x_reading);
                    y_reading = convertDataToT(y_reading);

                    isWriteStoragePermissionGranted();
                    if ((y_reading.length > 0) && (x_reading.length > 0)) {
                        String filesName = getIntent().getStringExtra("FilesName");
                        if(filesName.isEmpty()|| filesName==null)
                            filesName = SPECTRUM_DEFAULT_PATH_TEMPLATE;
                        writeGraphFile(x_reading, y_reading, OutputDirectory,
                                filesName + SPECTRUM_REFL_PATH_TEMPLATE + String.format("%03d",fileIterator)+
                                        SPECTRUM ,
                                SPECTRUM_FILE_X_AXIS_NM + "\t" + SPECTRUMFILE_Y_AXIS_R_T);
                    }

                    if(fileIterator < maxFileNum)
                    {
                        fileIterator = fileIterator + 1;
                        updateProgressValue(fileIterator);
                        if(bluetoothAPI != null)
                            bluetoothAPI.sendScansHistoryRequest(fileIterator);
                    }
                    else
                        finish();
                    break;
            }
        }
    };
}
