package com.si_ware.neospectra.Activities.MainFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.si_ware.neospectra.Activities.ConnectActivity;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import static com.si_ware.neospectra.Global.GlobalVariables.MEMORY_SAVE_DEFAULT_PARAM;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.scanTime;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage;
import com.si_ware.neospectra.Global.GlobalVariables.pointsCount;
import com.si_ware.neospectra.Global.GlobalVariables.apodization;
import com.si_ware.neospectra.Global.GlobalVariables.zeroPadding;

/**
 * A subclass of PreferenceFragmentCompat to supply preferences in a
 * Fragment for the SettingsActivity to display.
 * This subclass contains all needed configurations for the device
 */
public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "Settings Fragment";
    private ScanPresenter mScanPresenter;
    private Context mContext;
    private ListPreference mOpticalGainList, mInterpolationPoints, mApodizationList, mZeroPaddingList;
    SharedPreferences preferences;
    public String opticalGainName;
    public boolean isWaitingForSelfCorrection = false;
    public boolean isWaitingForGainSettings = false;
    public boolean isWaitingForRestoreToDefault = false;
    public boolean isWaitingForStoringAllSettings = false;
    private int notifications_count = 0;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);



        mContext = getContext();

        // Initialize all preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mOpticalGainList = (ListPreference) findPreference("optical_gain_settings");
        mInterpolationPoints = (ListPreference) findPreference("data_points");
        mApodizationList = (ListPreference) findPreference("apodization_function");
        mZeroPaddingList = (ListPreference) findPreference("fft_points");

        if(mInterpolationPoints.getValue() == null)  {
            mInterpolationPoints.setValueIndex(2);
        }

        if(mApodizationList.getValue() == null)  {
            mApodizationList.setValueIndex(0);
        }

        if(mZeroPaddingList.getValue() == null)  {
            mZeroPaddingList.setValueIndex(0);
        }


        // This preference applied to clear all optical gains settings
        Preference clearPreferences = (Preference) findPreference("clear_optical_gain_settings");
        clearPreferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                myAlert.setTitle("Clear");
                myAlert.setMessage("Are you sure you want to clear all optical gains?");
                myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        clearFile();
                        setListPreferenceData(mOpticalGainList);
                    }
                });

                myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                myAlert.show();
                return true;
            }
        });

        // This preference applied in storing all settings (Gain and Correction factors)
        Preference storSettings = (Preference) findPreference("store_all_Settings");
        storSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                myAlert.setTitle("Confirm");
                myAlert.setMessage("Start storing all settings?");
                myAlert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        askForStoringAllSettings();
                        dialogInterface.dismiss();

                    }
                });

                myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                myAlert.show();
                return true;
            }
        });

        // This preference restores the default gain and correction parameters.
        Preference restorePreference = (Preference) findPreference("restore_default");
        restorePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                myAlert.setTitle("Warning!");
                myAlert.setMessage("By restoring default settings, all current settings will be cleared." +
                        "Are you sure you would like to proceed?");
                myAlert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        askForRestoreToDefaultSettings();
                        dialogInterface.dismiss();

                    }
                });

                myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                myAlert.show();
                return true;
            }
        });

        // This preference is used to edit in the scan time to be saved in the stored preferences
        Preference scanTimePreference = (Preference) findPreference("scanTime_preferences");
        scanTimePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                        .Builder(mContext);
                myAlert.setTitle("ScanTime");
                myAlert.setMessage("Scan time will be saved in Scanner = " + Integer.toString(scanTime) + "S");
                myAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });


                myAlert.show();
                return true;
            }
        });

        //This preference is to add a new optical gain settings to opticalGainList
        EditTextPreference editTextPreference = (EditTextPreference) findPreference("add_new_optical_gain_settings");
        editTextPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    editTextPreference.setText("");
                    return true;
                }
            }
        );

        mOpticalGainList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                setListPreferenceData(mOpticalGainList);
                return false;
            }
        });

        // The preference is called when selfCorrection is needed to be applied
        Preference mSelfCorrectionPreferences = (Preference) findPreference("wavelength_correction");
        findPreference("wavelength_correction").setEnabled(false);
        mSelfCorrectionPreferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {



                    android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                            .Builder(mContext);
                    myAlert.setTitle("Run Self Calibration");
                    myAlert.setMessage("Run a smart routine to automatically recalibrate wavelengths shifts with samples that have flat spectral response.  ");
                    myAlert.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialogInterface, int i) {

                            sendSelfCorrectionCommand();
                            dialogInterface.dismiss();
                        }
                    });

                    myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    myAlert.show();


                return true;
            }
        });

        //Save all preferences
        findPreference("save_preferences").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                savePreferences();
                return false;
            }
        });
        findPreference("load_preferences").setEnabled(false);
    }

    // Request command of selfCorrection from scanner
    private void sendSelfCorrectionCommand() {
        System.out.println("inside sendSelfCorrectionCommand");
        if (isWaitingForSelfCorrection) {
            logMessage(TAG, "Still waiting for sensor reading ... ");
            return;
        }
        isWaitingForSelfCorrection = true;

        askForSelfCorrection();
        disableView();
    }

    // Request command of selfCorrection from scanner
    private void askForSelfCorrection() {
        if (mScanPresenter == null) {
            mScanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(mContext,
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }


        mScanPresenter.requestSelfCalibration(scanTime);
    }

    // Request restore to default settings from scanner
    private void askForRestoreToDefaultSettings() {
        if (mScanPresenter == null) {
            mScanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(mContext,
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }

        isWaitingForRestoreToDefault = true;
        mScanPresenter.restoreToDefaultSettings();
        disableView();
    }

    // Request to sore all setting in the scanner
    private void askForStoringAllSettings() {
        if (mScanPresenter == null) {
            mScanPresenter = new ScanPresenter();
        }
        // Don't complete the process if the device not connected
        if (bluetoothAPI == null || !bluetoothAPI.isDeviceConnected()) {
            showAlertMessage(mContext,
                    "Device not connected",
                    "Please! Ensure that you have a connected device firstly");
            Intent iMain = new Intent(mContext, ConnectActivity.class);
            iMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(iMain);
            return;
        }

        isWaitingForStoringAllSettings = true;
        mScanPresenter.storingSettings();
        disableView();
    }

    protected void setListPreferenceData(ListPreference lp) {
        List<CharSequence> charSequences = new ArrayList<>();
        ArrayList<String> opticalGains = readFromFile();
        ArrayList<String> newList = new ArrayList<String>();

        // Traverse through the first list
        for (String element : opticalGains) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        charSequences.add("Default");
        for (int i = 0; i < newList.size(); ++i) {
            charSequences.add(newList.get(i));
        }

        CharSequence[] charSequenceArray = charSequences.toArray(new
                CharSequence[charSequences.size()]);
        lp.setEntries(charSequenceArray);
        lp.setEntryValues(charSequenceArray);

        if (lp.getValue() == null)
            lp.setValueIndex(charSequenceArray.length - 1);

        if (lp.getEntries().length == 1) {
            lp.setValueIndex(0);
        }
    }

    // Read from configuration file
    private ArrayList<String> readFromFile() {
        ArrayList<String> arr = new ArrayList<String>();
        try {
            InputStream inputStream = mContext.openFileInput("configurations.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null) {
                    arr.add(receiveString);
                }

                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return arr;
    }

    // Receive response from scanner
    @NonNull
    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentName = intent.getStringExtra("iName");
            switch (intentName) {
                case "OperationDone":
                    showAlertMessage(mContext, "Save Preferences",
                            "Preferences has been saved on the scanner successfully.");
                    enableView();
                    break;
                case "Error":
                    int errorCode = intent.getIntExtra("data", 0);
                    showAlertMessage(mContext, "Error",
                            "Error " + String.valueOf(0x000000FF & errorCode) +
                                    " occurred during measurement!");
                    enableView();
                    break;
                default:
                    Log.v(TAG + "intent", "Got unknown broadcast intent");
            }
        }
    };

    // Receive response from scanner
    @NonNull
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String intentName = intent.getStringExtra("iName");
            switch (intentName) {
                case "sensorNotification_data":
                    gotSensorReading(intent);
                    break;
                case "sensorNotification_failure":
                    gotSensorReading(intent);
                    logMessage(TAG, "Intent Received:\n" +
                            "Name: " + intent.getStringExtra("iName") + "\n" +
                            "Success: " + intent.getBooleanExtra("isNotificationSuccess", false) + "\n" +
                            "Reason: " + intent.getStringExtra("reason") + "\n" +
                            "Error: " + intent.getStringExtra("err") + "\n" +
                            "data : " + String.valueOf(intent.getIntExtra("data", 0)) + "\n");
                    int errorCode = intent.getIntExtra("data", 0);
                    notifications_count = 0;
                    isWaitingForGainSettings = false;
                    isWaitingForSelfCorrection = false;
                    isWaitingForRestoreToDefault = false;
                    isWaitingForStoringAllSettings = false;

                    showAlertMessage(mContext, "Error", "Error " + String.valueOf(0x000000FF & errorCode) + " occurred during measurement!");
                    enableView();
                    break;
                case "sensorWriting":
                    break;
                default:
                    Log.v(TAG + "intent", "Got unknown broadcast intent");
            }
        }
    };

    // Got sensor readings
    private void gotSensorReading(Intent intent) {
        boolean isNotificationSuccessful = intent.getBooleanExtra("isNotificationSuccess", false);
        String notificationReason = intent.getStringExtra("reason");
        String errorMessage = intent.getStringExtra("err");

        /* If the notification is unsuccessful */
        if (!isNotificationSuccessful) {
            logMessage(notificationReason, errorMessage);
            return;
        }

        notifications_count++;

        if (notificationReason.equals("gotData")) {
            double[] gain = intent.getDoubleArrayExtra("data");

                if(isWaitingForGainSettings)
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(opticalGainName, (int) gain[0]);
                    editor.commit();
                    preferences.edit().putString(mOpticalGainList.getKey(), mOpticalGainList.getValue());
                    writeToFile();
                    setListPreferenceData(mOpticalGainList);
                    mOpticalGainList.setValue(opticalGainName);

                    isWaitingForGainSettings = false;

                    enableView();
                    return;
                }
                else if(isWaitingForRestoreToDefault)
                {
                    logMessage("RestoreToDefault", "Settings has been restored successfully.");
                     showAlertMessage(mContext, "Restore To Default", "Settings has been restored successfully.");

                    enableView();
                    isWaitingForRestoreToDefault = false;
                }
                else if(isWaitingForStoringAllSettings&&  ((notifications_count % 2) == 0))
                {
                    logMessage("StoringAllSettings", "Settings has been stored successfully.");
                    showAlertMessage(mContext, "Storing All Settings", "Settings has been stored successfully.");

                    enableView();
                    isWaitingForStoringAllSettings = false;
                }
                else if(isWaitingForSelfCorrection &&  ((notifications_count % 3) == 0))
                {
                    logMessage("SelfCalibration", "Self Calibration has been finished successfully.");
                    showAlertMessage(mContext, "Run Self Calibration", "Self Calibration has been finished successfully.");

                   isWaitingForSelfCorrection = false;
                   enableView();
                }

        }

        else
        {
            return;
        }

    }

    // Write optical Gain settings to configuration file
    private void writeToFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("configurations.txt", Context.MODE_APPEND));
            outputStreamWriter.write(opticalGainName);
            outputStreamWriter.write("\n");
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    // Clear all settings from configuration file
    private void clearFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("configurations.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (FileNotFoundException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver2);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(GlobalVariables.INTENT_ACTION));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter(GlobalVariables.HOME_INTENT_ACTION));
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals("add_new_optical_gain_settings")) {
            EditTextPreference preference = (EditTextPreference) findPreference(key);
            opticalGainName = String.valueOf(preference.getText().toString());

            if (!opticalGainName.equals("")) {
                if (bluetoothAPI == null) {
                    bluetoothAPI = new SWS_P3API(getActivity(), getContext());
                }

                mScanPresenter = new ScanPresenter();

                isWaitingForGainSettings = true;
                mScanPresenter.requestGainReading();
                disableView();
            }

        }

    }

    private void disableView()
    {
        ((PreferenceCategory) findPreference("category_measurement_parameters")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_data_display")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_fft_settings")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_advanced_settings")).setEnabled(false);
        ((PreferenceCategory) findPreference("category_save_restore")).setEnabled(false);
    }

    private void enableView()
    {
        ((PreferenceCategory) findPreference("category_measurement_parameters")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_data_display")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_fft_settings")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_advanced_settings")).setEnabled(true);
        ((PreferenceCategory) findPreference("category_save_restore")).setEnabled(true);
    }

    // Save all preferences in scanner
    private void savePreferences()
    {
        byte[] memPreferencePacket = new byte[11];
        memPreferencePacket[0] = (byte) MEMORY_SAVE_DEFAULT_PARAM;
        System.arraycopy(
                ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(scanTime * 1000).array(), 0,
                memPreferencePacket, 1, 3);

        String optical_gain = preferences.getString("optical_gain_settings", "Default");
        int OpticalGainValue = preferences.getInt(optical_gain, 0);
        System.arraycopy(
                ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(OpticalGainValue).array(), 0,
                memPreferencePacket, 4, 2);

        if(!preferences.getBoolean("linear_interpolation_switch", false))
            memPreferencePacket[6] = 0;
        else{
            String interpolationPoints =
                    preferences.getString("data_points", pointsCount.points_257.toString());

            if(interpolationPoints.equals(pointsCount.points_65.toString()))
                memPreferencePacket[6] = 1;
            else if(interpolationPoints.equals(pointsCount.points_129.toString()))
                memPreferencePacket[6] = 2;
            else if(interpolationPoints.equals(pointsCount.points_257.toString()))
                memPreferencePacket[6] = 3;
            else
                memPreferencePacket[6] = 0;
        }

        if(optical_gain.equals("Default"))
            memPreferencePacket[7] = 0;
        else
            memPreferencePacket[7] = 2;

        String apodizationSel =
                preferences.getString("apodization_function", apodization.Boxcar.toString());
        if(apodizationSel.equals(apodization.Boxcar.toString()))
            memPreferencePacket[8] = 0;
        else if(apodizationSel.equals(apodization.Gaussian.toString()))
            memPreferencePacket[8] = 1;
        else if(apodizationSel.equals(apodization.HappGenzel.toString()))
            memPreferencePacket[8] = 2;
        else if(apodizationSel.equals(apodization.Lorenz.toString()))
            memPreferencePacket[8] = 3;
        else
            memPreferencePacket[8] = 0;

        String FftPoints = preferences.getString("fft_points", zeroPadding.points_8k.toString());
        if(FftPoints.equals(zeroPadding.points_8k.toString()))
            memPreferencePacket[9] = 1;
        else if(FftPoints.equals(zeroPadding.points_16k.toString()))
            memPreferencePacket[9] = 2;
        else if(FftPoints.equals(zeroPadding.points_32k.toString()))
            memPreferencePacket[9] = 3;
        else
            memPreferencePacket[9] = 1;

        memPreferencePacket[10] = 0;     //runMode

        disableView();

        if(bluetoothAPI != null)
            bluetoothAPI.sendPacket(memPreferencePacket, false, "Memory Service");
    }
}