package com.si_ware.neospectra.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;


import com.si_ware.neospectra.Adapters.IntroViewPagerAdapter;
import com.si_ware.neospectra.Models.ScreemItem;
import com.si_ware.neospectra.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.si_ware.neospectra.Global.GlobalVariables.bluetoothAPI;

public class SplashScreenActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnGetStarted;
    Animation btnAnim;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ngecek aplikasi

        if(restorePrefData()){

            Intent intent = new Intent(getApplicationContext(),MainPage.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_splash_screen);

        tabIndicator = findViewById(R.id.tab_indicator);
        btnGetStarted = findViewById(R.id.btn_get_started);




        //fill list screen

        final List<ScreemItem> mList = new ArrayList<>();
        mList.add(new ScreemItem("Pindai tanahmu","Pindai tanahmu menggunakan alat pemindai tanah",R.drawable.splash1));
        mList.add(new ScreemItem("Temnukan data unsur!","Kamu dapat melihat data dari hasil pindai tanahmu disini",R.drawable.splash2));
        mList.add(new ScreemItem("Simpan datamu","Kamu dapat menyimpan data hasil pindai tanahmu",R.drawable.splash3));

        // setup viewPager

        screenPager = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        skip =  findViewById(R.id.skip);
        //setup tabLayout Pager

        tabIndicator.setupWithViewPager(screenPager);

        // tabLayout add change listener  BaseOnTabSelectedListener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()== mList.size()-1) {

                    loaddLastScreen();

                } else {
                    btnGetStarted.setVisibility(View.GONE);
                    skip.setVisibility(View.VISIBLE);
                    tabIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        // skip click listener

        skip.setOnClickListener(v -> {

            Intent intent = new Intent(SplashScreenActivity.this, IntroActivity.class);
            startActivity(intent);


        });


        // btn start click listener

        btnGetStarted.setOnClickListener(v -> {

            Intent intent = new Intent(SplashScreenActivity.this, IntroActivity.class);
            startActivity(intent);

            //shared preferences

            savePreferences();
            finish();


        });


    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        Boolean beforeOpened = pref.getBoolean("IS_FIRST_TIME_LAUNCHh",false);
        return beforeOpened;
    }

    private void savePreferences() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("IS_FIRST_TIME_LAUNCHh",true);
        editor.commit();

    }

    private void loaddLastScreen(){
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        skip.setVisibility(View.INVISIBLE);

        // setup animation

        btnGetStarted.setAnimation(btnAnim);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Disconnect the device before exit from the application
        if (bluetoothAPI != null) bluetoothAPI.disconnectFromDevice();
    }
}