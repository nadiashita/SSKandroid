package com.si_ware.neospectra.Models;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.si_ware.neospectra.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.si_ware.neospectra.DataIO.DataIO.retrieveUserModules;
//import static com.si_ware.neospectra.DataIO.DataIO.saveModuleData2SharedFile;

/**
 * Created by AmrWinter on 12/13/2017.
 */

public class dbUser {

    private String userName, userPassword, userID, userEmail, token;
    private List<dbModule> userModules;
    private boolean isUserLoggedIn;
    private dbSettings userSettings;
    private int lengthOfHistory = 10;

    private static final dbUser ourInstance = new dbUser();

    public static dbUser getUserInstance() {
        return ourInstance;
    }

    private dbUser() {
        // TODO: 1/3/18 Get the user profile from sharedPreferences.
        // TODO: 1/3/18 If the user has only one module set it as primary.
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<dbModule> getUserModules(Context mContext) {
        Set<String> tmp = retrieveUserModules(mContext);
        if (tmp != null)
            this.userModules = convertStringData2DoubleData(retrieveUserModules(mContext));
        else
            Toast.makeText(mContext,
                    mContext.getString(R.string.err_no_modules),
                    Toast.LENGTH_SHORT).show();
        return new ArrayList<>(userModules);
    }

    private ArrayList<dbModule> convertStringData2DoubleData(Set<String> strings) {
        // TODO: 1/3/18 Stooped Here
        return null;
    }

    public void setUserModules(List<dbModule> userModules) {
        this.userModules = userModules;
    }

    public void addUserModule(Context mContext, dbModule module){
//        saveModuleData2SharedFile(mContext, module);
        // TODO: 1/3/18 WAS HERE.

        this.userModules.add(module);
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }

    public void clearUserValues(){
        setUserName("");
        setUserEmail("");
        setUserID("");
        setToken("");
        clearModulesID();
        setUserLoggedIn(false);
//        setUserAvatar(null);
    }

    public dbSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(dbSettings userSettings) {
        this.userSettings = userSettings;
    }

    private void clearModulesID() {
        this.userModules = null;
    }

    public int getLengthOfHistory() {
        return lengthOfHistory;
    }

    public void setLengthOfHistory(int lengthOfHistory) {
        this.lengthOfHistory = lengthOfHistory;
    }

    @Override
    public String toString() {
        return "dbUser{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userID='" + userID + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", token='" + token + '\'' +
                ", userModules=" + userModules +
                ", isUserLoggedIn=" + isUserLoggedIn +
                ", userSettings=" + userSettings +
                ", lengthOfHistory=" + lengthOfHistory +
                '}';
    }
}
