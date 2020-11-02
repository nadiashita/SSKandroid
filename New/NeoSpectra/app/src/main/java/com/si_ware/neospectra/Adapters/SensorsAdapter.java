package com.si_ware.neospectra.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.si_ware.neospectra.Activities.HomeActivity;
import com.si_ware.neospectra.Activities.MainPage;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3BLEDevice;
import com.si_ware.neospectra.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gDeviceName;
import static com.si_ware.neospectra.Global.MethodsFactory.logMessage;

/**
 *
 */

public class SensorsAdapter extends Adapter<SensorsAdapter.SensorViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<SWS_P3BLEDevice> sensors;
    @NonNull
    private String TAG = "SensorAdapter";

    public SensorsAdapter(Context context, List<SWS_P3BLEDevice> devices) {

        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.sensors = devices;
        logMessage("blutooth", "Devices in constructor length: " + devices.size() +
                ", Sensors: " + sensors.size());
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        logMessage("bluetooth .. adapter", "Creating view holder 1");
        View view = inflater.inflate(R.layout.row_bluetooth_device, parent, false);
        logMessage("bluetooth .. adapter", "Creating view holder 2");
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder currentViewHolder, int position) {

        try {
            logMessage("bluetooth .. adapter", "Binding in adapter, the list length equals: " + sensors.size());
            SWS_P3BLEDevice currentDevice = sensors.get(position);
            String name = currentDevice.getDeviceName();
            if (name != null) currentViewHolder.tvName.setText(name);
            String mac = currentDevice.getDeviceMacAddress();
            if (mac != null) currentViewHolder.tvMac.setText(mac);
            ProgressBar mScanProgress = currentViewHolder.progressBar;
            Button btnConnect = currentViewHolder.btnConnect;

            if (!bluetoothAPI.isDeviceConnected()) {
                btnConnect.setText(mContext.getResources().getString(R.string.connect));
                currentViewHolder.tvName.setTypeface(Typeface.DEFAULT);
            }


            btnConnect.setOnClickListener(v -> {

                mScanProgress.setVisibility(View.VISIBLE);
                bluetoothAPI.connectToDevice(sensors.get(position),
                        mScanProgress,
                        btnConnect, currentViewHolder.tvName);

                try
                {
                    Thread.sleep(2500);
                }
                catch(InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
                Intent iConnected = new Intent(mContext, MainPage.class);
                gDeviceName = sensors.get(position).getDeviceName();
                mContext.startActivity(iConnected);

            });

        } catch (Exception e) {

            logMessage(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        logMessage("bluetooth .. adapter", "getItemCount() has: " + sensors.size() + " items");
        return sensors.size();
    }

    public class SensorViewHolder extends RecyclerView.ViewHolder {
        Button btnConnect;
        TextView tvName, tvMac;
        ProgressBar progressBar;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            btnConnect = itemView.findViewById(R.id.btn_connect_disconnect);
            tvName = itemView.findViewById(R.id.tv_sensor_name);
            tvMac = itemView.findViewById(R.id.tv_mac);
            progressBar = itemView.findViewById(R.id.pb_connecting);
            logMessage("bluetooth .. adapter", "Sensor view holder");
        }
    }
}
