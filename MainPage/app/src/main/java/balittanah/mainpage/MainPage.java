package balittanah.mainpage;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import balittanah.mainpage.ui.dataPage.dataPageFragment;
import balittanah.mainpage.ui.exportPage.exportPageFragment;
import balittanah.mainpage.ui.locationPage.locationPageFragment;
import balittanah.mainpage.ui.recomendationPage.recomendationPageFragment;
import balittanah.mainpage.ui.scanPage.scanPageFragment;

public class MainPage extends AppCompatActivity {

    scanPageFragment scanPageFragment = new scanPageFragment();
    dataPageFragment dataPageFragment = new dataPageFragment();
    locationPageFragment locationPageFragment = new locationPageFragment();
    recomendationPageFragment recomendationPageFragment = new recomendationPageFragment();
    exportPageFragment exportPageFragment = new exportPageFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        getSupportActionBar().setTitle("Scanning");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, scanPageFragment);
        fragmentTransaction.commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                if (item.getItemId()==R.id.navigation_scan){
                    fragmentTransaction.replace(R.id.nav_host_fragment, scanPageFragment);
                    getSupportActionBar().setTitle("Scanning");
                } else if (item.getItemId()==R.id.navigation_data){
                    fragmentTransaction.replace(R.id.nav_host_fragment, dataPageFragment);
                    getSupportActionBar().setTitle("balittanah.mainpage.Data Unsur");
                } else if (item.getItemId()==R.id.navigation_location){
                    fragmentTransaction.replace(R.id.nav_host_fragment, locationPageFragment);
                    getSupportActionBar().setTitle("Info Lokasi");
                } else if (item.getItemId()==R.id.navigation_recomendation) {
                    fragmentTransaction.replace(R.id.nav_host_fragment, recomendationPageFragment);
                    getSupportActionBar().setTitle("Rekomendasi Pupuk");
                } else {
                    fragmentTransaction.replace(R.id.nav_host_fragment, exportPageFragment);
                    getSupportActionBar().setTitle("Export");
                }
                fragmentTransaction.commit();
                return true;
            }
        });
    }
}