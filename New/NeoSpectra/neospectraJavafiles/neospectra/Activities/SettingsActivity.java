package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.Models.dbSettings;
import com.si_ware.neospectra.Models.dbUser;
import com.si_ware.neospectra.R;

import java.util.ArrayList;

import static com.si_ware.neospectra.DataIO.DataIO.readFileAsString;
import static com.si_ware.neospectra.DataIO.DataIO.writeStringAsFile;
import static com.si_ware.neospectra.DummyClassForTesting.getTestingUserModules;
import static com.si_ware.neospectra.Global.Factory.showAlertMessage;

public class SettingsActivity extends NavDrawerActivity implements
        View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    DrawerLayout drawer;

    private Spinner choosePrimaryModule, setHistoryLength;
    private dbUser user;
    private Button btnLogIn, btnLogOut;
    private ArrayList<String> primaryModuleSpinner;
    private Integer[] historyLengthSpinner;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = dbUser.getUserInstance();
        //  initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();
        //list of history length
        historyLengthSpinner = new Integer[]{10, 15, 20, 25, 30};

        //Get modules names from the list of user modules and put it to the spinner array.
        primaryModuleSpinner = new ArrayList<>();
        // TODO: 12/30/17 "TESTING_LINE" - Delete this line in the production
        fillSpinnerWithModulesName(getTestingUserModules(this));

        //Navigation Drawer
         drawer = findViewById(R.id.drawer_layout);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_settings, null, false);
        
        // Primary module spinner
        choosePrimaryModule = contentView
                .findViewById(R.id.spnr_primary_module)
                .findViewById(R.id.spnr_choose_module);
        ArrayAdapter<String> chooseModuleAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, primaryModuleSpinner);
        chooseModuleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choosePrimaryModule.setAdapter(chooseModuleAdapter);
        choosePrimaryModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dbSettings settings = new dbSettings();
                settings.setPrimaryModuleName(primaryModuleSpinner.get(position));
                user.setUserSettings(settings);
                Toast.makeText(SettingsActivity.this,
                        "Primary module is: " + user.getUserSettings().getPrimaryModuleName(),
                        Toast.LENGTH_SHORT).show();
                Log.e("err", "spinner item selected is: " + primaryModuleSpinner.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("err", "No item selected");
            }
        });

        // Spinner view = History length
        setHistoryLength = contentView.findViewById(R.id.spnr_history_length);
        ArrayAdapter<Integer> setHistoryLengthAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, historyLengthSpinner);
        setHistoryLengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setHistoryLength.setAdapter(setHistoryLengthAdapter);
        setHistoryLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user.setLengthOfHistory(historyLengthSpinner[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        drawer.addView(contentView, 0);
        // status text view
        mStatusTextView = findViewById(R.id.status);
        // buttons, login and logout
        btnLogIn = findViewById(R.id.sign_in_button);
        btnLogOut = findViewById(R.id.sign_out_button);
        // Button listeners
        btnLogIn.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        if (user.isUserLoggedIn()){
            btnLogIn.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.VISIBLE);
        }else {
            btnLogIn.setVisibility(View.VISIBLE);
            btnLogOut.setVisibility(View.GONE);
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void fillSpinnerWithModulesName(ArrayList<dbModule> userModules) {
        //Get modules name in string array
        for (dbModule module :
                userModules) {
            primaryModuleSpinner.add(module.getModuleName());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Google Sign In was successful, authenticate with Firebase
            firebaseAuthWithGoogle(account);
            // FIXME: 2/9/18 Return here if you want to cancel firebase authentication method
            // Signed in successfully, show authenticated UI.
//            updateUI(account);

            //Goto the main activity
            Intent iMain = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(iMain);
        } catch (ApiException e) {
            showAlertMessage(this,
                    getString(R.string.title_err_failed_login),
                    getString(R.string.title_body_failed_login));

            Log.e(TAG, "err_failedToLogin " + e.getMessage());
            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    updateUI(null);
                }
            });

        FirebaseAuth.getInstance().signOut();

    }

    private void updateUI(@Nullable FirebaseUser account) {
        if (account != null) {
            mStatusTextView.setText(String.format("%s %s",
                    getString(R.string.welcome),
                    account.getDisplayName()));
            btnLogIn.setVisibility(View.GONE);
            btnLogOut.setVisibility(View.VISIBLE);
            //Get info from google account
            fillUserDataFields(account);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            btnLogIn.setVisibility(View.VISIBLE);
            btnLogOut.setVisibility(View.GONE);
            user.clearUserValues();
        }
    }

    private void fillUserDataFields(@Nullable FirebaseUser account) {

        // Get information from google server
        user.setUserName(account.getDisplayName());
        user.setUserEmail(account.getEmail());
        user.setUserID(account.getUid());
//        user.setUserAvatar(account.getPhotoUrl());
        user.setUserLoggedIn(true);

        // TODO: 1/3/18 Get user profile from shared File.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                if (hasInternetConnection()){
                    signIn();
                }
                else {
                    showAlertMessage(this,
                            getString(R.string.title_err_net_connetion),
                            getString(R.string.msg_err_net_connetion));
                }
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }

    public boolean hasInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }
}