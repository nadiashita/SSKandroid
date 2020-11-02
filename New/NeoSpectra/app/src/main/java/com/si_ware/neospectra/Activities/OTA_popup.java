package com.si_ware.neospectra.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.si_ware.neospectra.BluetoothSDK.SWS_P3Packet;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.si_ware.neospectra.Global.GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT;
import static com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions.RESET_FIRMWARE;
import static com.si_ware.neospectra.Global.GlobalVariables.OTA_Functions.UPDATE_FIRMWARE;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gDeviceName;
import static com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage;

public class OTA_popup extends NavDrawerActivity implements
        View.OnClickListener {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream = null;

    ProgressBar mProgressBar;
    TextView mTextViewTitle;
    ViewGroup mainView;
    int action = 0;

    byte[] binaryOTAFileInBytes = null;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_ota);

        mProgressBar = findViewById(R.id.popupOTAprogressBar);
        mTextViewTitle = findViewById(R.id.popupOTATitle);
        mainView = findViewById(R.id.popupOTA_mainView);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        getWindow().setLayout((int)(dm.widthPixels * 0.85), (int)(dm.heightPixels * 0.15));

        action = getIntent().getIntExtra("Action", UPDATE_FIRMWARE.getValue());
        if(action == UPDATE_FIRMWARE.getValue())
        {
            mTextViewTitle.setText("Firmware updating ...");
            mProgressBar.setProgress(0);

        }
        else if(action == RESET_FIRMWARE.getValue())
        {
            mTextViewTitle.setText("Restore Default Firmware ...");
        }

        try {
            binaryOTAFileInBytes = fullyReadFileToBytes("image.bin");
        } catch (IOException e) {

            android.support.v7.app.AlertDialog.Builder myAlert =
                    new android.support.v7.app.AlertDialog.Builder(this);
            myAlert.setTitle("File not Found!");
            myAlert.setMessage("Please make sure that firmware (\"image.bin\") is placed in Downloads Folder.");
            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            myAlert.show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                if(action == UPDATE_FIRMWARE.getValue())
                {
                    startOTA();
                }
                else if(action == RESET_FIRMWARE.getValue())
                {
                    mProgressBar.setProgress(0);
                    resetOTA();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mProgressBar.setProgress(100);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                do{
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while (mmOutputStream == null);

                try {
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // Start OTA command
    private void startOTA()
    {
        byte[] otaStartCommand = new byte[4];

        otaStartCommand[0] = (byte)Integer.parseInt("44", 16);
        otaStartCommand[1] = (byte)Integer.parseInt("FE", 16);
        otaStartCommand[2] = (byte)Integer.parseInt("9D", 16);
        otaStartCommand[3] = (byte)Integer.parseInt("A6", 16);

        if(bluetoothAPI != null)
            bluetoothAPI.sendOTAPacket(otaStartCommand);

        discover();
    }

    // End OTA command
    private void endOTA()
    {
        try {
            byte[] Command = new byte[4];

            //OTA END Command (length 4 bytes): 0x5F 83 F6 9E
            Command[0] = (byte) Integer.parseInt("5F", 16);
            Command[1] = (byte) Integer.parseInt("83", 16);
            Command[2] = (byte) Integer.parseInt("F6", 16);
            Command[3] = (byte) Integer.parseInt("9E", 16);

            mmOutputStream.write(Command);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            //TODO: Handle the thrown exception.
        }
    }

    //Reset OTA command
    private void resetOTA()
    {
        byte[] otaResetCommand = new byte[4];
        // OTA Revert to Factory Application Command (length 4 bytes): 0xE0 DA D5 51
        otaResetCommand[0] = (byte)Integer.parseInt("E0", 16);
        otaResetCommand[1] = (byte)Integer.parseInt("DA", 16);
        otaResetCommand[2] = (byte)Integer.parseInt("D5", 16);
        otaResetCommand[3] = (byte)Integer.parseInt("51", 16);

        if(bluetoothAPI != null)
            bluetoothAPI.sendOTAPacket(otaResetCommand);
    }

    // Search on an open device
    private void discover(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            System.out.println("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        // Check if the device is already discovering
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
            System.out.println("Discovery stopped");
        }
        else{
            if(mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.startDiscovery();
                System.out.println("Discovery started");
                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                System.out.println("Bluetooth not on");
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                if(device.getName() != null) {
                    if (device.getName().equals("NeoSpectra_Scaner_OTA_" + gDeviceName)) {
                        mmDevice = device;
                        try {
                            openBT();
                        }catch (IOException e)
                        {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
                try {
                    if(mmDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                        openBT();
                        unregisterReceiver(blReceiver);
                    }
                }catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    };

    private void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID

        if(mmDevice.getBondState() == BluetoothDevice.BOND_BONDED){
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();

            System.out.println("Bluetooth Opened");
        }
        else {
            System.out.println("Bonding ......");
            mmDevice.createBond();
            registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }
    }

    private void sendData() throws IOException
    {
        try {

            int commandsCount = binaryOTAFileInBytes.length / OTA_BT_MAX_TRANSMISSION_UNIT;

            for (int i = 0; i < commandsCount; ++i) {
                mmOutputStream.write(binaryOTAFileInBytes, i * OTA_BT_MAX_TRANSMISSION_UNIT, OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);

                final int progessvalue = (int)((i * 1.0/commandsCount) * 100);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.setProgress(progessvalue);
                    }
                });

            }

            if ((binaryOTAFileInBytes.length % OTA_BT_MAX_TRANSMISSION_UNIT) != 0) {
                mmOutputStream.write(binaryOTAFileInBytes, commandsCount * OTA_BT_MAX_TRANSMISSION_UNIT, binaryOTAFileInBytes.length % OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        endOTA();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mProgressBar.setProgress(100);
        finish();
    }

    public static byte[] fullyReadFileToBytes(String fileName) throws IOException {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);

        try {
            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            throw e;
        } finally {
            fis.close();
        }
        return bytes;
    }

    private void endActivity()
    {
        bluetoothAPI = null;
        Intent mIntent = new Intent(OTA_popup.this, ConnectActivity.class);
        startActivity(mIntent);
    }

    @Override
    public void onClick(View v) {

    }
}
