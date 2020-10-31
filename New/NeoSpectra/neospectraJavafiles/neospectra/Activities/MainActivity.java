package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.si_ware.neospectra.Activities.MainFragments.AboutFragment;
import com.si_ware.neospectra.Activities.MainFragments.BuyModulesFragment;
import com.si_ware.neospectra.Activities.MainFragments.MyModulesFragment;
import com.si_ware.neospectra.DummyClassForTesting;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Network.FireBaseIO;
import com.si_ware.neospectra.R;
import com.si_ware.neospectra.Activities.MainFragments.ScanFragment;

import java.io.File;

import static com.si_ware.neospectra.DummyClassForTesting.getRandomModule;

public class MainActivity extends NavDrawerActivity {

    DrawerLayout drawer;
    private Fragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawer = findViewById(R.id.drawer_layout);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        Bundle fragmentFlag = getIntent().getExtras();
        if (fragmentFlag != null){
            int FRAGMENT_FLAG = fragmentFlag.getInt("goto");
            ReplaceFragment(FRAGMENT_FLAG);
        }else {

            ReplaceFragment(GOTO_FRAGMENT_SCAN);
            setTitle(R.string.title_fragment_home);
        }

        FireBaseIO fireBase = new FireBaseIO();
        dbModule mdl = getRandomModule(this, "Milk");
        Log.v("mdl", mdl.toString());
//        fireBase.uploadModuleData(this, mdl);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        downloadFile();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                setTitle(R.string.title_fragment_home);
                ReplaceFragment(GOTO_FRAGMENT_SCAN);
                return true;
            case R.id.nav_modules:
                setTitle(R.string.title_fragment_modules);
                ReplaceFragment(GOTO_FRAGMENT_MY_MODULES);
                return true;
            case R.id.nav_new_modules:
                setTitle(R.string.title_fragment_new_modules);
                ReplaceFragment(GOTO_FRAGMENT_BUY_MODULES);
                return true;
            case R.id.nav_login:
                gotoLogin();
                return true;
            case R.id.nav_about_us:
                Log.v("btnPress", "pressed about from onNavigationItemSelected 1");
                ReplaceFragment(GOTO_FRAGMENT_ABOUT_US);
                return true;
        }
        return false;
        }
    };

    private void gotoLogin() {
        Intent iLogin = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(iLogin);
    }

    private void ReplaceFragment(int fragment_id) {
        myFragment = null;
        switch (fragment_id){
            case GOTO_FRAGMENT_SCAN:
                setTitle(R.string.title_fragment_home);
                myFragment = new ScanFragment();
                break;
            case GOTO_FRAGMENT_MY_MODULES:
                setTitle(R.string.title_fragment_modules);
                myFragment = new MyModulesFragment();
                break;
            case GOTO_FRAGMENT_BUY_MODULES:
                setTitle(R.string.title_fragment_new_modules);
                myFragment = new BuyModulesFragment();
                break;
            case GOTO_FRAGMENT_ABOUT_US:
                setTitle(R.string.title_fragment_about_us);
                Log.v("btnPress", "About Us");
                myFragment = new AboutFragment();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        try{
            String FragmentName = myFragment.getClass().getName();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_main, myFragment)
                    .addToBackStack(FragmentName).commit();
        }catch (NullPointerException e){
            Log.e("err", e.getMessage() + "");
        }
    }

    @Override
    public void onBackPressed() {
        String appName = "com.si_ware.neospectra.Fragments.";
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (myFragment.getClass().getName().equals(appName + "ScanFragment")){
            finish();
        }else if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    public void downloadFile(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://originalneospectra.appspot.com/Modules/");
        StorageReference  islandRef = storageRef.child("Milk.txt");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "file_name");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"imageName.txt");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ","local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
                Toast.makeText(MainActivity.this, "File Downloaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ","local tem file not created  created " +exception.toString());
                Toast.makeText(MainActivity.this, "File Download not completed", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
