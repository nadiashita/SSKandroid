package com.si_ware.neospectra.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.si_ware.neospectra.R;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GOTO_FRAGMENT_SCAN = 0;
    public static final int GOTO_FRAGMENT_MY_MODULES = 1;
    public static final int GOTO_FRAGMENT_BUY_MODULES = 2;
    public static final int GOTO_FRAGMENT_ABOUT_US = 3;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        handleItemSelection(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        handleItemSelection(item);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void handleItemSelection(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.nav_login || id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
        }
        //TODO: Based on the flag goto handle which fragment should be inflated in the MainActivity.
        else if (id == R.id.nav_modules) {
            Intent iMyModules = new Intent(this, MainActivity.class);
            iMyModules.putExtra("goto", GOTO_FRAGMENT_MY_MODULES);
            startActivity(iMyModules);
        }
        else if (id == R.id.nav_new_modules) {
            Intent iBuyModules = new Intent(this, MainActivity.class);
            iBuyModules.putExtra("goto", GOTO_FRAGMENT_BUY_MODULES);
            startActivity(iBuyModules);
        }
        else if (id == R.id.nav_history) {
            Intent iHistory = new Intent(this, HistoryActivity.class);
            startActivity(iHistory);
        }
        else if(id == R.id.nav_about_us){
            Log.v("btnPress", "pressed about from onNavigationItemSelected 2");
            startActivity(new Intent(this, MainActivity.class).putExtra("goto", GOTO_FRAGMENT_ABOUT_US));
        }
    }
}
