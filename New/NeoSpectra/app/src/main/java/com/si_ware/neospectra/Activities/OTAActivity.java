package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.si_ware.neospectra.BluetoothSDK.SWS_P3Packet;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.si_ware.neospectra.Global.GlobalVariables.OTA_MAX_TRANSMISSION_UNIT;
import static com.si_ware.neospectra.Global.GlobalVariables.OTA_BT_MAX_TRANSMISSION_UNIT;
import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.filterDate;
import static com.si_ware.neospectra.Global.GlobalVariables.gDeviceName;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import java.util.Set;
import android.widget.ArrayAdapter;
import java.util.UUID;
import java.io.OutputStream;

public class OTAActivity extends NavDrawerActivity implements
        View.OnClickListener {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;

    DrawerLayout drawer;
    Context mContext;
    Button mStartOTA, mSendFile, mEndOTA, mResetOTA;

    byte[] binaryOTAFileInBytes;

    TextView tv_device_name, tv_firmware_version, tv_memory, mDisconnectDevice;
    ScanPresenter scanPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_ota, null, false);

        if (gDeviceName.indexOf("_") != -1)
            gDeviceName = gDeviceName.substring(gDeviceName.indexOf("_")+1);

        mContext = this;
        scanPresenter = new ScanPresenter();

        drawer = findViewById(R.id.drawer_layout);
        drawer.addView(contentView, 0);


        mStartOTA = findViewById(R.id.btn_ota_start);
        mSendFile = findViewById(R.id.btn_ota_send);
        mEndOTA = findViewById(R.id.btn_ota_end);
        mResetOTA = findViewById(R.id.btn_ota_reset);

        mDisconnectDevice = findViewById(R.id.btn_disconnect);
        tv_device_name = findViewById(R.id.button_device_name);
        tv_firmware_version = findViewById(R.id.button_firmware_version);
        tv_memory = findViewById(R.id.tv_memory_percentage);
        tv_device_name.setText(gDeviceName);

        mSendFile.setEnabled(false);
        mSendFile.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));
        mEndOTA.setEnabled(false);
        mEndOTA.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.Button_Disabled));

        mStartOTA.setOnClickListener(v->
        {
            SWS_P3Packet newPacket = new SWS_P3Packet(4);
            byte[] otaStartCommand = new byte[4];
            //OTA START Command (length 4 bytes): 0x44 FE 9D A6
            otaStartCommand[0] = (byte)Integer.parseInt("44", 16);
            otaStartCommand[1] = (byte)Integer.parseInt("FE", 16);
            otaStartCommand[2] = (byte)Integer.parseInt("9D", 16);
            otaStartCommand[3] = (byte)Integer.parseInt("A6", 16);

            newPacket.setPacketStream(otaStartCommand);

            if (scanPresenter == null) {
                scanPresenter = new ScanPresenter();
            }

            scanPresenter.sendOTACommand(newPacket);

            discover();
        });

        mSendFile.setOnClickListener(v->
        {
            try {

                sendData();

            } catch (IOException e) {
                System.out.println(e.getMessage());
                //TODO: Handle the thrown exception.
            }
        });

        mEndOTA.setOnClickListener(v->{
            try {
                SWS_P3Packet newPacket = new SWS_P3Packet(4);
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
        });

        mResetOTA.setOnClickListener(v->{
            // create a new packet instance
            SWS_P3Packet newPacket = new SWS_P3Packet(4);
            byte[] otaStartCommand = new byte[4];
            // OTA Revert to Factory Application Command (length 4 bytes): 0xE0 DA D5 51
            otaStartCommand[0] = (byte)Integer.parseInt("E0", 16);
            otaStartCommand[1] = (byte)Integer.parseInt("DA", 16);
            otaStartCommand[2] = (byte)Integer.parseInt("D5", 16);
            otaStartCommand[3] = (byte)Integer.parseInt("51", 16);

            newPacket.setPacketStream(otaStartCommand);

            if (scanPresenter == null) {
                scanPresenter = new ScanPresenter();
            }

            scanPresenter.sendOTACommand(newPacket);
        });


        mDisconnectDevice.setOnClickListener(v->{
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
        });
    }

    private void findBT()
    {
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
    }

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
                    if (device.getName().equals("NeoSpectra_Scaner_SPP_OTA")) {
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

            mSendFile.setEnabled(true);
            mSendFile.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
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
            binaryOTAFileInBytes = fullyReadFileToBytes("image.bin");

            int commandsCount = binaryOTAFileInBytes.length / OTA_BT_MAX_TRANSMISSION_UNIT;

            for (int i = 0; i < commandsCount; ++i) {
                mmOutputStream.write(binaryOTAFileInBytes, i * OTA_BT_MAX_TRANSMISSION_UNIT, OTA_BT_MAX_TRANSMISSION_UNIT);
                Thread.sleep(20);
            }

            if ((binaryOTAFileInBytes.length % OTA_BT_MAX_TRANSMISSION_UNIT) != 0) {
                mmOutputStream.write(binaryOTAFileInBytes, commandsCount * OTA_BT_MAX_TRANSMISSION_UNIT, binaryOTAFileInBytes.length % OTA_BT_MAX_TRANSMISSION_UNIT);
            }

            mEndOTA.setEnabled(true);
            mEndOTA.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.orange));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: Handle the thrown exception.
        }
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

    @Override
    public void onClick(View v) {

    }
}
