package com.si_ware.neospectra.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.si_ware.neospectra.R;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GOTO_FRAGMENT_SCAN = 0;
    public static final int GOTO_FRAGMENT_CONFIGURE = 1;
    public static final int GOTO_FRAGMENT_SHOP = 2;
    public static final int GOTO_FRAGMENT_ABOUT_US = 3;
    public static final int GOTO_FRAGMENT_HISTORY = 4;
    public static final int GOTO_FRAGMENT_POWER = 5;
    public static final int GOTO_FRAGMENT_OTA = 6;

    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    void handleItemSelection(@NonNull MenuItem item){
        int id = item.getItemId();
       // if (id == R.id.nav_disconnect) {
            //startActivity(new Intent(this, SettingsActivity.class));
//        } else if (id == R.id.nav_home) {
//            startActivity(new Intent(this, MainActivity.class));
//        }
//        else if (id == R.id.nav_ota) {
//            Intent iOta = new Intent(this, MainActivity.class);
//            iOta.putExtra("goto", GOTO_FRAGMENT_OTA);
//            startActivity(iOta);
//        }
//        else if (id == R.id.nav_history) {
//            Intent iHistory = new Intent(this, MainActivity.class);
//            iHistory.putExtra("goto", GOTO_FRAGMENT_HISTORY);
//            startActivity(iHistory);
//        }
//        else if (id == R.id.nav_configure) {
//            Intent iConfigure = new Intent(this, ResultsActivity.class);
//            iConfigure.putExtra("goto", GOTO_FRAGMENT_CONFIGURE);
//            startActivity(iConfigure);
//        }
//        else if (id == R.id.nav_power_settings) {
//            Intent iConfigure = new Intent(this, ResultsActivity.class);
//            iConfigure.putExtra("goto", GOTO_FRAGMENT_POWER);
//            startActivity(iConfigure);
//        }
       // } else if(id == R.id.nav_about_us){
            //Log.v("btnPress", "pressed about from onNavigationItemSelected 2");
            //startActivity(new Intent(this, MainActivity.class).putExtra("goto", GOTO_FRAGMENT_ABOUT_US));
       // }

    }
}
