package com.si_ware.neospectra.Activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.si_ware.neospectra.Activities.Interfaces.DataPage.DataPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.ExportPage.ExportPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.LocationPage.LocationPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.RecomendationPage.RecomendationPageFragment;
import com.si_ware.neospectra.Activities.Interfaces.ScanPage.ScanPageFragment;
import com.si_ware.neospectra.BluetoothSDK.SWS_P3API;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Scan.Presenter.ScanPresenter;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;
import static com.si_ware.neospectra.Global.GlobalVariables.gApodizationFunction;
import static com.si_ware.neospectra.Global.GlobalVariables.gCorrectionMode;
import static com.si_ware.neospectra.Global.GlobalVariables.gFftPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gInterpolationPoints;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsFftEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gIsInterpolationEnabled;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainSettings;
import static com.si_ware.neospectra.Global.GlobalVariables.gOpticalGainValue;
import static com.si_ware.neospectra.Global.GlobalVariables.gRunMode;
import static com.si_ware.neospectra.Global.GlobalVariables.progressBarPosition;
import static com.si_ware.neospectra.Global.GlobalVariables.scanTime;

public class MainPage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Context mContext;
    MenuInflater menuInflater;
    boolean isExportPage = false;
    boolean isDataUnsur = false;
    final int share_item_id = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_keyboard_arrow_left);
//        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        menuInflater = getMenuInflater();

        setContentView(R.layout.activity_main_page);
//        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Scanning" + "</font>")));
//        getSupportActionBar().setTitle("Scanning");
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFragment(new ScanPageFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bn_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

//         Create new instance from bluetoothApi if it null
//        if (bluetoothAPI == null) {
//            bluetoothAPI = new SWS_P3API(this, mContext);
//        }

        mContext = this;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

//        // Get all needed configuration settings
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
//        gRunMode = preferences.getString("run_mode", GlobalVariables.runMode.Single_Mode.toString());
//        gIsInterpolationEnabled = preferences.getBoolean("linear_interpolation_switch", false);
//        gInterpolationPoints = preferences.getString("data_points", GlobalVariables.pointsCount.points_257.toString());
//        gIsFftEnabled = preferences.getBoolean("fft_settings_switch", false);
//        gApodizationFunction = preferences.getString("apodization_function", GlobalVariables.apodization.Boxcar.toString());
//        gFftPoints = preferences.getString("fft_points", GlobalVariables.zeroPadding.points_8k.toString());
//        gOpticalGainSettings = preferences.getString("optical_gain_settings", "Default");
//        gOpticalGainValue = preferences.getInt(gOpticalGainSettings, 0);
//        gCorrectionMode = preferences.getString("wavelength_correction", GlobalVariables.wavelengthCorrection.Self_Calibration.toString());


//        ArrayAdapter<CharSequence> bluetoothSpinnerAdapter = ArrayAdapter.createFromResource(mContext,
//                R.array.BLE_Services, android.R.layout.simple_spinner_item);
//        bluetoothSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Menu menu = null;
        Fragment fragment = null;
        AppCompatActivity appCompatActivity = null;
        switch (menuItem.getItemId()) {
            case R.id.scan_page:
                fragment = new ScanPageFragment();
//                getSupportActionBar().setTitle("Scanning");
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#74A7FF")));
//                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Scanning" + "</font>")));
                isDataUnsur = false;
                isExportPage = false;
//                invalidateOptionsMenu();
                break;
            case R.id.data_page:
                fragment = new DataPageFragment();
//                getSupportActionBar().setTitle("Data Unsur");
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Data Unsur" + "</font>")));
                isDataUnsur = true;
                isExportPage = false;
//                invalidateOptionsMenu();
                break;
            case R.id.location_page:
                fragment = new LocationPageFragment();
//                getSupportActionBar().setTitle("Info Lokasi");
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Info Lokasi" + "</font>")));
                isDataUnsur = false;
                isExportPage = false;
//                invalidateOptionsMenu();
                break;
            case R.id.recomendation_page:
                fragment = new RecomendationPageFragment();
//                getSupportActionBar().setTitle("Rekomendasi Pupuk");
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Rekomendasi Pupuk" + "</font>")));
                isDataUnsur = false;
                isExportPage = false;
//                invalidateOptionsMenu();
                break;
            case R.id.export_page:
                fragment = new ExportPageFragment();
//                getSupportActionBar().setTitle("Export Data");
//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
//                getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#000000\">" + "Export Data" + "</font>")));
                isDataUnsur = false;
                isExportPage = true;
//                invalidateOptionsMenu();
//                menuInflater.inflate(R.menu.menu_export, menu);
//                appCompatActivity = new Export();
//                Intent intent = new Intent(MainPage.this, Export.class);
//                startActivity(intent);
                break;
        }
        loadFragment(fragment);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId()==R.id.share){
////
////        } if (item.getItemId()==R.id.export_data){
////
////        } if (item.getItemId()==R.id.delete){
////
////        }
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                Intent i = new Intent(new Intent(MainPage.this, SettingsActivity.class));
//                startActivity(i);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        menuInflater.inflate(R.menu.menu_export, menu);
////        MenuItem item = menu.findItem(R.id.clear_hist);
//        return super.onCreateOptionsMenu(menu);
//    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        if (isExportPage && !isDataUnsur) {
//            if (menu.findItem(share_item_id) == null) {
//                menuInflater.inflate(R.menu.menu_export, menu);
//                MenuItem shareitem = menu.add(Menu.NONE, share_item_id, 0, "Options");
//                shareitem.setIcon(R.drawable.icons_setting);
//                shareitem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
//                shareitem.setVisible(false);
//            }
//        }
//        if (isDataUnsur && !isExportPage) {
//            if (menu.findItem(share_item_id) == null) {
//                final Drawable setings = getResources().getDrawable(R.drawable.ic_settings);
//                setings.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
//                MenuItem shareitem = menu.add(Menu.NONE, share_item_id, 0, "Options");
//                shareitem.setIcon(setings);
//                shareitem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            }
//        } else {
//            menu.removeItem(share_item_id);
//        }
//        super.onPrepareOptionsMenu(menu);
//        return true;
//    }
}
